package com.gsc.shopcart.controller;


import com.gsc.shopcart.dto.entity.UserDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthenticationController {
    @SecurityRequirements
    @PostMapping(value = "/sign-in/{appId}")
    UserDTO createAuthenticationToken(@RequestHeader("loginToken") String loginToken, @PathVariable String appId);
}


