package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public FilmNotFoundException handle(final FilmNotFoundException e) {
        return new FilmNotFoundException();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserNotFoundException handle(final UserNotFoundException e) {
        return new UserNotFoundException();
    }
}
