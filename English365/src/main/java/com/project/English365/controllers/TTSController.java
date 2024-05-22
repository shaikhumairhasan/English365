package com.project.English365.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.English365.services.TTSService;

@Controller
public class TTSController {

	@Autowired
	TTSService ttsService;
	
	@PostMapping("/speak")
	public String speakText(@RequestParam("textToConvert") String textToConvert) {	
		ttsService.speakText(textToConvert);
		return "textToSpeech";
	}
}
