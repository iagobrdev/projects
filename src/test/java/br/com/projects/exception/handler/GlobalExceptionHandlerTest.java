package br.com.projects.exception.handler;

import br.com.projects.exception.BusinessException;
import br.com.projects.exception.handler.GlobalExceptionHandler.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleBusinessException() {
        String expectedMessage = "Erro de Negócio";
        String expectedDescription = "Descrição detalhada do erro";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        BusinessException exception = new BusinessException(expectedMessage, expectedDescription, expectedStatus);

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleBusinessException(exception);

        assertNotNull(responseEntity);
        assertEquals(expectedStatus, responseEntity.getStatusCode());

        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(expectedMessage, errorResponse.getMessage());
        assertEquals(expectedDescription, errorResponse.getDescription());
    }

    @Test
    void testErrorResponse() {
        String expectedMessage = "Erro";
        String expectedDescription = "Erro no sistema";

        ErrorResponse errorResponse = new ErrorResponse(expectedMessage, expectedDescription);

        assertEquals(expectedMessage, errorResponse.getMessage());
        assertEquals(expectedDescription, errorResponse.getDescription());
    }
}
