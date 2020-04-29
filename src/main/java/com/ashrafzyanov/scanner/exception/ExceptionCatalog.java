package com.ashrafzyanov.scanner.exception;

public class ExceptionCatalog extends Exception {
    private byte codeError;
    private String MessageError;
    private String ParrentMessageError;

    public ExceptionCatalog(byte CodeError, String MessageError, String ParrentMsg) {
        super("Error! Code error = " + CodeError + " MessageError = " + MessageError);
        this.codeError = CodeError;
        this.MessageError = MessageError;
        this.ParrentMessageError = ParrentMsg;
    }

    public String getMessageError() {
        return MessageError;
    }

    public byte getCodeError() {
        return codeError;
    }

    public String getParrentMessageError() {
        return ParrentMessageError;
    }
    
}
