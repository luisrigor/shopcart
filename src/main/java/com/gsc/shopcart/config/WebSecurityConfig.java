package com.gsc.shopcart.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.repository.scart.ClientRepository;
import com.gsc.shopcart.repository.scart.ConfigurationRepository;
import com.gsc.shopcart.security.JwtAuthenticationFilter;
import com.gsc.shopcart.security.JwtAuthenticationManager;
import com.gsc.shopcart.security.RestAuthenticationEntryPoint;
import com.gsc.shopcart.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
/**
 * Class that represents the configuration of the WebSecurity
 */
public class WebSecurityConfig {
    @Bean
    public AuthenticationManager authenticationManagerBean(ConfigurationRepository configurationRepository, EnvironmentConfig environmentConfig) throws Exception {
        return new JwtAuthenticationManager(configurationRepository, environmentConfig);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Value("${app.auth.token-name}") String tokenName, TokenProvider tokenProvider, ObjectMapper objectMapper) throws Exception {
        http
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/signin", "/events", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-cust/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint(objectMapper));

        http.addFilterBefore(new JwtAuthenticationFilter(tokenProvider, tokenName), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> customCorsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setName("CorsConfiguration");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}


