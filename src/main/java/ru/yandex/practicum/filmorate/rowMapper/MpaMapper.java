package ru.yandex.practicum.filmorate.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder().id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
