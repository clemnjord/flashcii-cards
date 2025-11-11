package com.clemnjord.flashcii.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flashcards", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlashcardController {

}
