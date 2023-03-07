package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.ReviewStorageValidator;


@Component
public class ReviewDbStorageValidator implements ReviewStorageValidator {

    private final JdbcTemplate jdbcTemplate;
    private static final int REQUIRED_QUANTITY = 2;

    public ReviewDbStorageValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean reviewLikeValidate(int reviewId, int userId, boolean isLike) {
        String queryCheck;
        if (isLike) {
            queryCheck = "select review_like_id from review_like where review_id = " + reviewId +
                    " and user_id = " + userId;
            return jdbcTemplate.queryForRowSet(queryCheck).next();
        }
        queryCheck = "select review_dislike_id from review_dislike where review_id = " + reviewId +
                    " and user_id = " + userId;
        return jdbcTemplate.queryForRowSet(queryCheck).next();
    }

    @Override
    public boolean checkUserFilmValidate(int userId, int film_id) {
        String queryCheck = "select count(select user_id from users where user_id = " + userId +
                ") + count(select film_id from film where film_id = " + film_id + ")";
        return jdbcTemplate.queryForObject(queryCheck, Integer.class) == REQUIRED_QUANTITY;
    }
}
