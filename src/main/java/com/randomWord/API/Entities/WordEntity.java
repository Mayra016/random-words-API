package com.randomWord.API.Entities;


import javax.persistence.Id;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name = "words")
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="name")
    private String wordName;
    @Column(name="length")
    private byte wordLength;
    @Column
    private char firstLetter;
    @Column
    private char lastLetter;
    
    public WordEntity() {
    }

    
    public WordEntity(String name) {
        this.wordName = name;
        setWordLength(name);
        setFirstLetter();
        setLastLetter();   
    }
    
    public void setId(Long newId) {
    	this.id = newId;
    }
    
    public Long getId() {
    	return this.id;
    }
    
    public void setWordName(String word) {
    	this.wordName = word;
    }
    
    public String getWordName() {
    	return this.wordName;
    }
    
    public void setWordLength(String word) {
        this.wordLength = (byte) word.length();
    }
    
    public byte getWordLength() {
        return this.wordLength;
    }
    
    public void setFirstLetter() {
        this.firstLetter = this.wordName.charAt(0);
    }
    
    public char getFirstLetter() {
        return this.firstLetter;
    }
    
    public void setLastLetter() {
        this.lastLetter = this.wordName.charAt(this.wordName.length() - 1);
    }
    
    public char getLastLetter() {
        return this.lastLetter;
    }
    
    @JsonIgnore
    public JSONObject getWordInfo() {
    	JSONObject response = new JSONObject();
    	if (this.wordName != null){
	        response.put("name", this.wordName);
	        response.put("length", this.wordLength);
	        response.put("firstLetter", this.firstLetter);
	        response.put("lastLetter", this.lastLetter);
	        return response;
        } else {
        	response.put("error", "Word not found!");  
        	return response;
        }	        
    }
}