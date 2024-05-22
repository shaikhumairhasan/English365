package com.project.English365.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.project.English365.entities.WordDefinition;
import com.project.English365.services.DictionaryService;

@Controller
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/fetch-definition")
    public String fetchDefinition(@RequestParam String word, Model model) {
        WordDefinition wordDefinition = dictionaryService.fetchWordDefinition(word);
        if (wordDefinition != null) {
            System.out.println(wordDefinition);
            model.addAttribute("word", word);
            model.addAttribute("definition", wordDefinition.getDefinition());
            return "dictionary";
        } else {
        	model.addAttribute("wordNotFound", true);
            return "dictionary";
        }
    }

}
