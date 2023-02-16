package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.rowMapper.ReviewMapper;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Component
@Qualifier("ReviewDbStorage")
public class ReviewDbStorage implements ReviewStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewStorage.class);

    private final JdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsertReview;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsertReview = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");
    }

    @Override
    public Review createReview(Review review) {
        int reviewId = (int) simpleJdbcInsertReview.executeAndReturnKey(review.toMap());
        review.setReviewId(reviewId);
        LOG.info("Добавлен отзыв");
        return findReviewById(reviewId);
    }

    @Override
    public Review changeReview(Review review) {
        String sqlQuery = "update reviews set " +
                "content = ?, is_positive = ? where review_id = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        if (changes == 0) {
            LOG.warn("Попытка изменить несуществующий отзыв");
            throw new NotFoundException();
        }
        LOG.info("Данные отзыва изменены");
        return findReviewById(review.getReviewId());
    }

    @Override
    public void breakReview(int id) {
        String query = "delete reviews where review_id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public Review findReviewById(int id) {
        String query = "select * from reviews where review_id = " + id;
        List<Review> reviews = jdbcTemplate.query(query, new ReviewMapper());
        if (reviews.isEmpty()) {
            LOG.warn("Попытка  получить несуществующий отзыв");
            throw new NotFoundException();
        }
        return reviews.get(0);
    }

    @Override
    public List<Review> getReviews(int filmId, int count) {
        if (filmId != 0) {
            String query = "select * from reviews where film_id = ? order by useful DESC, review_id ASC limit ?";
            return jdbcTemplate.query(query, new ReviewMapper(), filmId, count);
        } else {
            String query = "select * from reviews order by useful DESC, review_id ASC limit ?";
            return jdbcTemplate.query(query, new ReviewMapper(), count);
        }

    }

    @Override
    public void addLike(int id, int userId) {
        Review review = findReviewById(id);
        review.setUseful(review.getUseful() + 1);
        String sqlQuery = "update reviews set useful=? where review_id = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                review.getUseful(),
                review.getReviewId());
        if (changes == 0) {
            LOG.warn("Неудачная попытка добавить лайк");
            throw new NotFoundException();
        }
        String query = "insert into review_like(review_id, user_id, is_like) values (?,?, true)";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Поставлен лайк отзыву");
    }

    @Override
    public void addDislike(int id, int userId) {
        Review review = findReviewById(id);
        review.setUseful(review.getUseful() - 1);
        String sqlQuery = "update reviews set useful=? where review_id = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                review.getUseful(),
                review.getReviewId());
        if (changes == 0) {
            LOG.warn("Неудачная попытка добавить дизлайк");
            throw new NotFoundException();
        }
        LOG.info("Данные отзыва изменены");
        String query2 = "insert into review_like(review_id, user_id, is_like) values (?,?, false)";
        jdbcTemplate.update(query2, id, userId);
        LOG.info("Поставлен дизлайк отзыву");
    }

    @Override
    public void deleteLike(int id, int userId) {
        Review review = findReviewById(id);
        review.setUseful(review.getUseful() - 1);
        String sqlQuery = "update reviews set useful=? where review_id = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                review.getUseful(),
                review.getReviewId());
        if (changes == 0) {
            LOG.warn("Неудачная попытка удалить лайк");
            throw new NotFoundException();
        }
        LOG.info("Данные отзыва изменены");
        String query = "delete review_like where review_id = ?, user_id = ?, is_like = ?";
        jdbcTemplate.update(query, id, userId, true);
        LOG.info("Удален лайк к отзыву");
    }

    @Override
    public void deleteDislike(int id, int userId) {
        Review review = findReviewById(id);
        review.setUseful(review.getUseful() + 1);
        String sqlQuery = "update reviews set useful=? where review_id = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                review.getUseful(),
                review.getReviewId());
        if (changes == 0) {
            LOG.warn("Неудачная попытка удалить лайк");
            throw new NotFoundException();
        }
        LOG.info("Данные отзыва изменены");
        String query = "delete review_like where review_id = ?, user_id = ?, is_like = ?";
        jdbcTemplate.update(query, id, userId, false);
        LOG.info("Удален лайк к отзыву");
    }
}
