package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Director {
    private int id;
    private String name;

    public Map<String, Object> toMap() {
        return Map.of("id", id,
                "name", name);
    }
}
