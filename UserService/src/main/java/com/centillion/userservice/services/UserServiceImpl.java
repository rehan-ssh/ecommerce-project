package com.centillion.userservice.services;

import com.centillion.userservice.models.Token;
import com.centillion.userservice.models.User;
import com.centillion.userservice.repositories.TokenRepository;
import com.centillion.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
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

    UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;

    }

    @Override
    public User signup(String name, String email, String password) {


        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));


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

       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, 30);
       Date date = cal.getTime();
       token.setExpiryAt(date);
       return tokenRepository.save(token);
    }

    public boolean logout(String token) {
        return tokenRepository
                .findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date())
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

        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(tokenValue,
                false, new Date());

        return optionalToken.map(Token::getUser).orElse(null);

    }
}
