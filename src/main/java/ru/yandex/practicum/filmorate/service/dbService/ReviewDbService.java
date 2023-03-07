package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EntityType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorageValidator;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;
import ru.yandex.practicum.filmorate.validator.ReviewValidator;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewDbService implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final EventStorage eventStorage;
    private final ReviewValidator reviewValidator;
    private final UserStorageValidator userStorageValidator;
    private final ReviewStorageValidator reviewStorageValidator;
    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    public ReviewDbService(ReviewStorage reviewStorage,
                           EventStorage eventStorage,
                           UserStorageValidator userStorageValidator,
                           ReviewStorageValidator reviewStorageValidator,
                           ReviewValidator reviewValidator) {
        this.reviewStorage = reviewStorage;
        this.eventStorage = eventStorage;
        this.userStorageValidator = userStorageValidator;
        this.reviewStorageValidator = reviewStorageValidator;
        this.reviewValidator = reviewValidator;
    }

    public Review createReview(Review review) {
        if (!reviewValidator.validate(review)) {
            LOG.warn("Валидация отзыва не пройдена");
            throw new ValidationException();
        }
        if (!reviewStorageValidator.checkUserFilmValidate(review.getUserId(), review.getFilmId())) {
            LOG.warn("Валидация отзыва не пройдена");
            throw new NotFoundException();
        }
        Review createdReview = reviewStorage.createReview(review);
        eventStorage.createEvent(createdReview.getUserId(), createdReview.getReviewId(),
                EntityType.REVIEW, EventType.REVIEW,Operation.ADD);
        return createdReview;
    }

    public Review changeReview(Review review) {
        if (!userStorageValidator.userIdValidate(review.getUserId())) {
            LOG.warn("Пользователь не найден");
            throw new ValidationException();
        }
        Review createdReview =reviewStorage.changeReview(review);
        eventStorage.createEvent(createdReview.getUserId(), createdReview.getReviewId(),
                EntityType.REVIEW, EventType.REVIEW,Operation.UPDATE);
        return createdReview;
    }

    public void breakReview(int id) {
        eventStorage.createEvent(reviewStorage.getReviewAuthorId(id), id,
                EntityType.REVIEW, EventType.REVIEW,Operation.REMOVE);

        reviewStorage.breakReview(id);
    }

    public Review findReviewById(int id) {
        return reviewStorage.findReviewById(id);
    }

    public List<Review> getReviews(Optional<Integer> filmId, int count) {
        return reviewStorage.getReviews(filmId, count);
    }

    public void addLike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        if (!reviewStorageValidator.reviewLikeValidate(id, userId, true)) {
            reviewStorage.addLike(id, userId);
            return;
        }
        LOG.warn("Пользователь уже поставил лайк этому отзыву");
    }

    public void addDislike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        if (!reviewStorageValidator.reviewLikeValidate(id, userId, false)) {
            reviewStorage.addDislike(id, userId);
            return;
        }
        LOG.warn("Пользователь уже поставил дизлайк этому отзыву");
    }

    public void deleteLike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        if (reviewStorageValidator.reviewLikeValidate(id, userId, true)) {
            reviewStorage.deleteLike(id, userId);
            return;
        }
        LOG.warn("Пользователь не ставил лайк этому отзыву");
    }

    public void deleteDislike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        if (reviewStorageValidator.reviewLikeValidate(id, userId, false)) {
            reviewStorage.deleteDislike(id, userId);
            return;
        }
        LOG.warn("Пользователь не ставил дизлайк этому отзыву");
    }
}
