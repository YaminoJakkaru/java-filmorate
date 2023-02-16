package ru.yandex.practicum.filmorate.storage;

public interface ReviewStorageValidator {
    boolean reviewLikeValidate(int reviewId, int userId, boolean isLike);
}
