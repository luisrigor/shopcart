package com.gsc.shopcart.config.swagger;

import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SwaggerCodeBlockTransformer extends SwaggerIndexPageTransformer {

    @Autowired
    private ResourceLoader loader;

    public SwaggerCodeBlockTransformer(SwaggerUiConfigProperties configProperties, SwaggerUiOAuthProperties oauthProperties,
                                       SwaggerUiConfigParameters configParameters, SwaggerWelcomeCommon common, ObjectMapperProvider objectMapperProvider) {
        super(configProperties, oauthProperties, configParameters, common, objectMapperProvider);
    }

    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        if (resource.toString().contains("index.html")) {
            return loader.getResource("classpath:static/swagger/index.html");
        }

        return super.transform(request, resource, transformerChain);
    }
}
