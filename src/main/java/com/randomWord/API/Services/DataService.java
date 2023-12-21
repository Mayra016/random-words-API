package com.randomWord.API.Services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.randomWord.API", "com.randomWord.API.Services.WordService"})

public class DataService {
	
    @Autowired
    private WordService wordService;  // Inyecta el servicio

    @Value("${document.file.path}")
    private String documentFilePath;  // Inyecta la ruta del documento desde la configuración

    public DataService(String filePath) {
    	this.documentFilePath = filePath;
    }

    @PostConstruct
    public void init() {
        // Llama a updateDataBase utilizando la instancia inyectada de WordService
        wordService.updateDataBase(documentFilePath);
    }
}