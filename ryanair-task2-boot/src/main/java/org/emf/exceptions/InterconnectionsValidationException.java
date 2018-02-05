package org.emf.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InterconnectionsValidationException extends RuntimeException {

    public InterconnectionsValidationException(String message) {
        super(message);
    }
}
