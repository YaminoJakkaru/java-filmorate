package ru.yandex.practicum.filmorate.service.dbService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreDbService implements GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreDbService(@Qualifier("GenreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Genre findGenreById(int id) {
        return genreStorage.findGenreById(id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }
}
