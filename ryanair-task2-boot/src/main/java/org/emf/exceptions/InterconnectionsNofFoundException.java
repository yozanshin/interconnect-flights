package org.emf.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InterconnectionsNofFoundException extends RuntimeException{

    public InterconnectionsNofFoundException(String message) {
        super(message);
    }
}
