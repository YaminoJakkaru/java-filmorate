
CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id int PRIMARY KEY,
    name   varchar(40)     NOT NULL,
    CONSTRAINT mpa_pk PRIMARY KEY (mpa_id)

);
CREATE TABLE IF NOT EXISTS film
(
    film_id      int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         varchar(40)                          NOT NULL,
    description  varchar  (1000)                      NOT NULL,
    release_date date                                 NOT NULL,
    duration     int                                  NOT NULL,
    mpa_id       int                                  NOT NULL,
    CONSTRAINT film_pk PRIMARY KEY (film_id),
    CONSTRAINT mpa_fk FOREIGN KEY (mpa_id) REFERENCES mpa

);



CREATE TABLE IF NOT EXISTS genre
(
    genre_id int  PRIMARY KEY,
    name     varchar(40)      NOT NULL,
    CONSTRAINT genre_pk PRIMARY KEY (genre_id)

);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_genre_id int  as concat(film_id,genre_id) PRIMARY KEY,
    film_id  int NOT NULL,
    genre_id int NOT NULL,
    CONSTRAINT film_genre_id_pk PRIMARY KEY (film_genre_id),
    CONSTRAINT film_genre_film_fk FOREIGN KEY (film_id) REFERENCES film ON DELETE CASCADE,
    CONSTRAINT film_genre_genre_fk FOREIGN KEY (genre_id) REFERENCES genre
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login    varchar(40)                              NOT NULL,
    name     varchar (40)                             NOT NULL,
    email    varchar (40)                             NOT NULL,
    birthday date                                     NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_likes_id int as concat(film_id,user_id) PRIMARY KEY,
    film_id int NOT NULL,
    user_id int NOT NULL,
    CONSTRAINT film_likes_id_pk PRIMARY KEY (film_likes_id),
    CONSTRAINT film_like_film_fk FOREIGN KEY (film_id) REFERENCES film ON DELETE CASCADE,
    CONSTRAINT film_like_user_fk FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_friend
(
    user_friend_id int as concat(user_id,friend_id)PRIMARY KEY,
    user_id   int NOT NULL,
    friend_id int NOT NULL,
    CONSTRAINT user_friend_pk PRIMARY KEY (user_friend_id),
    CONSTRAINT user_friend_user_fk FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
    CONSTRAINT user_friend_friend_fk FOREIGN KEY (friend_id) REFERENCES users ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS reviews
(
    review_id      int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content        varchar(1000)                     NOT NULL,
    is_positive    boolean                           ,
    user_id        int                               NOT NULL,
    film_id        int                               NOT NULL,
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users,
    CONSTRAINT film_fk FOREIGN KEY (film_id) REFERENCES film
);

CREATE TABLE IF NOT EXISTS review_like
(
    review_like_id int AS concat(review_id,user_id) PRIMARY KEY,
    review_id      int                               NOT NULL,
    user_id        int                               NOT NULL,
    CONSTRAINT review_like_fk FOREIGN KEY (review_id) REFERENCES reviews ON DELETE CASCADE,
    CONSTRAINT user_review_like_fk FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS review_dislike
(
    review_dislike_id int AS concat(review_id,user_id) PRIMARY KEY,
    review_id      int                               NOT NULL,
    user_id        int                               NOT NULL,
    CONSTRAINT review_dislike_fk FOREIGN KEY (review_id) REFERENCES reviews ON DELETE CASCADE,
    CONSTRAINT user_review_dislike_fk FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS director
(
    director_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name   varchar(40)     NOT NULL,
    CONSTRAINT director_pk PRIMARY KEY (director_id)
);

CREATE TABLE IF NOT EXISTS film_director
(
    film_director_id int  as concat(film_id,director_id) PRIMARY KEY,
    film_id  int NOT NULL,
    director_id int NOT NULL,
    CONSTRAINT film_director_id_pk PRIMARY KEY (film_director_id),
    CONSTRAINT film_director_film_fk FOREIGN KEY (film_id) REFERENCES film ON DELETE CASCADE,
    CONSTRAINT film_director_director_fk FOREIGN KEY (director_id) REFERENCES director ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_type
(
    event_type_id int  PRIMARY KEY,
    name   varchar(40)     NOT NULL,
    CONSTRAINT event_type_pk PRIMARY KEY (event_type_id)
);

CREATE TABLE IF NOT EXISTS operation
(
    operation_id int  PRIMARY KEY,
    name   varchar(40)     NOT NULL,
    CONSTRAINT operation_pk PRIMARY KEY (operation_id)
);

CREATE TABLE IF NOT EXISTS event
(
    event_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id int NOT NULL,
    entity_id int NOT NULL,
    event_type_id int NOT NULL,
    operation_id int NOT NULL,
    timestamp timestamp NOT NULL,
    CONSTRAINT event_pk PRIMARY KEY (event_id),
    CONSTRAINT event_user_fk FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
    CONSTRAINT event_type_fk FOREIGN KEY (event_type_id) REFERENCES event_type ON DELETE CASCADE,
    CONSTRAINT operation_fk FOREIGN KEY (operation_id) REFERENCES operation ON DELETE CASCADE
);

