package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.reader.Reader;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(FilmStorage.class);
    private final FilmValidator filmValidator;
    private final JdbcTemplate jdbcTemplate;
    private final Reader reader;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmValidator filmValidator, Reader reader) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidator = filmValidator;
        this.reader = reader;
    }

    @Override
    public List<Film> getAllFilms() {
        String query="select * from film";

        List<Film> films = jdbcTemplate.query(query, reader::readFilm);
        for (Film film : films) {
            fillFilm(film);
        }
        return films;
    }


    @Override
    public Film findFilmById(int id) {
        try {
            String query="select * from film where film_id=" + id;

            Film film = jdbcTemplate.query(query, reader::readFilm).get(0);
            fillFilm(film);

            return film;
        } catch (IndexOutOfBoundsException e) {
            log.warn("Попытка  получить несуществующий фильм");
            throw new FilmNotFoundException();
        }
    }

    @Override
    public Film createFilm(Film film) {
        if (!filmValidator.validate(film)) {
            log.warn("Валидация фильма не пройдена");
            throw new ValidationException();
        }
            SimpleJdbcInsert simpleJdbcInsertFilm = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns("film_id");

            int filmId = (int) simpleJdbcInsertFilm.executeAndReturnKey(film.toMap());
            film.setId(filmId);
            film.getGenres().forEach(genre -> addFilmsGenre(filmId, genre.getId()));
            log.info("Добавлен фильм");
            return findFilmById(filmId);
    }

    @Override
    public Film changeFilm(Film film) {
        String queryCheck="select film_id from film where film_id=" + film.getId();
        if (!jdbcTemplate.queryForRowSet(queryCheck).next()) {
            log.warn("Попытка изменить несуществующий фильм");
            throw new FilmNotFoundException();
        }
        if (!filmValidator.validate(film)) {
            log.warn("Валидация фильма не пройдена");
            throw new ValidationException();
        }
        String query="select genre_id from genre " +
                "where genre_id in(select genre_id from film_genre where film_id=" + film.getId() + ")";

        List<Integer> genresId = jdbcTemplate.queryForList(query, Integer.class);
        List<Integer> newGenresId = film.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        genresId.stream().filter(genreId -> !newGenresId.contains(genreId)).
                forEach(genreId -> deleteFilmsGenre(film.getId(), genreId));
        newGenresId.stream().filter(genreId -> !genresId.contains(genreId)).
                forEach(genreId -> addFilmsGenre(film.getId(), genreId));

        String sqlQuery = "update film set " +
                "name = ?, description = ?, release_date = ?,duration=?,mpa_id=? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        log.info("Данные фильма изменены");
        return findFilmById(film.getId());
    }

    private void fillFilm(Film film) {
        String queryGenre="select * from genre where genre_id in(select genre_id from film_genre" +
                " where film_id=" + film.getId() + ")";
        String queryLike="select user_id from film_likes";
        String queryMpa="select * from mpa where mpa_id=" + film.getMpa().getId();

        jdbcTemplate.query(queryGenre, reader::readGenre).forEach(film::addGenres);
        jdbcTemplate.queryForList(queryLike, Integer.class).forEach(film::addLikes);
        film.setMpa(jdbcTemplate.queryForObject(queryMpa, reader::readMpa));
    }

    public void addFilmsGenre(int filmId, int genreId) {
        jdbcTemplate.update("insert into film_genre (film_id,genre_id) values (?,?)", filmId, genreId);
    }

    public void deleteFilmsGenre(int filmId, int genreId) {
        jdbcTemplate.update("delete film_genre where film_id=? and genre_id=?", filmId, genreId);
    }
}
