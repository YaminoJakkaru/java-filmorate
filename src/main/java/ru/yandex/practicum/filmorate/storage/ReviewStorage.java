package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review createReview(Review review);

    Review changeReview(Review review);

    void breakReview(int id);

    Review findReviewById(int id);

    List<Review> getReviews(Optional<Integer> filmId, int count);

    void addLike(int id, int userId);

    void addDislike(int id, int userId);

    void deleteLike(int id, int userId);

    void deleteDislike(int id, int userId);

    int getReviewAuthorId (int id);
}
