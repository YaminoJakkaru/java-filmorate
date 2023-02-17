package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorageValidator;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;
import ru.yandex.practicum.filmorate.validator.ReviewValidator;

import java.util.List;

@Service
@Qualifier("ReviewDbService")
public class ReviewDbService implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final ReviewValidator reviewValidator;
    private final UserStorageValidator userStorageValidator;
    private final ReviewStorageValidator reviewStorageValidator;
    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    public ReviewDbService(@Qualifier("ReviewDbStorage") ReviewStorage reviewStorage,
                           @Qualifier("UserDbStorageValidator") UserStorageValidator userStorageValidator,
                           @Qualifier("ReviewDbStorageValidator") ReviewStorageValidator reviewStorageValidator,
                           ReviewValidator reviewValidator) {
        this.reviewStorage = reviewStorage;
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
        return reviewStorage.createReview(review);
    }

    public Review changeReview(Review review) {
        if (!userStorageValidator.userIdValidate(review.getUserId())) {
            LOG.warn("Пользователь не найден");
            throw new ValidationException();
        }
        return reviewStorage.changeReview(review);
    }

    public void breakReview(int id) {
        reviewStorage.breakReview(id);
    }

    public Review findReviewById(int id) {
        return reviewStorage.findReviewById(id);
    }

    public List<Review> getReviews(int filmId, int count) {
        return reviewStorage.getReviews(filmId, count);
    }

    public void addLike(int id, int userId) {
        if (reviewStorageValidator.checkUserAndLike(id, userId, true) == 1) {
            reviewStorage.addLike(id, userId);
        } else {
            LOG.warn("Проверка пользователя и лайка не пройдена");
        }
    }

    public void addDislike(int id, int userId) {
        if (reviewStorageValidator.checkUserAndLike(id, userId, false) == 1) {
            reviewStorage.addDislike(id, userId);
        } else {
            LOG.warn("Проверка пользователя и лайка не пройдена");
        }
    }

    public void deleteLike(int id, int userId) {
        if (reviewStorageValidator.checkUserAndLike(id, userId, true) == 2) {
            reviewStorage.deleteLike(id, userId);
        } else {
            LOG.warn("Проверка пользователя и лайка не пройдена");
        }
    }

    public void deleteDislike(int id, int userId) {
        if (reviewStorageValidator.checkUserAndLike(id, userId, false) == 2) {
            reviewStorage.deleteDislike(id, userId);
        } else {
            LOG.warn("Проверка пользователя и лайка не пройдена");
        }
    }
}
