package com.project.English365.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.English365.entities.Quote;
import com.project.English365.entities.WordDefinition;

@Service
public class DailyVocabService {

//	public Quote fetchRandomQuote() {
//        try {
//            URL url = new URL("https://api.quotable.io/quotes/random");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("accept", "application/json");
//
//            try (InputStream responseStream = connection.getInputStream()) {
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode responseNode = mapper.readTree(responseStream);
//
//                if (responseNode.isArray() && responseNode.size() > 0) {
//                    JsonNode quoteNode = responseNode.get(0);
//                    String content = quoteNode.path("content").asText();
//                    String author = quoteNode.path("author").asText();
//
//                    Quote quote = new Quote();
//                    quote.setContent(content);
//                    quote.setAuthor(author);
//
//                    return quote;
//                } else {
//                	return null;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            } finally {
//                connection.disconnect();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
	
	
	public List<Quote> fetchRandomQuotes() {
	    List<Quote> quotes = new ArrayList<>();

	    try {
	        URL url = new URL("https://api.quotable.io/quotes/random?minLength=165&limit=3");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestProperty("accept", "application/json");

	        try (InputStream responseStream = connection.getInputStream()) {
	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode responseNode = mapper.readTree(responseStream);

	            if (responseNode.isArray()) {
	                for (JsonNode quoteNode : responseNode) {
	                    String content = quoteNode.path("content").asText();
	                    String author = quoteNode.path("author").asText();

	                    Quote quote = new Quote();
	                    quote.setContent(content);
	                    quote.setAuthor(author);
	                    quotes.add(quote);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            connection.disconnect();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return quotes;
	}
	
	
	
	public WordDefinition fetchRandomWord() {
        try {
            URL url = new URL("https://random-words-api.vercel.app/word");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");

            try (InputStream responseStream = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode wordNode = mapper.readTree(responseStream);
                
                if (wordNode.isArray() && wordNode.size() > 0) {
                    JsonNode firstWord = wordNode.get(0);
                    String word = firstWord.path("word").asText();
                    String definition = firstWord.path("definition").asText();

                    WordDefinition randomWord = new WordDefinition();
                    randomWord.setWord(word);
                    randomWord.setDefinition(definition);
                    
                    return randomWord;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
