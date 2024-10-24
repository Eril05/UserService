package ecommerce.userservice.dto;

import ecommerce.userservice.model.Token;
import ecommerce.userservice.model.User;
import lombok.Data;

@Data
public class UserLoginResponseDto {

    Token token;


    public static UserLoginResponseDto of(Token token) {

        UserLoginResponseDto dto = new UserLoginResponseDto();
         dto.setToken(token);
        return dto;
    }

}
