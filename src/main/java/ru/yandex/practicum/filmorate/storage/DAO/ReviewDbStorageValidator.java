package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.ReviewStorageValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("ReviewDbStorageValidator")
public class ReviewDbStorageValidator implements ReviewStorageValidator {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorageValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean checkUserFilmValidate(int userId, int film_id) {
        String queryCheck = "select count(user_id) AS a from users where user_id = " + userId +
                " UNION ALL select count (film_id) AS a from film where film_id = " + film_id;
        return getCount(queryCheck) == 2;
    }

    @Override
    public int checkUserAndLike(int id, int userId, boolean isLike) {
        String queryCheck;
        if (isLike == true) {
            queryCheck = "select count(user_id) AS a from users where user_id = " + userId +
                         " union all select review_like_id from review_like where review_id = " + id +
                    " and user_id = " + userId;
        } else {
            queryCheck = "select count(user_id) AS a from users where user_id = " + userId +
                    " union all select review_dislike_id from review_dislike where review_id = " + id +
                    " and user_id = " + userId;
        }
        return getCount(queryCheck);
    }

    private int getCount(String queryCheck) {
        List<Integer> checkList = jdbcTemplate.query(queryCheck, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        });
        int result = 0;
        for (Integer i : checkList) {
            result = result + i;
        }
        return result;
    }
}
