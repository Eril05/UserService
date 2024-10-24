package ecommerce.userservice.dto;

import ecommerce.userservice.model.User;
import lombok.Data;

@Data
public class UserSignUpRequestDto {

    private String username;
    private String email;
    private String password;



}
