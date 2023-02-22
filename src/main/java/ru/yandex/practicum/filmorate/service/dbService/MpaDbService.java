package ru.yandex.practicum.filmorate.service.dbService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaDbService implements MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaDbService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Mpa findMpaById(int id) {
        return mpaStorage.findMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
