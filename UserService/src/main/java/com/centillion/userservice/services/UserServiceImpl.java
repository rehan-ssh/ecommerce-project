package com.centillion.userservice.services;

import com.centillion.userservice.dtos.SendEmailDto;
import com.centillion.userservice.models.Token;
import com.centillion.userservice.models.User;
import com.centillion.userservice.repositories.TokenRepository;
import com.centillion.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    TokenRepository tokenRepository;
    KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper;
    String senderEmail;

    UserServiceImpl(UserRepository userRepository,
                    BCryptPasswordEncoder bCryptPasswordEncoder,
                    TokenRepository tokenRepository,
                    KafkaTemplate<String, String> kafkaTemplate,
                    ObjectMapper objectMapper,
                    @Value("${sender.email}") String senderEmail) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.senderEmail = senderEmail;
    }

    @Override
    public User signup(String name, String email, String password) {


        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setFrom(senderEmail);
        sendEmailDto.setSubject("User Registration");
        sendEmailDto.setBody("Hello, " + name + "!");
        sendEmailDto.setTo(email);

        try {
            String sendEmailDtoString = objectMapper.writeValueAsString(sendEmailDto);
            kafkaTemplate.send("sendEmail", sendEmailDtoString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return userRepository.save(user);
    }

    @Override
    public Token login(String username, String password) {
       Optional<User> userOptional = userRepository.findByEmail(username);
       if(userOptional.isEmpty()){
           return null;
       }
       User user = userOptional.get();
       if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
           return null;
       }
       Token token = new Token();
       token.setUser(user);
       token.setValue(UUID.randomUUID().toString());
       token.setDeleted(false);

       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, 7);
       Date date = cal.getTime();
       token.setExpiryAt(date);
       return tokenRepository.save(token);
    }

    public boolean logout(String token) {
        return tokenRepository
                .findByValueAndDeletedNotAndExpiryAtGreaterThan(token, true, new Date())
                .map(t -> {
                    t.setDeleted(true);
                    tokenRepository.save(t);
                    return true;
                })
                .orElse(false);
    }


    @Override
    public User validateToken(String tokenValue) {
        /*
         * 1. Should exist in DB
         * 2. Should not be deleted
         * 3. Should not have expired
         * */

        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedNotAndExpiryAtGreaterThan(tokenValue,
                true, new Date());
        System.out.println( optionalToken.map(Token::getUser).orElse(null));
        return optionalToken.map(Token::getUser).orElse(null);

    }
}
