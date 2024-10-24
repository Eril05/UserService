package ecommerce.userservice.controller;

import ecommerce.userservice.dto.UserLoginRequestDto;
import ecommerce.userservice.dto.UserLoginResponseDto;
import ecommerce.userservice.dto.UserSignUpRequestDto;
import ecommerce.userservice.dto.UserSignUpResponseDto;
import ecommerce.userservice.exception.IncorrectPasswordException;
import ecommerce.userservice.exception.InvalidTokenException;
import ecommerce.userservice.exception.UserAlreadyPresentException;
import ecommerce.userservice.model.Token;
import ecommerce.userservice.model.User;
import ecommerce.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {

private UserService userService;

UserController(UserService userService) {
    this.userService = userService;
}

    @PostMapping("/signUp")
    public UserSignUpResponseDto signUp(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {

        User user= null;
        try {
            user = userService.signUp(userSignUpRequestDto.getUsername(),userSignUpRequestDto.getEmail(),userSignUpRequestDto.getPassword());
        } catch (UserAlreadyPresentException e) {
            throw new RuntimeException(e);
        }

        return UserSignUpResponseDto.from(user);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto userLoginRequestDto) {

        try {
           Token token= userService.login(userLoginRequestDto.getEmail(),userLoginRequestDto.getPassword());
           return  UserLoginResponseDto.of(token);

        } catch (IncorrectPasswordException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String token) {
            token=token.trim();
        try {
            userService.logOut(token);
        } catch (InvalidTokenException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("validate/{token}")
    public UserSignUpResponseDto validateUser(@PathVariable String token) {
        try {
            return userService.ValidateToken(token);
        } catch (InvalidTokenException e) {
            throw new RuntimeException(e);
        }
    }

}
