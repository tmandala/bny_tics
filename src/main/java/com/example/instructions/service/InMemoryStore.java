package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryStore {
    private final Map<String, CanonicalTrade> map = new ConcurrentHashMap<>();
    public void put(CanonicalTrade c) { map.put(c.id(), c); }
    public CanonicalTrade get(String id) { return map.get(id); }
}

