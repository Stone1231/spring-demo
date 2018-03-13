package com.demo.dao.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.demo.model.Message;

public interface MessageMongo extends MongoRepository<Message, String> {
    public Message findByType(String type);
    public List<Message> findByBody(String body);
}