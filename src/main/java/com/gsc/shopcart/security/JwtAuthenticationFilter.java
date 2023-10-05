package com.gsc.shopcart.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

   private final TokenProvider tokenProvider;
   private final String tokenName;

   public JwtAuthenticationFilter(TokenProvider tokenProvider, @Value("${app.auth.token-name}") String tokenName) {
      this.tokenProvider = tokenProvider;
      this.tokenName = tokenName;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      logger.trace("Filtering...");
      JwtAuthenticationToken authentication = null;
      try {
         String accessToken = request.getHeader(tokenName);
         authentication = StringUtils.hasText(accessToken) ? tokenProvider.validateToken(accessToken) :
                 microsoftAuthenticationToken(request, authentication);

         if (authentication != null) {
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
         }
      } catch (AuthenticationException ex) {
         logger.error(Constants.USER_AUTHENTICATION_ERROR, ex);
      }
      filterChain.doFilter(request, response);
   }

   private JwtAuthenticationToken microsoftAuthenticationToken(HttpServletRequest request, JwtAuthenticationToken authentication) {
      String authorization = request.getHeader(Constants.AUTHORIZATION);

      if (StringUtils.hasText(authorization)) {
         authorization = authorization.substring("Bearer ".length());
         authentication = tokenProvider.validateMicrosoftServiceToken(authorization);
      }
      return authentication;
   }

}
