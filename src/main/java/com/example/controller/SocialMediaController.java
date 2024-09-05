package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService; 
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (account.getUsername().isBlank() || account.getUsername().isEmpty() || account.getPassword().length() < 4)
            return ResponseEntity.status(400).build();

        if (accountService.getAccountByUsername(account.getUsername()).isPresent()) {
            return ResponseEntity.status(409).build();
        }

        Account createdAccount = accountService.createAccount(account);
        if (createdAccount != null)
            return ResponseEntity.status(200).body(accountService.getAccountByUsername(account.getUsername()).get());

        return ResponseEntity.status(400).build();

    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        if (account.getUsername().isBlank() || account.getUsername().isEmpty() || account.getPassword().length() < 4)
            return ResponseEntity.status(400).build();

        Optional<Account> accountfound = accountService.getAccountByUsername(account.getUsername());

        if (accountfound.isPresent() && accountfound.get().getPassword().equals(account.getPassword())) {
            return ResponseEntity.status(200).body(accountfound.get());
        }

        return ResponseEntity.status(401).build();

    }

    @PostMapping("messages")
    public ResponseEntity<Message> newMessage(@RequestBody Message message) {
        if (message.getMessageText().isBlank() || message.getMessageText().isEmpty()
                || message.getMessageText().length() > 255
                || !accountService.getAccountById(message.getPostedBy()).isPresent())
            return ResponseEntity.status(400).build();

        Message createdMsg = messageService.newMessage(message);
        if (createdMsg != null) {
            return ResponseEntity.status(200).body(createdMsg);
        }

        return ResponseEntity.status(400).build();

    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        if (message.isPresent()) {
            return ResponseEntity.status(200).body(message.get());
        }

        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByAccountId(@PathVariable int accountId) {

        return ResponseEntity.status(200).body(messageService.getAllMessagesByUserId(accountId));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        if (messageService.getMessageById(messageId).isPresent()) {
            messageService.deleteMessageById(messageId);
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).build();

    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message updatedMessage) {
        if (updatedMessage.getMessageText().isBlank() || updatedMessage.getMessageText().isEmpty()
                || updatedMessage.getMessageText().length() > 255
                || !messageService.getMessageById(messageId).isPresent())
            return ResponseEntity.status(400).build();

        Message message = messageService.getMessageById(messageId).get();

        message.setMessageText(updatedMessage.getMessageText());
        Message savedMessage = messageService.updateMessage(message);
       
        if (savedMessage != null) return ResponseEntity.status(200).body(1);

        return ResponseEntity.status(400).build();
    }

}
