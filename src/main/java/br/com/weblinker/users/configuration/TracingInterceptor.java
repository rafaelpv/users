package br.com.weblinker.users.configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TracingInterceptor implements HandlerInterceptor {

    private final Tracer tracer;

    @Value("${spring.application.service-name:default}")
    private String serviceName;

    public TracingInterceptor(OpenTelemetry openTelemetry) {
        this.tracer = openTelemetry.getTracer(serviceName);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Span span = tracer.spanBuilder(request.getMethod() + " " + request.getRequestURI()).startSpan();
        request.setAttribute("currentSpan", span);
        span.makeCurrent();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Span span = (Span) request.getAttribute("currentSpan");
        if (span != null) {
            span.end();
        }
    }
}
