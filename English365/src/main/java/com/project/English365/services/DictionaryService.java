package com.project.English365.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.English365.entities.WordDefinition;

@Service
public class DictionaryService {

	public WordDefinition fetchWordDefinition(String text) {
        try {
            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + text);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");

            try (InputStream responseStream = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode wordNode = mapper.readTree(responseStream);

                if (wordNode.isArray() && wordNode.size() > 0) {
                    JsonNode firstEntry = wordNode.get(0);
                    String word = firstEntry.path("word").asText();
                    
                    // The 'definitions' field is an array, so you need to iterate through it
                    JsonNode definitionsNode = firstEntry.path("meanings");
                    StringBuilder definitionBuilder = new StringBuilder();
                    for (JsonNode definitionNode : definitionsNode) {
                        String partOfSpeech = definitionNode.path("partOfSpeech").asText();
                        String definition = definitionNode.path("definitions").get(0).path("definition").asText();
                        definitionBuilder.append(partOfSpeech).append(": ").append(definition).append("\n");
                    }

                    WordDefinition wordDefinition = new WordDefinition();
                    wordDefinition.setWord(word);
                    wordDefinition.setDefinition(definitionBuilder.toString());

                    return wordDefinition;
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            return null;
        }
    }

}
