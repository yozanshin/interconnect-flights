package org.emf.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InterconnectionsInternalException extends RuntimeException {

    public InterconnectionsInternalException(String message) {
        super(message);
    }
}
