package ru.yandex.practicum.filmorate.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public enum UserMapper implements RowMapper<User> {

    INSTANCE;
    public static UserMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        if (resultSet.getString("friends") != null) {
            Arrays.stream(resultSet.getString("friends").split(","))
                    .forEach(i -> user.addFriend(Integer.parseInt(i)));
        }
        return user;
    }
}
