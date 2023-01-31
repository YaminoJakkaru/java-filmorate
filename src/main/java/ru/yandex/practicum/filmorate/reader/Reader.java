package ru.yandex.practicum.filmorate.reader;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class Reader {
    public Film readFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder().
                id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .mpa(readMpa(resultSet, rowNum))
                .build();
    }

    public Mpa readMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder().id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public Genre readGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder().id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public User readUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();


    }
}
