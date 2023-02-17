package ru.yandex.practicum.filmorate.storage;

public interface ReviewStorageValidator {

    boolean checkUserFilmValidate(int userId, int film_id);

    int checkUserAndLike(int id, int userId, boolean isLike);
}
