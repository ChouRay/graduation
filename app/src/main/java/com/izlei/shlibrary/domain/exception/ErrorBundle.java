package com.izlei.shlibrary.domain.exception;

/**
 * Interface to represent a wrapper around an {@link java.lang.Exception} to manage errors.
 * Created by zhouzili on 2015/4/20.
 */
public interface ErrorBundle {
    Exception getExeception();

    String getErrorMessage();
}


