package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.reader.Reader;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class SupportiveDbStorage {
    private static final Logger log = LoggerFactory.getLogger(SupportiveDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final Reader reader;

    public SupportiveDbStorage(JdbcTemplate jdbcTemplate, Reader reader) {
        this.jdbcTemplate = jdbcTemplate;
        this.reader = reader;
    }

    public List<Genre> getAllGenres(){
        String query="select * from genre";

        return  jdbcTemplate.query(query,reader::readGenre);
    }

    public Genre getGenre(int id){
        try{
            String query="select * from genre where genre_id=" + id;
        return  jdbcTemplate.query(query,reader::readGenre).get(0);
        }catch (IndexOutOfBoundsException e){
            log.warn("Попытка  получить несуществующий жанр");
            throw new NotFoundException();
        }
    }

    public List<Mpa> getAllMpa(){
        String query="select * from mpa";
        return  jdbcTemplate.query(query, reader::readMpa);
    }

    public Mpa getMpa(int id){
        try {
            String query="select * from mpa where mpa_id=" + id;
            return jdbcTemplate.query(query,
                    reader::readMpa).get(0);
        }catch (IndexOutOfBoundsException e){
            log.warn("Попытка  получить несуществующий рейтинг");
            throw new NotFoundException();
        }
    }
}
