package br.com.weblinker.users.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaginationConfiguration {

    @Value("${app.pagination.default-page}")
    private int defaultPage;

    @Value("${app.pagination.default-size}")
    private int defaultSize;

    @Value("${app.pagination.max-size}")
    private int maxSize;

    public int getDefaultPage() {
        return defaultPage;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
