package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class Review {

    private int reviewId;
    private String content;
    private Boolean isPositive;
    private int userId;
    private int filmId;
    private int useful;

    public Map<String, Object> toMap() {
        return Map.of("content", content,
                "is_positive", isPositive,
                "user_id", userId,
                "film_id", filmId,
                "useful", useful);
    }
}
