package ecommerce.userservice.service;

import ecommerce.userservice.dto.UserLoginResponseDto;
import ecommerce.userservice.dto.UserSignUpResponseDto;
import ecommerce.userservice.exception.IncorrectPasswordException;
import ecommerce.userservice.exception.InvalidTokenException;
import ecommerce.userservice.exception.UserAlreadyPresentException;
import ecommerce.userservice.model.Token;
import ecommerce.userservice.model.User;
import ecommerce.userservice.repository.TokenRepository;
import ecommerce.userservice.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public User signUp(String username,String email,String password) throws UserAlreadyPresentException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user=new User();
        if(userOptional.isPresent()){
                throw new UserAlreadyPresentException("User already exists with this email address");
        }
        else{
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

        }

       return  userRepository.save(user);
    }

    public Token login(String email,String password) throws IncorrectPasswordException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(passwordEncoder.matches(password,user.getPassword())){
                Token token= generateToken(user);
                return tokenRepository.save(token);

            }
            else{
                throw new IncorrectPasswordException("Sorry the password is incorrect");
            }
        }
        else{

            throw new UsernameNotFoundException("User not found");
        }

    }

    public void logOut(String token) throws InvalidTokenException {
        System.out.println(token);
        System.out.println(token.length());
        System.out.println("PgsoCfZQhcgkHYobDPHdSMQVNeeYHWVhshcWCPxmXtQfKavUhAMXaBPfBaQaUUnuBDEgAEeswTMDhUnvingzmGZNpBprMunmUpupAEkorLCZuQAZlErkPEsIOvzXjgir".length());
        System.out.println(token.trim().equals("PgsoCfZQhcgkHYobDPHdSMQVNeeYHWVhshcWCPxmXtQfKavUhAMXaBPfBaQaUUnuBDEgAEeswTMDhUnvingzmGZNpBprMunmUpupAEkorLCZuQAZlErkPEsIOvzXjgir".trim()));
        Optional<Token>tokenOptional=tokenRepository.findByValueAndDeleted(token, false);
        if(tokenOptional.isEmpty()){
            throw new InvalidTokenException("Sorry this token is not valid");
        }
        Token tokenGet=tokenOptional.get();
        tokenGet.setDeleted(true);
        tokenRepository.save(tokenGet);

    }

    public Token generateToken(User user) {

        LocalDate timnow=LocalDate.now();
        LocalDate timeAfterthrityDays=timnow.plusDays(30);
        Date expiryDate=Date.from(timeAfterthrityDays.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token=new Token();
        token.setUser(user);
        token.setExpiresAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphabetic(128));
        token.setCreatedAt(user.getCreatedAt());
        token.setUpdatedAt(user.getUpdatedAt());

        return token;
    }

    public UserSignUpResponseDto ValidateToken(String token) throws InvalidTokenException {
        Optional<Token>tokenOptional=tokenRepository.findByValueAndDeleted(token, false);
        if(tokenOptional.isEmpty()){
            throw new InvalidTokenException("Sorry this token is not valid");
        }
        Token tokenGet=tokenOptional.get();
        UserSignUpResponseDto userSignUpResponseDto=new UserSignUpResponseDto();
        userSignUpResponseDto.setEmail(tokenGet.getUser().getEmail());
        userSignUpResponseDto.setUsername(tokenGet.getUser().getUsername());
        userSignUpResponseDto.setRoles(tokenGet.getUser().getUserRoles());
        return userSignUpResponseDto;
    }

}
