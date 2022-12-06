package ru.yandex.practicum.filmorate.ValidatorTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;

class FilmValidatorTest {
    FilmValidator filmValidator = new FilmValidator();

    @Test
    void nameValidateTest() {
        Assertions.assertTrue(filmValidator.nameValidate("name"), "не верно проверяется имя");
        Assertions.assertFalse(filmValidator.nameValidate(" "), "не верно проверяется имя");
    }

    @Test
    void descriptionValidateTest() {
        Assertions.assertTrue(filmValidator.descriptionValidate("description"), "не верно проверяется описание");
        Assertions.assertFalse(filmValidator.descriptionValidate("g".repeat(201)), "не верно проверяется описание");
    }

    @Test
    void releaseDateValidateTest() {
        Assertions.assertTrue(filmValidator.releaseDateValidate(LocalDate.parse("1895-12-22")), "не верно проверяется дата релиза");
        Assertions.assertFalse(filmValidator.releaseDateValidate(LocalDate.parse("1895-12-20")), "не верно проверяется дата релиза");
    }

    @Test
    void durationValidateTest() {
        Assertions.assertTrue(filmValidator.durationValidate(1), "не верно проверяется продолжительность");
        Assertions.assertFalse(filmValidator.durationValidate(-1), "не верно проверяется продолжительность");
    }
}
