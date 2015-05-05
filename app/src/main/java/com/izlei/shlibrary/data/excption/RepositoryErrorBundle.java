package com.izlei.shlibrary.data.excption;

import com.izlei.shlibrary.domain.exception.ErrorBundle;

/**
 * Created by zhouzili on 2015/4/28.
 */
public class RepositoryErrorBundle implements ErrorBundle {
    private final Exception exception;

    public RepositoryErrorBundle(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Exception getExeception() {
        return exception;
    }

    @Override
    public String getErrorMessage() {
        String message = "";
        if (this.exception != null) {
            this.exception.getMessage();
        }
        return message;
    }
}
