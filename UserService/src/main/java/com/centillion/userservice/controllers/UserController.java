package com.centillion.userservice.controllers;


import com.centillion.userservice.dtos.*;
import com.centillion.userservice.models.Token;
import com.centillion.userservice.models.User;
import com.centillion.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/")
public class UserController {

    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        User user = userService.signup(signupRequestDto.getName(), signupRequestDto.getEmail(), signupRequestDto.getPassword());
        return UserDto.from(user);

    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(token.getValue());
        return loginResponseDto;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
       boolean logoutIsSuccessful = userService.logout(logoutRequestDto.getTokenValue());
        if(logoutIsSuccessful)
        {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable("token") String token){
        User user = userService.validateToken(token);
        if(user == null){
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
