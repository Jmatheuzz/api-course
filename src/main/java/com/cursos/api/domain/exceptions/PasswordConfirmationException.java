package com.cursos.api.domain.exceptions;

public class PasswordConfirmationException extends RuntimeException{
    public PasswordConfirmationException(String message){
        super(message);
    }
}
