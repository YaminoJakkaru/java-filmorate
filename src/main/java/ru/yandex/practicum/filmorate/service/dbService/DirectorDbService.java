package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
public class DirectorDbService implements DirectorService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorDbService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    @Override
    public Director createDirector(Director director) {
        if (director.getName().isEmpty() || director.getName().isBlank()) {
            LOG.warn("Валидация режиссера не пройдена");
            throw new ValidationException();
        }
        return directorStorage.createDirector(director);
    }

    @Override
    public Director findDirectorById(int id) {
        return directorStorage.findDirectorById(id);
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    @Override
    public Director changeDirector(Director director) {
        if (director.getName().isEmpty() || director.getName().isBlank()) {
            LOG.warn("Валидация режиссера не пройдена");
            throw new ValidationException();
        }
        return directorStorage.changeDirector(director);
    }

    @Override
    public void deleteDirector(int id) {
        directorStorage.deleteDirector(id);
    }
}
