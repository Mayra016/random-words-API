package com.randomWord.API.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.randomWord.API.Entities.WordEntity;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Long> {
	Optional<WordEntity> findByWordNameIgnoreCase(String wordName);
	Optional<List<WordEntity>> findByWordNameStartingWith(String firstLetter);
	Optional<List<WordEntity>> findByWordNameEndingWith(String lastLetter);
	Optional<List<WordEntity>> findByWordLength(byte wordLength);
	List<WordEntity> findByWordNameIgnoreCaseAndWordLength(String word, byte length);
}
