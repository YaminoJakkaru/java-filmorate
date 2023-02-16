package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import ru.yandex.practicum.filmorate.rowMapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static final Logger LOG = LoggerFactory.getLogger(FilmStorage.class);
    private final JdbcTemplate jdbcTemplate;
    SimpleJdbcInsert simpleJdbcInsertFilm;
    private static final String BASE_FIND_QUERY = "select f.*,m.name as mpa_name, group_concat(fg.genre_id)as genres_ids,"
            + "group_concat(g.name) as genres_names, group_concat(fd.director_id)as directors_ids, " +
            " group_concat(d.name) as directors_names, group_concat(fl.user_id) as likes from film as f"
            + " left join mpa as m on f.mpa_id=m.mpa_id left join film_genre as fg on f.film_id=fg.film_id"
            + " left join genre as g on fg.genre_id=g.genre_id left join film_likes as fl on f.film_id=fl.film_id " +
            " left join film_director as fd on f.film_id=fd.film_id left join director as d on fd.director_id=d.director_id";
    private static final String GROUP_BY_ID_CLAUSE = " group by f.film_id ";
    private static final String WHERE_ID_CLAUSE = " where f.film_id= ";
    private static final String ORDER_BY_COUNT_CLAUSE = " order by count(fl.user_id) desc ";

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
    }

    @Override
    public List<Film> getAllFilms() {
        String query = BASE_FIND_QUERY + GROUP_BY_ID_CLAUSE;
        return jdbcTemplate.query(query, new FilmMapper());
    }


    @Override
    public Film findFilmById(int id) {
        String query = BASE_FIND_QUERY + WHERE_ID_CLAUSE + id + GROUP_BY_ID_CLAUSE;
        List<Film> films = jdbcTemplate.query(query, new FilmMapper());
        if (films.isEmpty()) {
            LOG.warn("Попытка  получить несуществующий фильм");
            throw new FilmNotFoundException();
        }
        return films.get(0);
    }

    @Override
    public Film createFilm(Film film) {
        int filmId = (int) simpleJdbcInsertFilm.executeAndReturnKey(film.toMap());
        film.getGenres().forEach(genre -> addFilmsGenre(filmId, genre.getId()));
        film.getDirectors().forEach(director -> addFilmsDirector(filmId, director.getId()));
        film.setId(filmId);
        LOG.info("Добавлен фильм");
        return film;
    }

    @Override
    public Film changeFilm(Film film) {
        String query = "select genre_id from genre " +
                "where genre_id in(select genre_id from film_genre where film_id=" + film.getId() + ")";

        List<Integer> genresId = jdbcTemplate.queryForList(query, Integer.class);
        List<Integer> newGenresId = film.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        genresId.stream().filter(genreId -> !newGenresId.contains(genreId)).
                forEach(genreId -> deleteFilmsGenre(film.getId(), genreId));
        newGenresId.stream().filter(genreId -> !genresId.contains(genreId)).
                forEach(genreId -> addFilmsGenre(film.getId(), genreId));

        query = "select director_id from director " +
                "where director_id in(select director_id from film_director where film_id=" + film.getId() + ")";

        List<Integer> directorsId = jdbcTemplate.queryForList(query, Integer.class);
        List<Integer> newDirectorsId = film.getDirectors().stream().map(Director::getId).collect(Collectors.toList());
        directorsId.stream().filter(directorId -> !newDirectorsId.contains(directorId)).
                forEach(directorId -> deleteFilmsDirector(film.getId(), directorId));
        newDirectorsId.stream().filter(directorId -> !directorsId.contains(directorId)).
                forEach(directorId -> addFilmsDirector(film.getId(), directorId));

        String sqlQuery = "update film set " +
                "name = ?, description = ?, release_date = ?,duration=?,mpa_id=? " +
                "where film_id = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (changes == 0) {
            LOG.warn("Попытка изменить несуществующий фильм");
            throw new FilmNotFoundException();
        }
        LOG.info("Данные фильма изменены");
        return findFilmById(film.getId());
    }

    @Override
    public void addLike(int id, int userId) {
        String query = "insert into film_likes(film_id,user_id) values (?,?)";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Поставлен лайк");
    }

    @Override
    public void deleteLike(int id, int userId) {
        String query = "delete from film_likes where film_id=? and user_id=?";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Удален лайк");
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String query = BASE_FIND_QUERY + GROUP_BY_ID_CLAUSE + ORDER_BY_COUNT_CLAUSE + " limit " + count;
        LOG.info("Запрошен топ " + count + " популярных фильмов");
        return jdbcTemplate.query(query, new FilmMapper());
    }

    @Override
    public void addFilmsGenre(int filmId, int genreId) {
        String sqlQuery = "insert into film_genre (film_id,genre_id) values (?,?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void deleteFilmsGenre(int filmId, int genreId) {
        String sqlQuery = "delete from film_genre where film_id=? and genre_id=?";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void addFilmsDirector(int filmId, int directorId) {
        String sqlQuery = "insert into film_director (film_id,director_id) values (?,?)";
        jdbcTemplate.update(sqlQuery, filmId, directorId);
    }

    @Override
    public void deleteFilmsDirector(int filmId, int directorId) {
        String sqlQuery = "delete from film_director where film_id=? and director_id=?";
        jdbcTemplate.update(sqlQuery, filmId, directorId);
    }
}
