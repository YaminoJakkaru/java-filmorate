package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;

@Service
public class ReviewValidator {

    public boolean validate(Review review) {
        return contentValidate(review.getContent()) && filmIdValidate(review.getFilmId()) &&
                userIdValidate(review.getUserId()) && isPositiveIsValidated(review.getIsPositive());
    }

    public boolean contentValidate(String name) {
        return name != null && !name.isBlank();
    }

    public boolean filmIdValidate(int filmId) {
        return filmId != 0;
    }

    public boolean userIdValidate(int userId) {
        return userId != 0;
    }

    public boolean isPositiveIsValidated(Boolean isPositive) {
        return isPositive != null;
    }
}
