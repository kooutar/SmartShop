package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.Mapper.LoginMapper;
import com.kaoutar.SmartShop.DTO.LoginRequest;
import com.kaoutar.SmartShop.DTO.Responses.LoginResponse;
import com.kaoutar.SmartShop.Security.CustomUserDetails;
import com.kaoutar.SmartShop.Security.JwtService;
import com.kaoutar.SmartShop.model.User;
import com.kaoutar.SmartShop.repositery.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class AuthService {
     private final PasswordEncoder passwordEncoder;
     private  final UserRepository userRepository;
    private  final JwtService jwtService;
     public LoginResponse login(LoginRequest request){
      String message="";
      String token="";
      String refreshToken="";
      User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new RuntimeException("username Introuvable"));
      if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
          message="Connexion réussie";
          CustomUserDetails userDetails= new CustomUserDetails(user);
           token=jwtService.generateToken(userDetails);
           refreshToken=jwtService.generateRefreshToken(userDetails);
      }else {
          throw  new RuntimeException("Mot de passe incorrect");
      }
      return  LoginMapper.toLoginResponse(request,token,refreshToken);
     }

}
