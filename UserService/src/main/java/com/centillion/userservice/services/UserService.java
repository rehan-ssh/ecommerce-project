package com.centillion.userservice.services;

import com.centillion.userservice.models.Token;
import com.centillion.userservice.models.User;

public interface UserService {
    User signup(String name, String email, String password);
    Token login(String username, String password);
    boolean logout(String token);
    User validateToken(String token);
}
