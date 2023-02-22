package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.rowMapper.ReviewMapper;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Component
public class ReviewDbStorage implements ReviewStorage {

    private static final String BASE_FIND_QUERY = "select r.*, " +
            "(count(rl.review_id) - count(rd.review_id)) AS useful " +
            "from reviews AS r " +
            "left join review_like AS rl on r.review_id = rl.review_id " +
            "left join review_dislike AS rd on r.review_id = rd.review_id ";

    private static final Logger LOG = LoggerFactory.getLogger(ReviewStorage.class);

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsertReview;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsertReview = new SimpleJdbcInsert(jdbcTemplate)
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
        String query = BASE_FIND_QUERY + "where r.review_id = " + id + " group by r.review_id";
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
            String query = BASE_FIND_QUERY + "where film_id = ? group by r.review_id " +
                           "order by useful DESC, review_id ASC fetch first ? rows only";
            return jdbcTemplate.query(query, new ReviewMapper(), filmId, count);
        }
        String query = BASE_FIND_QUERY + " group by r.review_id order by useful DESC, review_id ASC fetch first ? rows only";
        return jdbcTemplate.query(query, new ReviewMapper(), count);
    }

    @Override
    public void addLike(int id, int userId) {
        String query = "insert into review_like(review_id, user_id) values (?,?)";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Поставлен лайк отзыву");
    }

    @Override
    public void addDislike(int id, int userId) {
        String query2 = "insert into review_dislike(review_id, user_id) values (?, ?)";
        jdbcTemplate.update(query2, id, userId);
        LOG.info("Поставлен дизлайк отзыву");
    }

    @Override
    public void deleteLike(int id, int userId) {
        String query = "delete review_like where review_id = ?, user_id = ?";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Удален лайк к отзыву");
    }

    @Override
    public void deleteDislike(int id, int userId) {
        String query = "delete review_like where review_id = ?, user_id = ?";
        jdbcTemplate.update(query, id, userId);
        LOG.info("Удален лайк к отзыву");
    }

    @Override
    public int getReviewAuthorId(int id){
        String query = "select user_id from reviews where review_id =" + id;
        return jdbcTemplate.queryForObject(query,Integer.class);
    }
}
