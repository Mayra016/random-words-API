package com.randomWord.API.Controller;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.randomWord.API.Entities.WordEntity;
import com.randomWord.API.Services.WordService;


@RestController
public class WordController {
    @Autowired
    WordService service;
    
    ResourceBundle resourceBundle = ResourceBundle.getBundle("text");
   
    
    @GetMapping("/list")
    public List<WordEntity> list() {
    	return service.getWords();
    }
    
    @GetMapping("/add/{newWord}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addNewWord(@PathVariable String newWord) {
    	String message;
    	try {
    		service.addNewWord(newWord);
    		message = "La palabra " + newWord + " fue añadida.";
    	} catch(Exception e) {
    		message = "Hubo un problema al añadir la palabra " + newWord;
    	}
        return message;
    }
    
    @GetMapping("/search/{word}")
    public ResponseEntity<WordEntity> searchSpecificWord(@PathVariable String word) {
        Optional<WordEntity> searchingWord = service.searchWord(word);
        return searchingWord.map(entity -> ResponseEntity.ok().body(entity))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/beginsWith/{firstLetter}")
    public Optional<List<WordEntity>> beginsWith(@PathVariable String firstLetter) {
        return service.beginsWith(firstLetter);
    }
    
    @GetMapping("/beginsWith/{firstLetter}/range/{range}")
    public Optional<List<WordEntity>> beginsWithLength(@PathVariable String firstLetter, @PathVariable byte range) {
        return service.beginsWithAndLength(firstLetter, range);
    }
    
    @GetMapping("/endsWith/{lastLetter}")
    public Optional<List<WordEntity>> endsWithLetter(@PathVariable String lastLetter) {
        return service.endsWith(lastLetter);
    }
    
    @GetMapping("/endsWith/{lastLetter}/range/{range}")
    public Optional<List<WordEntity>> endsWithLetterLength(@PathVariable String lastLetter, @PathVariable byte range) {
        return service.endsWithAndLength(lastLetter, range);
    }
    
    @GetMapping("/length/{length}")
    public Optional<List<WordEntity>> withLength(@PathVariable byte length) {   
        return service.withLength(length);
    }
    
    @GetMapping("/length/{length}/range/{range}")
    public Optional<List<WordEntity>> withLength(@PathVariable byte length, @PathVariable int range) {   
        return service.filterXWordsWithLength(length, range);
    }

    @GetMapping("/change/{wordToChange}/{newName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateWord(@PathVariable String wordToChange, @PathVariable String newName) {
        try {
        	service.updateName(wordToChange, newName);
        	return "La palabra " + wordToChange + " tuvo su nombre actualizado para " + newName;
        } catch (Exception e) {
        	return "Hubo un problema al alterar la palabra" + wordToChange + "a " + newName;
        }
    }    
    
    @GetMapping("/delete/{word}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteSpecificWord(@PathVariable String word) {
        boolean ok = service.deleteWord(word);
        
        if(ok) {
        	return "La palabra " + word + " fue deleteada exitosamente";
        } else {
        	return "Hubo un problema al deletear la palabra " + word;
        }
    }
    
    @GetMapping("/deleteId/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteId(@PathVariable Long id) {
        boolean ok = service.deleteWordId(id);
        
        if(ok) {
        	return "La palabra con el id" + id + " fue deleteada exitosamente";
        } else {
        	return "Hubo un problema al deletear la palabra con el " + id;
        }
    }
    
    @GetMapping("/deleteMulti/{word}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteMulti(@PathVariable String word) {
        service.deleteMultiples(word);
    }
    
    @GetMapping("/update/{secret_pass}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateDataBase(@PathVariable String secret_pass) {
    	String code = resourceBundle.getString("PASS");
    	String pass = secret_pass;
    	if (secret_pass.equals(code)) {
	    	try {
	    		String filepath = "/home/mayra/Documentos/Teste-agujero.docx";
			    service.updateDataBase(filepath);
			    return "El update fue concluído";  		
	    	} catch (Exception e) {
	    		return "Hubo un problema y el proceso no se pudo concluir";
	    	}
    	} else {
    		return "No tienes permiso";
    	}
    }
}
