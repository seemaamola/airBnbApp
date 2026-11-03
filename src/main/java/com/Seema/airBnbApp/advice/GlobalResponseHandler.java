package com.Seema.airBnbApp.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalResponseHandler
        implements ResponseBodyAdvice<Object> {

    private static final List<String> ALLOWED_ROUTES =
            List.of("/v3/api-docs", "/actuator");

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {

        // Apply this response handler to supported controller responses.
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // Get the requested API path.
        String path = request.getURI().getPath();

        // Check whether the route should be excluded from wrapping.
        boolean isExcludedRoute = ALLOWED_ROUTES.stream()
                .anyMatch(path::startsWith);

        // Do not wrap responses that are already wrapped or excluded.
        if (body instanceof ApiErrorResponse<?> || isExcludedRoute) {
            return body;
        }

        // Wrap the response body in a standard response structure.
        return new ApiErrorResponse<>(body);
    }
}