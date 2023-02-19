merge into genre (genre_id, name)
values (1, 'Комедия');
merge into genre (genre_id, name)
values (2, 'Драма');
merge into genre (genre_id, name)
values (3, 'Мультфильм');
merge into genre (genre_id, name)
values (4, 'Триллер');
merge into genre (genre_id, name)
values (5, 'Документальный');
merge into genre (genre_id, name)
values (6, 'Боевик');
merge into mpa (mpa_id, name)
values (1, 'G');
merge into mpa (mpa_id, name)
values (2, 'PG');
merge into mpa (mpa_id, name)
values (3, 'PG-13');
merge into mpa (mpa_id, name)
values (4, 'R');
merge into mpa (mpa_id, name)
values (5, 'NC-17');
merge into event_type (event_type_id, name)
    values (1, 'LIKE');
merge into event_type (event_type_id, name)
    values (2, 'REVIEW');
merge into event_type (event_type_id, name)
    values (3, 'FRIEND');
merge into operation (operation_id, name)
    values (1, 'REMOVE');
merge into operation (operation_id, name)
    values (2, 'ADD');
merge into operation (operation_id, name)
        values (3, 'UPDATE');
