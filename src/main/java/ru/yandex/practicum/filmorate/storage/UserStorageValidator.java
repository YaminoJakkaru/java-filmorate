package ru.yandex.practicum.filmorate.storage;

public interface UserStorageValidator {

    boolean usersIdValidate(int id, int otherId);

    boolean userIdValidate(int id);
}
