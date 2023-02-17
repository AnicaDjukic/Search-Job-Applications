package com.example.ELK.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException() {
        super("Application not found!");
    }
}
