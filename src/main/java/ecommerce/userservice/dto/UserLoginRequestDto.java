package ecommerce.userservice.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String email;
    private String password;
}
