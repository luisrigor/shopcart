package com.gsc.shopcart.controller;


import com.gsc.shopcart.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "",tags = "Shop cart - Authentication")
public interface AuthenticationController {
    @SecurityRequirements
    @PostMapping(value = "/sign-in/{appId}")
    UserDTO createAuthenticationToken(@RequestHeader("loginToken") String loginToken, @PathVariable String appId);
}


