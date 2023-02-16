package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.ReviewStorageValidator;

@Component
@Qualifier("ReviewDbStorageValidator")
public class ReviewDbStorageValidator implements ReviewStorageValidator {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorageValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean reviewLikeValidate(int reviewId, int userId, boolean isLike) {
        String queryCheck = "select review_like_id from review_like where review_id = " + reviewId +
                " and user_id = " + userId + " and is_like = " + isLike;
        return jdbcTemplate.queryForRowSet(queryCheck).next();
    }
}
