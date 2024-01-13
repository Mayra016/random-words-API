package com.randomWord.API.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.randomWord.API.Entities.WordEntity;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Long> {
	Optional<WordEntity> findByWordNameIgnoreCase(String wordName);
	Optional<List<WordEntity>> findByWordNameStartingWith(String firstLetter);
	Optional<List<WordEntity>> findByWordNameEndingWith(String lastLetter);
	Optional<List<WordEntity>> findByWordLength(byte wordLength);
	List<WordEntity> findByWordNameIgnoreCaseAndWordLength(String word, byte length);
	
	@Query(value = "SELECT * FROM words WHERE length = :requisito ORDER BY RAND() LIMIT :range", nativeQuery = true)
	Optional<List<WordEntity>> findRandomWordsByRequisito(@Param("requisito") byte requisito, @Param("range") int range);

	@Query(value = "SELECT * FROM words WHERE first_Letter = :firstLetter ORDER BY RAND(:seed) LIMIT :range", nativeQuery = true)
	Optional<List<WordEntity>> findByWordNameStartingWithAndQuantity(@Param("firstLetter") String firstLetter, @Param("seed") double seed, @Param("range") byte range);

	@Query(value = "SELECT * FROM words WHERE last_Letter = :lastLetter ORDER BY RAND(:seed) LIMIT :range", nativeQuery = true)
	Optional<List<WordEntity>> findByWordNameEndingWithAndQuantity(@Param("lastLetter") String lastLetter, @Param("seed") double seed, @Param("range") byte range);

}
