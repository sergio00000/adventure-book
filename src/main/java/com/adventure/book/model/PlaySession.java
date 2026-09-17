package com.adventure.book.model;

import com.adventure.book.model.enums.Status;

public class PlaySession {
    private String id;
    private String playerId;
    private String bookId;
    private int currentSectionId;
    private int health;
    Status status;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getCurrentSectionId() {
        return currentSectionId;
    }

    public void setCurrentSectionId(int currentSectionId) {
        this.currentSectionId = currentSectionId;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
