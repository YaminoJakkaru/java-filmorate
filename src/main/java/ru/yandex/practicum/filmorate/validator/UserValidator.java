package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {


    public boolean validate(User user){
        return emailValidate(user.getEmail())&&
                birthDateValidate(user.getBirthday())&&loginValidate(user.getLogin());
    }

    public  boolean emailValidate(String email){
        return email.contains("@");
    }

    public  boolean birthDateValidate(LocalDate birthday){
        return birthday.isBefore(LocalDate.now());
    }

    public boolean loginValidate(String login){
        return !login.isBlank()&&!login.contains(" ");
    }
}
