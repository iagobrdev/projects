package br.com.projects.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionTest {

    @Test
    void testBusinessExceptionAttributes() {
        String expectedMessage = "Erro ao processar requisição";
        String expectedDescription = "Detalhes do erro";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        BusinessException exception = new BusinessException(expectedMessage, expectedDescription, expectedStatus);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedDescription, exception.getDescription());
        assertEquals(expectedStatus, exception.getStatus());
    }

    @Test
    void testBusinessExceptionWithDifferentStatus() {
        String expectedMessage = "Erro Interno";
        String expectedDescription = "Erro no sistema";
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        BusinessException exception = new BusinessException(expectedMessage, expectedDescription, expectedStatus);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedDescription, exception.getDescription());
        assertEquals(expectedStatus, exception.getStatus());
    }
}
