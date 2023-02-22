package ru.yandex.practicum.filmorate.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;


public enum FilmMapper implements RowMapper<Film> {
    INSTANCE;
    public static FilmMapper getInstance() {
        return INSTANCE;
    }
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .mpa(Mpa.builder().id(resultSet.getInt("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .build();
        if (resultSet.getString("genres_ids") != null) {
            String[] genreIds = resultSet.getString("genres_ids").split(",");
            String[] genreNames = resultSet.getString("genres_names").split(",");
            for (int i = 0; i < genreIds.length; i++) {
                    film.addGenres(Genre.builder().id(Integer.parseInt(genreIds[i])).name(genreNames[i]).build());
            }
        }
        if (resultSet.getString("likes") != null) {
            Arrays.stream(resultSet.getString("likes").split(","))
                    .forEach(like -> film.addLike(Integer.parseInt(like)));
        }
        if (resultSet.getString("directors_ids") != null) {
            String[] directorIds = resultSet.getString("directors_ids").split(",");
            String[] directorNames = resultSet.getString("directors_names").split(",");
            for (int i = 0; i < directorIds.length; i++) {
                film.addDirectors(Director.builder().id(Integer.parseInt(directorIds[i])).name(directorNames[i]).build());
            }
        }
        return film;
    }
}
