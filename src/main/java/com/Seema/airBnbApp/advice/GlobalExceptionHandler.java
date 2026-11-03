package com.Seema.airBnbApp.advice;

import com.Seema.airBnbApp.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Uncomment this import if your project uses jjwt:
// import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse<?>> handleResourceNotFoundException(
            ResourceNotFoundException exception) {

        ApiError apiError = new ApiError(
                exception.getLocalizedMessage(),
                HttpStatus.NOT_FOUND
        );

        ApiErrorResponse<?> response = new ApiErrorResponse<>(apiError);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse<?>> handleInternalServerError(
            Exception exception) {

        // Avoid exposing internal exception details to the client.
        // Log the exception using a logger in production.

        ApiError apiError = new ApiError(
                "An unexpected internal server error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        ApiErrorResponse<?> response = new ApiErrorResponse<>(apiError);

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ApiErrorResponse<?>> handleAuthenticationException(
//            AuthenticationException exception) {
//
//        ApiError apiError = new ApiError(
//                exception.getLocalizedMessage(),
//                HttpStatus.UNAUTHORIZED
//        );
//
//        ApiErrorResponse<?> response = new ApiErrorResponse<>(apiError);
//
//        return new ResponseEntity<>(
//                response,
//                HttpStatus.UNAUTHORIZED
//        );
//    }

//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<ApiErrorResponse<?>> handleJwtException(
//            JwtException exception) {
//
//        ApiError apiError = new ApiError(
//                exception.getLocalizedMessage(),
//                HttpStatus.UNAUTHORIZED
//        );
//
//        ApiErrorResponse<?> response = new ApiErrorResponse<>(apiError);
//
//        return new ResponseEntity<>(
//                response,
//                HttpStatus.UNAUTHORIZED
//        );
//    }

}
