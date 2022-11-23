package ru.yandex.practicum.filmorate.ValidatorTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;

class UserValidatorTest {
    UserValidator userValidator = new UserValidator();

    @Test
    void emailValidateTest() {
        Assertions.assertTrue(userValidator.emailValidate("descri@ption"), "не верно проверяется почта");
        Assertions.assertFalse(userValidator.emailValidate("description"), "не верно проверяется почта");
        Assertions.assertFalse(userValidator.emailValidate(""), "не верно проверяется почта");
    }

    @Test
    void birthDateValidateTest() {
        Assertions.assertTrue(userValidator.birthDateValidate(LocalDate.now().minusDays(1)), "не верно проверяется дата рождения");
        Assertions.assertFalse(userValidator.birthDateValidate(LocalDate.now().plusDays(1)), "не верно проверяется дата рождения");
    }

    @Test
    void loginValidateTest() {
        Assertions.assertTrue(userValidator.loginValidate("login"), "не верно проверяется логин");
        Assertions.assertFalse(userValidator.loginValidate(" "), "не верно проверяется логин");
        Assertions.assertFalse(userValidator.loginValidate("lo gin"), "не верно проверяется логин");
    }
}
