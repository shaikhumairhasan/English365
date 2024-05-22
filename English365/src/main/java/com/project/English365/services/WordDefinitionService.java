package com.project.English365.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.English365.entities.WordDefinition;
import com.project.English365.repositories.WordDefinitionsRepository;

@Service
public class WordDefinitionService {

    private final WordDefinitionsRepository wordDefinitionsRepository;

    @Autowired
    public WordDefinitionService(WordDefinitionsRepository wordDefinitionsRepository) {
        this.wordDefinitionsRepository = wordDefinitionsRepository;
    }

    public WordDefinition saveWordDefinition(WordDefinition wordDefinition) {
        return wordDefinitionsRepository.save(wordDefinition);
    }

    public WordDefinition findWordDefinitionById(Long id) {
        return wordDefinitionsRepository.findById(id).orElse(null);
    }

    public WordDefinition updateWordDefinition(WordDefinition wordDefinition) {
        return wordDefinitionsRepository.save(wordDefinition);
    }

    public void deleteWordDefinitionById(Long id) {
        wordDefinitionsRepository.deleteById(id);
    }

    public List<WordDefinition> getAllWordDefinitions() {
        return wordDefinitionsRepository.findAll();
    }
}
