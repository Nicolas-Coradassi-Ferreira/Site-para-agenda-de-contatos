package br.com.nicoservices.contatospersistence.exception;

public class UsuarioJaCadastradoException extends RuntimeException {
    public UsuarioJaCadastradoException(String msg) {
        super(msg);
    }
}
