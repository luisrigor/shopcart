package com.gsc.shopcart.controller.impl;

import com.gsc.shopcart.controller.AuthenticationController;
import com.gsc.shopcart.dto.UserDTO;
import com.gsc.shopcart.security.JwtAuthenticationToken;
import com.gsc.shopcart.security.TokenProvider;
import com.gsc.shopcart.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    @Override
    public UserDTO createAuthenticationToken(String loginToken,String appId) {

        loginToken = appId+"|||"+loginToken;
        Authentication authentication = authenticationManager.authenticate(new JwtAuthenticationToken(loginToken));
        String token = tokenProvider.createToken(authentication, appId);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return new UserDTO(token, userPrincipal.getRoles(), userPrincipal.getClientId());
    }
}
