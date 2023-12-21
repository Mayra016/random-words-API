package com.randomWord.API.Services;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.util.logging.Logger;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import java.util.logging.Level;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    
    // search for words that ends with a specific letter
    public Optional<List<WordEntity>> endsWith(String endLetter) {
    	Optional<List<WordEntity>> wordsWithLastLetter = repository.findByWordNameEndingWith(endLetter);
        return wordsWithLastLetter;
    }
    
    // filter by length
    public Optional<List<WordEntity>> withLength(byte length) {
        return repository.findByWordLength(length);
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
    
    public void checkWord(String word) {
    	try {
    		WordEntity searchWord = repository.findByWordNameIgnoreCase(word).get(); 
    		//WordEntity hola2 = new WordEntity("Iterando sobre párafos");
    	}	
    	catch (Exception e) {	
            WordEntity newWord = new WordEntity(word);
            repository.save(newWord);

    	}
    }
    
    // add new word from a document
    public void updateDataBase(String fileRoot) {
        try (FileInputStream fis = new FileInputStream(fileRoot)) {
            // Load document
            XWPFDocument document = new XWPFDocument(fis);
            String[] elements = {"!", "¡", ".", ",", "¿", "?"};
            String[] shortWords = {"y", "o", "la", "el", "él", "a", "tus", "ella", "los"};
;            // Get paragraphs
            //WordEntity hola = new WordEntity("Se ha ejecutado");
            //repository.save(hola);
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                // For each paragraph it will break the words and check if they are already in the data base
                String paragraphText = extractText(paragraph);
                //LOGGER.info("Extracted Text: " + paragraphText);
                //WordEntity hola2 = new WordEntity("Iterando sobre párafos");
                //repository.save(hola2);
                String[] words = paragraphText.split(" ");
                for (String word : words) {     
                	//WordEntity hola3 = new WordEntity("Palabras");
                    //repository.save(hola3);
                    for ( String character : elements) {
                    	if (word.contains(character)) {
                    		word = word.replaceAll(character, "");       
                    	}
                    }
                    for ( String character : shortWords) {
                    	if (word.equalsIgnoreCase(character)) {
                    		word = "";       
                    	}
                    }
                    if (word.trim().isEmpty()) {
                    	
                    } else {
                    	checkWord(word);
                    }
                }
                
            }

            document.close();
        }   catch (IOException e) {
 
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
