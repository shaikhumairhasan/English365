package com.project.English365.services;

import org.springframework.stereotype.Service;

import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

@Service
public class TTSService {

    private Synthesizer synthesizer;

    @PostConstruct
    public void initialize() {
        try {
            if (synthesizer == null) {
                // Set property as Kevin Dictionary (only set once during initialization)
                System.setProperty(
                    "freetts.voices",
                    "com.sun.speech.freetts.en.us"
                        + ".cmu_us_kal.KevinVoiceDirectory");

                // Register Engine (only register once during initialization)
                Central.registerEngineCentral(
                    "com.sun.speech.freetts"
                    + ".jsapi.FreeTTSEngineCentral");

                // Create a Synthesizer (only create once during initialization)
                synthesizer = Central.createSynthesizer(
                    new SynthesizerModeDesc(Locale.US));

                // Allocate synthesizer (only allocate once during initialization)
                synthesizer.allocate();

                // Resume Synthesizer (only resume once during initialization)
                synthesizer.resume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void speakText(String text) {
        try {
            // Speaks the given text until the queue is empty.
            synthesizer.speakPlainText(text, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        if (synthesizer != null) {
            try {
                synthesizer.deallocate();
            } catch (EngineException e) {
                e.printStackTrace();
            } catch (EngineStateError e) {
                e.printStackTrace();
            }
        }
    }
}
