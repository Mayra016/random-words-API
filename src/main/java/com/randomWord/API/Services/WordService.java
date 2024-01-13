package com.randomWord.API.Services;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import java.util.logging.Level;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.randomWord.API.Entities.WordEntity;
import com.randomWord.API.Repositories.WordRepository;

@Service
public class WordService {
    @Autowired
    WordRepository repository;
        
    public WordService() {
    }    
    
    // CRUD
    
    //add new word
    public void addNewWord(String word) {
    	Optional<WordEntity> searchWord = repository.findByWordNameIgnoreCase(word);
        if (searchWord.isEmpty()){
            WordEntity newWord = new WordEntity(word);
            repository.save(newWord);
        }
    }
    
    // get all word entries
    public List<WordEntity> getWords() {
    	List<WordEntity> words = repository.findAll();
    	return words;
    }
    
    // search for a specific word
    public Optional<WordEntity> searchWord(String toSearchWord) {
        Optional<WordEntity> searchingWord = repository.findByWordNameIgnoreCase(toSearchWord);
        return searchingWord;
    }
    
    // search for words that begins with a specific letter
    public Optional<List<WordEntity>> beginsWith(String letter){
    	Optional<List<WordEntity>> wordsWithFirstLetter = repository.findByWordNameStartingWith(letter);
        return wordsWithFirstLetter;
    }
    
    // search for words that begins with a specific letter and has the specified length
    public Optional<List<WordEntity>> beginsWithAndLength(String letter, byte length){
    	double seed = Math.random();
    	Optional<List<WordEntity>> words = repository.findByWordNameStartingWithAndQuantity(letter, seed, length);
        return words;
    }
    
    // search for words that ends with a specific letter
    public Optional<List<WordEntity>> endsWith(String endLetter) {
    	Optional<List<WordEntity>> wordsWithLastLetter = repository.findByWordNameEndingWith(endLetter);
        return wordsWithLastLetter;
    }
    
    // search for words that ends with a specific letter and matches the specified length
    public Optional<List<WordEntity>> endsWithAndLength(String endLetter, byte length) {
    	double seed = Math.random();
    	Optional<List<WordEntity>> words = repository.findByWordNameEndingWithAndQuantity(endLetter, seed, length);
        return words;
    }
    
    // filter by length
    public Optional<List<WordEntity>> withLength(byte length) {
        return repository.findByWordLength(length);
    }
    
    // filter x words by a specific range
    public Optional<List<WordEntity>> filterXWordsWithLength(byte length, int range) {
    	return repository.findRandomWordsByRequisito(length, range);
    }
    // update word
    public void updateName(String name, String newName) {
        Optional<WordEntity> toUpdateWord = repository.findByWordNameIgnoreCase(name);
        if (toUpdateWord.isPresent()) {
        	WordEntity updateWord = toUpdateWord.get();
        	WordEntity word = toUpdateWord.get();
        	updateWord.setWordName(newName);
        	updateWord.setId(word.getId());
        	updateWord.setLastLetter();
        	updateWord.setFirstLetter();
        	updateWord.setWordLength(newName);
        	repository.save(updateWord);
        }       
    }
    
    // delete word
    public boolean deleteWord(String toDeleteWord) {
        Optional<WordEntity> toBeDeleted = repository.findByWordNameIgnoreCase(toDeleteWord);
        if (toBeDeleted.isPresent()) {
        	WordEntity deletedWord = toBeDeleted.get();
        	repository.delete(deletedWord);
        	return true;
        } else {
        	return false;
        }
        
    }
    
    // delete word
    public boolean deleteWordId(Long wordId) {
        Optional<WordEntity> toBeDeleted = repository.findById(wordId);
        if (toBeDeleted.isPresent()) {
        	WordEntity deletedWord = toBeDeleted.get();
        	repository.delete(deletedWord);
        	return true;
        } else {
        	return false;
        }
        
    }
    
   // It will check if the word has already an entry on the data basis
   // and in case it is not present, the word will be saved
    public void checkWord(String word) {
    	try {
    		WordEntity searchWord = repository.findByWordNameIgnoreCase(word).get(); 
    	}	
    	catch (Exception e) {	
    		WordEntity newWord = new WordEntity(word);
    		e.printStackTrace();
            try {            	
                repository.save(newWord);
            } 
            catch (NoSuchElementException p){
                repository.save(newWord);
                p.printStackTrace();
            }

    	}
    }
    
    // add new word from a document
    public void updateDataBase(String fileRoot) {
        try (FileInputStream fis = new FileInputStream(fileRoot)) {
            // Load document
            XWPFDocument document = new XWPFDocument(fis);
            
            String[] elements = {"!", "¡", ".", ",", "¿", "?", ":", "...", "(", ")"};
            String[] shortWords = {"y", "o", "la", "el", "él", "a", "tus", "ella", "los"};
;          
			// Get paragraphs
            for (XWPFParagraph paragraph : document.getParagraphs()) {
            	
                // For each paragraph it will break the words and check if they are already in the data base
                String paragraphText = extractText(paragraph);
                String[] words = paragraphText.split(" ");
                
                // Apply filter to save only relevant words
                for (String word : words) {     
                    for ( String character : elements) {
                    	if (word.contains(character)) {
                    		word = word.replace(character, "");       
                    	}
                    }
                    for ( String character : shortWords) {
                    	if (word.equalsIgnoreCase(character)) {
                    		word = "";       
                    	}
                    }
                    if (isNumeric(word)) {
                    	word = "";
                    }
                    
                    if (word.trim().isEmpty()) {
                    	
                    } else {
                    	checkWord(word);
                    }
                }
                
            }

            document.close();
        }   
        catch (Exception e) {
        	e.printStackTrace();
        }

    }

    // Extract text from a document
    private static String extractText(XWPFParagraph paragraph) {
        StringBuilder text = new StringBuilder();
        for (XWPFRun run : paragraph.getRuns()) {
            text.append(run.text());
        }
        return text.toString();
    }
    
    // Verify if the word is a number, in this case it will return true. Otherwise
    // it will return false
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    
    // Search and delete all entries that has the same word name as the parameter.
    public void deleteMultiples(String word) {
    	byte length = (byte) word.length();
    	List<WordEntity> toBeDeletedWords = repository.findByWordNameIgnoreCaseAndWordLength(word, length);
    	for (WordEntity toBeDeleted : toBeDeletedWords) {
    		repository.deleteById(toBeDeleted.getId());
    	}
    	WordEntity singleWord = new WordEntity(word);
	    repository.save(singleWord);
    }
}
