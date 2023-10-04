package com.gsc.shopcart.security;

import com.gsc.shopcart.constants.AppProfile;
import com.gsc.shopcart.exceptions.AuthTokenException;
import com.gsc.shopcart.model.scart.entity.Configuration;
import com.gsc.shopcart.model.scart.entity.LoginKey;
import com.gsc.shopcart.model.scart.entity.ServiceLogin;
import com.gsc.shopcart.repository.scart.ConfigRepository;
import com.gsc.shopcart.repository.scart.ConfigurationRepository;
import com.gsc.shopcart.repository.scart.LoginKeyRepository;
import com.gsc.shopcart.repository.scart.ServiceLoginRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import static com.gsc.shopcart.constants.ApiConstants.LEXUS_APP;
import static com.gsc.shopcart.constants.ApiConstants.TOYOTA_APP;

@Service
public class TokenProvider {

   private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

   private static final String CREATED_BY = "Tcap-TokenProvider";

   private static final String ISSUER = "Tcap";
   private static final String AUDIENCE = "Tcap Clients";
   private static final String ROLES = "roles";
   private static final String JWT_ENVIRONMENT = "environment";
   private static final String JWT_CLIENT_ID = "client";
   private static final String OID_DEALER_PARENT = "dealer_parent";
   private static final String OID_DEALER = "dealer";
   private static final String OID_NET = "oid_net";
   private static final String ID_CATALOG = "ID_CATALOG";
   private static final String UPLOAD_DIR = "UPLOAD_DIR";
   private static final String VIRTUAL_PATH = "VIRTUAL_PATH";
   private static final String APPLICATION = "APPLICATION";
   private static final String TCAP_PROFILE = "TCAP_PROFILE";
   private static final String DEALER_PROFILE = "DEALER_PROFILE";
   private static final String SUPPLIER_PROFILE = "SUPPLIER_PROFILE";
   private static final String ID_USER = "ID_USER";
   private static final String ID_ENTITY = "ID_ENTITY";
   private static final String LOGIN = "LOGIN";
   private static final String NIF_UTILIZADOR = "NIF_UTILIZADOR";

   private final ConfigurationRepository configurationRepository;
   private final ServiceLoginRepository serviceLoginRepository;
   private final LoginKeyRepository loginKeyRepository;
   private final ConfigRepository configRepository;
   private final String activeProfile;
   private final UsrLogonSecurity usrLogonSecurity;


   public TokenProvider(ConfigurationRepository configurationRepository, LoginKeyRepository loginKeyRepository,
                        ServiceLoginRepository serviceLoginRepository, ConfigRepository configRepository, @Value("${spring.profiles.active}") String activeProfile,
                        UsrLogonSecurity usrLogonSecurity) {
      this.configurationRepository = configurationRepository;
      this.loginKeyRepository = loginKeyRepository;
      this.serviceLoginRepository = serviceLoginRepository;
      this.configRepository = configRepository;
      this.activeProfile = activeProfile;
      this.usrLogonSecurity = usrLogonSecurity;
   }

   public String createToken(Authentication authentication, String appId) throws AuthenticationException {
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      if(appId.equals(String.valueOf(TOYOTA_APP))) {
         userPrincipal.setOidNet("SC00010001");
      } else if (appId.equals(String.valueOf(LEXUS_APP))) {
         userPrincipal.setOidNet("SC00010002");
      }

      Optional<LoginKey> loginKey = getKey();

      if (!loginKey.isPresent()) {
         throw new AuthenticationServiceException("Unable to find key for token generation");
      }

      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + configurationRepository.getTokenExpirationMsec());

      List<Configuration> configuration = configRepository.findAll();

      if(appId.equals(String.valueOf(TOYOTA_APP))) {
         configuration = configuration.stream()
                 .filter(configp -> configp.getApplication().equals("ToyotaShopping"))
                 .collect(Collectors.toList());
      } else if (appId.equals(String.valueOf(LEXUS_APP))) {
         configuration = configuration.stream()
                 .filter(configp -> configp.getApplication().equals("LexusShopping"))
                 .collect(Collectors.toList());
      }

      if(configuration!=null && !configuration.isEmpty())
         setConfigFields(userPrincipal, configuration);

      usrLogonSecurity.setUserLogin(userPrincipal);


      return Jwts.builder()
              .setIssuer(ISSUER)
              .setSubject(userPrincipal.getUsername())
              .setAudience(AUDIENCE)
              .setExpiration(expiryDate)
              .setIssuedAt(new Date())
              .setId(UUID.randomUUID().toString())
              .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(loginKey.get().getKeyValue())))
              .setHeaderParam("kid", loginKey.get().getId())
              .claim(JWT_ENVIRONMENT, activeProfile)
              .claim(JWT_CLIENT_ID, userPrincipal.getClientId())
              .claim(ROLES, userPrincipal.getRoles())
              .claim(OID_DEALER_PARENT, userPrincipal.getOidDealerParent())
              .claim(OID_DEALER, userPrincipal.getOidDealer())
              .claim(OID_NET, userPrincipal.getOidNet())
              .claim(ID_CATALOG, userPrincipal.getIdCatalog())
              .claim(UPLOAD_DIR, userPrincipal.getUploadDir())
              .claim(VIRTUAL_PATH, userPrincipal.getVirtualPath())
              .claim(APPLICATION, userPrincipal.getApplication())
              .claim(TCAP_PROFILE, userPrincipal.getTcapProfile())
              .claim(DEALER_PROFILE, userPrincipal.getDealerProfile())
              .claim(SUPPLIER_PROFILE, userPrincipal.getSupplierProfile())
              .claim(ID_USER, userPrincipal.getIdUser())
              .claim(ID_ENTITY, userPrincipal.getIdEntity())
              .claim(LOGIN, userPrincipal.getLogin())
              .claim(NIF_UTILIZADOR, userPrincipal.getNifUtilizador())
              .compact();
   }

   private Optional<LoginKey> getKey() {
      Optional<LoginKey> key = loginKeyRepository.findFirstByEnabledIsTrue();
      if (key.isPresent()) {
         return key;
      }
      return generateNewKey();
   }

   private Optional<LoginKey> generateNewKey() {
      if (!configurationRepository.isKeyCreationEnabled()) {
         return Optional.empty();
      }

      SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

      LoginKey loginKey = new LoginKey();
      loginKey.setKeyValue(Encoders.BASE64.encode(key.getEncoded()));
      loginKey.setEnabled(Boolean.TRUE);
      loginKey.setCreatedBy(CREATED_BY);
      loginKey.setCreated(LocalDateTime.now());

      return Optional.of(loginKeyRepository.saveAndFlush(loginKey));
   }

   public JwtAuthenticationToken validateToken(String authToken) throws AuthenticationException {
      if (authToken.contains(":")) {
         return validateServiceToken(authToken);
      }
      return validateUserToken(authToken);

   }

   private JwtAuthenticationToken validateUserToken(String authToken) throws AuthenticationException {
      try {
         Claims claims = Jwts.parserBuilder()
            .setSigningKeyResolver(new DBSigningKeyResolver())
            .requireIssuer(ISSUER)
            .requireAudience(AUDIENCE)
            .require(JWT_ENVIRONMENT, activeProfile)
            .build()
            .parseClaimsJws(authToken)
            .getBody();

         @SuppressWarnings("unchecked")
         Set<AppProfile> roles = (Set<AppProfile>) claims.get(ROLES, List.class).stream()
            .map(role -> AppProfile.valueOf(role.toString()))
            .collect(Collectors.toSet());

         UserPrincipal userPrincipalClaims = new UserPrincipal(
                 claims.getSubject(),
                 roles,
                 claims.get(JWT_CLIENT_ID, Long.class),
                 claims.get(OID_NET, String.class),
                 claims.get(OID_DEALER_PARENT, String.class),
                 claims.get(OID_DEALER, String.class),
                 claims.get(ID_CATALOG, String.class),
                 claims.get(UPLOAD_DIR, String.class),
                 claims.get(VIRTUAL_PATH, String.class),
                 claims.get(APPLICATION, String.class),
                 claims.get(TCAP_PROFILE, String.class),
                 claims.get(DEALER_PROFILE, String.class),
                 claims.get(SUPPLIER_PROFILE, String.class),
                 claims.get(LOGIN, String.class),
                 claims.get(NIF_UTILIZADOR, String.class)
         );

         userPrincipalClaims.setIdUser(claims.get(ID_USER, Integer.class));
         userPrincipalClaims.setIdEntity(claims.get(ID_ENTITY, Integer.class));

         return JwtAuthenticationToken.authenticated(
                 userPrincipalClaims,
            Collections.emptyList()
         );
      } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
         throw new BadCredentialsException("Invalid access token.");
      } catch (ExpiredJwtException ex) {
         throw new AuthTokenException("The session has expired " + ex.getMessage(), ex);
      }
   }

   private JwtAuthenticationToken validateServiceToken(String authToken) throws AuthenticationException {
      String[] parts = authToken.split(":", -1);
      if (parts.length != 2) {
         throw new BadCredentialsException("Invalid access token.");
      }

      Optional<ServiceLogin> serviceLogin = serviceLoginRepository.findByNameAndValue(parts[0], parts[1]);

      ServiceLogin sl = serviceLogin.orElseThrow(() -> new BadCredentialsException("Invalid access token."));

      return JwtAuthenticationToken.authenticated(
         new UserPrincipal(
            parts[0],
                 getRoles(sl),
            JwtAuthenticationManager.CLIENT_ID
         ),
         Collections.emptyList()
      );
   }

   private Set<AppProfile> getRoles(ServiceLogin sl) {
      Set<AppProfile> profiles = new HashSet<>();

//      if (Objects.equals(sl.getUploadFile(), Boolean.TRUE)) {
//         profiles.add(AppProfile.UPLOAD_FILE);
//      }
//
//      if (Objects.equals(sl.getCleanupProjects(), Boolean.TRUE)) {
//         profiles.add(AppProfile.CLEANUP_PROJECTS);
//      }
//
//      if (Objects.equals(sl.getDownloadProjectFiles(), Boolean.TRUE)) {
//         profiles.add(AppProfile.DOWNLOAD_PROJECT_FILES);
//      }

      return profiles;
   }

   private class DBSigningKeyResolver extends SigningKeyResolverAdapter {

      @SuppressWarnings("rawtypes")
      @Override
      public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
         //inspect the header or claims, lookup and return the signing key
         Long keyId = Long.valueOf(jwsHeader.getKeyId()); //or any other field that you need to inspect
         return loginKeyRepository.findById(keyId)
            .map(key -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(key.getKeyValue())))
            .orElse(null);
      }
   }

   private void setConfigFields(UserPrincipal userPrincipal, List<Configuration> configurations) {

      Map<Map<String,String>, String> configMap = configToMap(configurations);

      if(activeProfile.contains("local")) {
         setUserMapFields(userPrincipal, configMap, "local");
      } else if (activeProfile.contains("development")) {
         setUserMapFields(userPrincipal, configMap, "staging");
      } else if (activeProfile.contains("staging")) {
         setUserMapFields(userPrincipal, configMap, "staging");
      } else if (activeProfile.contains("production")) {
         setUserMapFields(userPrincipal, configMap, "production");
      }
   }

   private Map<Map<String,String>, String> configToMap(List<Configuration> configurations) {
      Map<Map<String,String>, String> configMap = new HashMap<>();

      for (Configuration config: configurations) {
         Map<String,String> keyLocal = new HashMap<>();
         keyLocal.put("local", config.getKey());
         configMap.put(keyLocal, config.getLocal());

         Map<String,String> keyStage = new HashMap<>();
         keyStage.put( "staging", config.getKey());
         configMap.put(keyStage, config.getStaging());

         Map<String,String> keyProd = new HashMap<>();
         keyProd.put( "production", config.getKey());
         configMap.put(keyProd, config.getProduction());
      }

      return configMap;
   }

   private void setUserMapFields(UserPrincipal userPrincipal, Map<Map<String,String>, String> configMap, String profile) {

      userPrincipal.setIdCatalog(configMap.get(new HashMap<String, String>() {{put(profile, ID_CATALOG);}}));
      userPrincipal.setUploadDir(configMap.get(new HashMap<String, String>() {{put(profile, UPLOAD_DIR);}}));
      userPrincipal.setVirtualPath(configMap.get(new HashMap<String, String>() {{put(profile, VIRTUAL_PATH);}}));
      userPrincipal.setApplication(configMap.get(new HashMap<String, String>() {{put(profile, APPLICATION);}}));
      userPrincipal.setTcapProfile(configMap.get(new HashMap<String, String>() {{put(profile, TCAP_PROFILE);}}));
      userPrincipal.setDealerProfile(configMap.get(new HashMap<String, String>() {{put(profile, DEALER_PROFILE);}}));
      userPrincipal.setSupplierProfile(configMap.get(new HashMap<String, String>() {{put(profile, SUPPLIER_PROFILE);}}));
   }

}
