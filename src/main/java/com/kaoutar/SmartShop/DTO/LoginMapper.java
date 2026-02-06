package com.kaoutar.SmartShop.DTO;

import com.kaoutar.SmartShop.DTO.Responses.LoginResponse;
import org.springframework.stereotype.Component;


public class LoginMapper {
    public static LoginResponse toLoginResponse(LoginRequest loginRequest,
                                                String token,
                                                String refreshToken) {
        return new LoginResponse(
                token,
                refreshToken,
                loginRequest.getUsername()
        );
    }
}
