package ru.yandex.practicum.filmorate.service.dbService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@Qualifier("DirectorDbService")
public class DirectorDbService implements DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorDbService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    @Override
    public Director createDirector(Director director) {
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
        return directorStorage.changeDirector(director);
    }

    @Override
    public void deleteDirector(int id) {
        directorStorage.deleteDirector(id);
    }
}
