package ru.yandex.practicum.filmorate.storage;

public interface FilmStorageValidator {

    boolean filmLikeValidate(int filmId, int userId);
}
