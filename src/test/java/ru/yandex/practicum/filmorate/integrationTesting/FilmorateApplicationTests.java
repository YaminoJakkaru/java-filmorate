package ru.yandex.practicum.filmorate.integrationTesting;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DAO.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.DAO.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    User user1 = User.builder()
            .id(1)
            .email("em@ail1")
            .login("login1")
            .name("name1")
            .birthday(LocalDate.now().minusDays(1))
            .build();

    User user2 = User.builder()
            .id(2)
            .email("em@ail2")
            .login("login2")
            .name("name2")
            .birthday(LocalDate.now().minusDays(2))
            .build();
    User user3 = User.builder()
            .id(3)
            .email("em@ail3")
            .login("login3")
            .name("name3")
            .birthday(LocalDate.now().minusDays(3))
            .build();

    Film film1 = Film.builder()
            .id(1)
            .name("name1")
            .description("description1")
            .releaseDate(LocalDate.now().minusDays(1))
            .duration(101)
            .mpa(Mpa.builder().id(1).name("G").build())
            .build();
    Film film2 = Film.builder()
            .id(2)
            .name("name2")
            .description("description2")
            .releaseDate(LocalDate.now().minusDays(2))
            .duration(102)
            .mpa(Mpa.builder().id(2).name("PG").build())
            .build();
    Film film3 = Film.builder()
            .id(3)
            .name("name3")
            .description("description3")
            .releaseDate(LocalDate.now().minusDays(3))
            .duration(103)
            .mpa(Mpa.builder().id(3).name("PG-13").build())
            .build();


    @Test
    public void testFindUnknownUserById() {
        assertThatThrownBy(() -> userStorage.findUserById(5)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testGetAllUsers() {
        userStorage.createUser(user1);
        List<User> users = userStorage.getAllUsers();
        assertThat(users).isEqualTo(List.of(user1));
    }

    @Test
    public void testCreateUser() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.createUser(user2));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 2))
                .hasValueSatisfying(user -> assertThat(user.toMap()).isEqualTo(user2.toMap()));
    }

    @Test
    public void testChangeUnknownUser() {
        assertThatThrownBy(() -> userStorage.changeUser(user3)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testChangeUser() {
        user3.setId(2);
        Optional<User> userOptional = Optional.ofNullable(userStorage.changeUser(user3));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 2))
                .hasValueSatisfying(user -> assertThat(user.toMap()).isEqualTo(user3.toMap()));
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(user -> assertThat(user.toMap()).isEqualTo(user1.toMap()));
    }

    @Test
    public void testFindUnknownFilmById() {
        assertThatThrownBy(() -> filmStorage.findFilmById(5)).isInstanceOf(FilmNotFoundException.class);
    }

    @Test
    public void testGetAllFilms() {
        filmStorage.createFilm(film1);
        List<Film> films = filmStorage.getAllFilms();
        assertThat(films).isEqualTo(List.of(film1));
    }

    @Test
    public void testCreateFilm() {
        Optional<Film> userOptional = Optional.ofNullable(filmStorage.createFilm(film2));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 2))
                .hasValueSatisfying(film -> assertThat(film.toMap()).isEqualTo(film2.toMap()));
    }

    @Test
    public void testChangeUnknownFilm() {
        assertThatThrownBy(() -> filmStorage.changeFilm(film3)).isInstanceOf(FilmNotFoundException.class);
    }

    @Test
    public void testChangeFilm() {
        film3.setId(2);
        Optional<Film> userOptional = Optional.ofNullable(filmStorage.changeFilm(film3));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 2))
                .hasValueSatisfying(film -> assertThat(film.toMap()).isEqualTo(film3.toMap()));
    }

    @Test
    public void testFindFilmById() {

        Optional<Film> userOptional = Optional.ofNullable(filmStorage.findFilmById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(film -> assertThat(film.toMap()).isEqualTo(film1.toMap()));
    }
}