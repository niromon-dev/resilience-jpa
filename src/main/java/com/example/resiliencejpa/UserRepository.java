package com.example.resiliencejpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
            nativeQuery = true,
            value = "WITH sleep AS (select pg_sleep(:delay)) select 1 from sleep")
    int sleep(@Param("delay") int delay);

    @Query(
            nativeQuery = true,
            value = "select error from error")
    int sqlError();

}