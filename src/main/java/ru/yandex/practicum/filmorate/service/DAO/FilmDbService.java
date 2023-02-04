package ru.yandex.practicum.filmorate.service.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.rowMapper.FilmMapper;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Service
@Qualifier("FilmDbService")
public class FilmDbService implements FilmService {

    private static final Logger LOG = LoggerFactory.getLogger(FilmService.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int id, int userId) {
        String queryCheck = "select user_id from users where user_id=" + userId;
        if (!jdbcTemplate.queryForRowSet(queryCheck).next()) {
            LOG.warn("Попытка поставить лайк несуществующем пользователем");
            throw new UserNotFoundException();
        }
        String query = "insert into film_likes(film_id,user_id) values (?,?)";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Поставлен лайк");
    }

    @Override
    public void deleteLike(int id, int userId) {
        String queryCheck = "select user_id from users where user_id=" + userId;
        if (!jdbcTemplate.queryForRowSet(queryCheck).next()) {
            LOG.warn("Попытка удалить лайк несуществующем пользователем");
            throw new UserNotFoundException();
        }
        String query = "delete from film_likes where film_id=? and user_id=?";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Удален лайк");
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String query = "select f.*,m.name as mpa_name, group_concat(fg.genre_id)as genres_ids"
                + ",group_concat(g.name) as genres_names, group_concat(fl.user_id) as likes from film as f"
                + " left join mpa as m on f.mpa_id=m.mpa_id left join film_genre as fg on f.film_id=fg.film_id"
                + " left join genre as g on fg.genre_id=g.genre_id left join film_likes as fl on f.film_id=fl.film_id"
                + " group by f.film_id order by count(fl.user_id) desc limit " + count;
        LOG.info("Запрошен топ " + count + " популярных фильмов");
        return jdbcTemplate.query(query, new FilmMapper());
    }
}
