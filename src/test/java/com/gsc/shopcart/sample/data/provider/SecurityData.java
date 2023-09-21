package com.gsc.shopcart.sample.data.provider;


import com.gsc.shopcart.constants.AppProfile;
import com.gsc.shopcart.model.scart.entity.LoginKey;
import com.gsc.shopcart.security.UserPrincipal;
import com.rg.dealer.Dealer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.naming.directory.Attributes;
import java.time.LocalDateTime;
import java.util.*;

public class SecurityData {

    private static final String ISSUER = "Tcap";
    private static final String AUDIENCE = "Tcap Clients";
    private static final String ROLES = "roles";
    private static final String JWT_ENVIRONMENT = "environment";
    private static final String JWT_CLIENT_ID = "client";
    private static final String CREATED_BY = "Tcap-TokenProvider";
    public static  final  String ACTIVE_PROFILE = "local";

    public LoginKey getLoginKey(){
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        //This key was generated for testing purpouses
        String key = "T5LyHroP0hxSTDeOyh1knSH1ZqWJyJ9KHb7T7nQNkPvgsRI2xKV9OIPOugSf6bXKLRXPkfB07YXJ03EhzPC/kg==";

        LoginKey loginKey = new LoginKey();
        loginKey.setId(1L);
//        loginKey.setKeyValue(Encoders.BASE64.encode(key.getEncoded()));
        loginKey.setKeyValue(key);
        loginKey.setEnabled(true);
        loginKey.setCreatedBy(CREATED_BY);
        loginKey.setCreated(LocalDateTime.now());

        return loginKey;

    }

    public String generateNewToken() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TOYOTA_LEXUS_PRF_TCAP);
        UserPrincipal userPrincipal = new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles, 1L);
        Date now = new Date();
        Date expiryDate =  new Date(now.getTime() + 648000L);

        LoginKey loginKey =  getLoginKey();

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(userPrincipal.getUsername())
                .setAudience(AUDIENCE)
                .setExpiration(expiryDate)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(loginKey.getKeyValue())))
                .setHeaderParam("kid", loginKey.getId())
                .claim(JWT_ENVIRONMENT, ACTIVE_PROFILE)
                .claim(JWT_CLIENT_ID, userPrincipal.getClientId())
                .claim(ROLES, userPrincipal.getRoles())
                .compact();
    }

    public UserPrincipal getUserPrincipal() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TOYOTA_LEXUS_PRF_TCAP);
        return new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
    }

    public static UserPrincipal getUserDefaultStatic() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TOYOTA_LEXUS_PRF_TCAP);
        UserPrincipal userPrincipal = new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,59L);
        userPrincipal.setOidNet(Dealer.OID_NET_TOYOTA);
        return userPrincipal;
    }

    public UserPrincipal getUserToyotaProfile() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TOYOTA_LEXUS_PRF_TCAP);
        UserPrincipal userPrincipal = new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,59L);
        userPrincipal.setOidNet(Dealer.OID_NET_TOYOTA);
        userPrincipal.setTcapProfile("116");
        userPrincipal.setSupplierProfile("117");
        userPrincipal.setDealerProfile("134");
        return userPrincipal;
    }
}


