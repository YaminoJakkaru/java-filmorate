package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class UserValidator {


    public boolean validate(User user) {
        return emailValidate(user.getEmail()) &&
                birthDateValidate(user.getBirthday()) && loginValidate(user.getLogin());
    }

    public boolean emailValidate(String email) {
        return email != null && email.contains("@");
    }

    public boolean birthDateValidate(LocalDate birthday) {
        return birthday != null && birthday.isBefore(LocalDate.now());
    }

    public boolean loginValidate(String login) {
        return login != null && !login.isBlank() && !login.contains(" ");
    }
}
