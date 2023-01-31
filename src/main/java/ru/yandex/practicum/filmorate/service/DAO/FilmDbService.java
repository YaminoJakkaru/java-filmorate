package ru.yandex.practicum.filmorate.service.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.reader.Reader;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Qualifier("FilmDbService")
public class FilmDbService implements FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final JdbcTemplate jdbcTemplate;
    private final Reader reader;
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    public FilmDbService(JdbcTemplate jdbcTemplate, Reader reader, @Qualifier("FilmDbStorage") FilmStorage filmStorage) {

        this.jdbcTemplate = jdbcTemplate;
        this.reader = reader;
        this.filmStorage = filmStorage;
    }

    @Override
    public void addLike(int id, int userId) {
        String queryCheck="select user_id from users where user_id=" + userId;
        if (!jdbcTemplate.queryForRowSet(queryCheck).next()) {
            log.warn("Попытка поставить лайк несуществующем пользователем");
            throw new UserNotFoundException();
        }
        String query="insert into film_likes(film_id,user_id) values (?,?)";
        jdbcTemplate.update(query, id, userId);
        log.info("Поставлен лайк");
    }

    @Override
    public void deleteLike(int id, int userId) {
        String queryCheck="select user_id from users where user_id=" + userId;
        if (!jdbcTemplate.queryForRowSet(queryCheck).next()) {
            log.warn("Попытка удалить лайк несуществующем пользователем");
            throw new UserNotFoundException();
        }
        String query="delete from film_likes where film_id=? and user_id=?";
        jdbcTemplate.update(query, id, userId);
        log.info("Удален лайк");
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String query="select f.*,count(fl.user_id) from film as f "
                +"left join  film_likes as fl on f.film_id=fl.film_id"
                +" group by f.film_id order by count(fl.user_id) desc limit " + count;

        List<Film> films = jdbcTemplate.query(query, reader::readFilm);
        for (Film film : films) {
            fillFilm(film);
        }
        log.info("Запрошен топ " + count + " популярных фильмов");
        return films;
    }
    private void fillFilm(Film film) {
        String queryGenre="select * from genre where genre_id in(select genre_id from film_genre"
                + " where film_id=" + film.getId() + ")";
        String queryLike="select user_id from film_likes";
        String queryMpa="select * from mpa where mpa_id=" + film.getMpa().getId();

        jdbcTemplate.query(queryGenre, reader::readGenre).forEach(film::addGenres);
        jdbcTemplate.queryForList(queryLike, Integer.class).forEach(film::addLikes);
        film.setMpa(jdbcTemplate.queryForObject(queryMpa, reader::readMpa));
    }
}
