package com.gsc.shopcart.config.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import org.springdoc.core.providers.ObjectMapperProvider;

@Configuration
/**
 * Class that represents the configuration of the Swagger
 */
public class SwaggerConfig {


    @Autowired
    private ObjectMapperProvider objectMapperProvider;
    private final String applicationName;
    private final String tokenName;
    private final String bearerToken;
    private final String contextPath;
    private final String baseUrl;

    public SwaggerConfig(@Value("${app.baseUrl}") String baseUrl,
                         @Value("${application.name}") String applicationName,
                         @Value("${app.auth.token-name}") String tokenName,
                         @Value("${app.auth.bearer-Token}") String bearerToken,
                         @Value("${server.servlet.context-path}") String contextPath) {
        this.baseUrl = baseUrl;
        this.applicationName = applicationName;
        this.tokenName = tokenName;
        this.contextPath = contextPath;
        this.bearerToken = bearerToken;
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("ShopCart")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        final String securitySchemeName = "API Key";
        final String apiTitle = String.format("%s API", StringUtils.capitalize(applicationName));

        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName)
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(tokenName)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER))
                        .addSecuritySchemes(
                                "Bearer Authentication",
                                new SecurityScheme()
                                        .name(bearerToken)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")))
                .servers(getServers())
                .info(new Info().title(apiTitle));
    }

    private List<Server> getServers() {
        List<Server> servers = new ArrayList<>();

        Server server = new Server();
        server.setUrl(baseUrl + contextPath);
        server.description("Server url");
        servers.add(server);

        return servers;
    }

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(
            SwaggerUiConfigProperties configProperties,
            SwaggerUiOAuthProperties oauthProperties,
            SwaggerUiConfigParameters configParameters,
            SwaggerWelcomeCommon common) {
        return new SwaggerCodeBlockTransformer(configProperties, oauthProperties,
                configParameters, common, objectMapperProvider);
    }

}

