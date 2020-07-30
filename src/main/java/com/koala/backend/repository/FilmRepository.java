package com.koala.backend.repository;

import com.koala.backend.domain.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface FilmRepository extends CrudRepository<Film, UUID> {

    @Query("select f.id from Film f")
    Stream<UUID> getIdsOfFilms();

}
