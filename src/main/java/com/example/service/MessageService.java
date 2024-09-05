package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    public Message newMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int id){
        return messageRepository.findById(id);
    }

    public List<Message> getAllMessagesByUserId(int id){
        return messageRepository.findAllMessagesByPostedBy(id);
    }

    public void deleteMessageById(int messageId){
        messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Message message){
        return messageRepository.save(message);
    }
}
