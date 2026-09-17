package com.adventure.book.repository;

import com.adventure.book.model.PlaySession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PlaySessionRepository {

    private final Map<String, PlaySession> playSessions;

    public PlaySessionRepository() {
        this.playSessions = new HashMap<>();
    }

    public void save(PlaySession playSession) {
        playSessions.put(playSession.getId(), playSession);
    }

    public PlaySession findByPlayId(String playId) {
        return playSessions.get(playId);
    }

    public PlaySession findByPlayerId(String playerId) {
        return playSessions.get(playerId);
    }
}
