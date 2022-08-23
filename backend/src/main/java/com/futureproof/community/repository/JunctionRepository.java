package com.futureproof.community.repository;

import com.futureproof.community.models.Junction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JunctionRepository extends JpaRepository<Junction, Long> {
    List<Junction> findAllByAnimalId(long animalId);

    List<Junction> findAllByCountryId(long countryId);

    List<Junction> findAllByAnimalIdAndCountryId(long animalId, long countryId);

    @Modifying
    @Transactional
    @Query(value = "delete from Junction junction where junction.animalId = :animalId and junction.countryId = :countryId")
    void deleteByAnimalIdAndCountryId(@Param("animalId") long animalId, @Param("countryId") long countryId);
}
