package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.LoginRequest;
import com.kaoutar.SmartShop.DTO.Responses.LoginResponse;


public class LoginMapper {
    private LoginMapper() {
        throw new UnsupportedOperationException("Utility class");
    }
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
