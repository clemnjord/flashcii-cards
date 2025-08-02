package com.clemnjord.flashcii.web.dto;

public class DeckDto {
    public record DeckResponse(String uuid, String name, String description) {
    }

    public record DeckRequest(String name, String description) {
    }
}
