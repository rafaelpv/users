package br.com.weblinker.users.configuration;

import br.com.weblinker.users.components.CustomPageableResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// Custom pageable resolver, configured to accept max 50 items per page
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final CustomPageableResolver customPageableResolver;

    public WebConfiguration(CustomPageableResolver customPageableResolver) {
        this.customPageableResolver = customPageableResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customPageableResolver);
    }
}
