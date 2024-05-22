package com.project.English365.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.English365.entities.JokeResponse;
import com.project.English365.entities.Quiz;
import com.project.English365.entities.Quote;
import com.project.English365.entities.User;
import com.project.English365.entities.WordDefinition;
import com.project.English365.repositories.UserRepository;
import com.project.English365.repositories.WordDefinitionsRepository;
import com.project.English365.services.DailyVocabService;

@Controller
public class LoggedInUserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	WordDefinitionsRepository definitonRepository;

	@Autowired
	private DailyVocabService dailyVocabService;

	@GetMapping("/dashboard")
	public ModelAndView showDashboard(Authentication authentication) {
		String loggedInUsername = authentication.getName();
		User userDetails = userRepository.findByEmail(loggedInUsername);
		ModelAndView modelAndView = new ModelAndView("dashboard");
		modelAndView.addObject("userDetails", userDetails);
		return modelAndView;
	}

	@GetMapping("/dailyVocab")
	public String dailyVocab(Model model) {
		List<Quote> quotes = new ArrayList<>();
		quotes = dailyVocabService.fetchRandomQuotes();

		WordDefinition randomWord = dailyVocabService.fetchRandomWord();
		if (quotes != null) {
			model.addAttribute("quote1Content", quotes.get(0).getContent());
			model.addAttribute("quote1Author", quotes.get(0).getAuthor());

			model.addAttribute("quote2Content", quotes.get(1).getContent());
			model.addAttribute("quote2Author", quotes.get(1).getAuthor());

			model.addAttribute("quote3Content", quotes.get(2).getContent());
			model.addAttribute("quote3Author", quotes.get(2).getAuthor());
		} else {
			model.addAttribute("quote1Content", "No quotes found.");
			model.addAttribute("quote1Author", "NA");

			model.addAttribute("quote2Content", "No quotes found.");
			model.addAttribute("quote2Author", "NA");

			model.addAttribute("quote3Content", "No quotes found.");
			model.addAttribute("quote4Author", "NA");
		}
		if (randomWord != null) {
			model.addAttribute("word", randomWord.getWord());
			model.addAttribute("definition", randomWord.getDefinition());
		} else {
			model.addAttribute("word", "No words found.");
			model.addAttribute("definition", "");
		}
		return "dailyVocab";
	}

	@GetMapping("/chatroom")
	public String chatroom() {
		return "chatroom";
	}

	@GetMapping("/chat")
	public String chat(Model model, Authentication authentication) {
		String loggedInUsername = authentication.getName();
		User user = userRepository.findByEmail(loggedInUsername);
		model.addAttribute("username", user.getName());
		return "chat";
	}

	@GetMapping("/imageTranslate")
	public String imageTranslate() {
		return "imageTranslate";
	}

	@GetMapping("/tts")
	public String tts() {
		return "textToSpeech";
	}

	@GetMapping("/dictionary")
	public String dictionary() {
		return "dictionary";
	}

	@GetMapping("/profile")
	public String profilePage(Model model, Authentication authentication) {
		String loggedInUsername = authentication.getName();
		User user = userRepository.findByEmail(loggedInUsername);
		model.addAttribute("user", user);
		return "profile";
	}

	@GetMapping("/wordkeeper")
	public String wordKeeper(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    User user = userRepository.findByEmail(currentPrincipalName);
	    List<WordDefinition> wds = user.getWordDefinitions();
	    model.addAttribute("wordDefinitions", wds);
	    model.addAttribute("currentUser", currentPrincipalName);
	    return "wordKeeper";
	}
	
	@PostMapping("/saveWord")
	public String saveWord(@ModelAttribute WordDefinition wordDefinition, RedirectAttributes redirectAttributes, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    User user = userRepository.findByEmail(currentPrincipalName);
	    definitonRepository.save(wordDefinition);
	    List<WordDefinition> userWords= user.getWordDefinitions();
	    userWords.add(wordDefinition);
	    user.setWordDefinitions(userWords);
		userRepository.save(user);
	    redirectAttributes.addFlashAttribute("message", "Word added successfully!");
		return "redirect:/wordkeeper";
	}
	
	@GetMapping("/deleteword/{id}")
    public String deleteWord(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    User user = userRepository.findByEmail(currentPrincipalName);
        user.getWordDefinitions().removeIf(wordDefinition -> wordDefinition.getId().equals(id));
        userRepository.save(user);
    	definitonRepository.deleteById(id);
	    redirectAttributes.addFlashAttribute("messageRed", "Word deleted successfully!");
        return "redirect:/wordkeeper";
    }

	@GetMapping("/quicktrivia")
	public String quickTrivia() {
		return "quicktrivia";
	}

	@GetMapping("/starttrivia")
	public String startTrivia(@RequestParam String difficulty, @RequestParam String category, Model model) {
		String apiUrl = "https://the-trivia-api.com/v2/questions?difficulties=" + difficulty + "&categories="
				+ category;
		RestTemplate restTemplate = new RestTemplate();
		List<Quiz> questions = restTemplate
				.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Quiz>>() {
				}).getBody();
		model.addAttribute("quizList", questions);
		return "startTrivia";
	}

	@GetMapping("/humourhub")
	public ModelAndView humourHub() {
		ModelAndView modelAndView = new ModelAndView();

		String apiUrl = "https://v2.jokeapi.dev/joke/Programming,Pun?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=twopart";

		RestTemplate restTemplate = new RestTemplate();

		JokeResponse jokeResponse = restTemplate.getForObject(apiUrl, JokeResponse.class);

		String setup = jokeResponse.getSetup();
		String delivery = jokeResponse.getDelivery();

		modelAndView.addObject("setup", setup);
		modelAndView.addObject("delivery", delivery);
		modelAndView.setViewName("humourHub");

		return modelAndView;
	}

}
