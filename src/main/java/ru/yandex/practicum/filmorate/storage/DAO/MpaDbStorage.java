package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.rowMapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Component
@Qualifier("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {

    private static final Logger LOG = LoggerFactory.getLogger(MpaStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findMpaById(int id) {
        String query = "select * from mpa where mpa_id=" + id;
        List<Mpa> mpa = jdbcTemplate.query(query, new MpaMapper());
        if (mpa.isEmpty()) {
            LOG.warn("Попытка  получить несуществующий рейтинг");
            throw new NotFoundException();
        }
        return mpa.get(0);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String query = "select * from mpa";
        return jdbcTemplate.query(query, new MpaMapper());
    }
}
