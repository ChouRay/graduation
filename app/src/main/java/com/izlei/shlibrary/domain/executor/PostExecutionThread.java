package com.izlei.shlibrary.domain.executor;

/**
 *  Thread abstraction create to change the execution context from any thread to any other thread.
 *  Useful to encapsulate a UI thread for example, since some job will be done in background ,
 *  an implementation of this interface will change context and update the UI.
 * Created by zhouzili on 2015/4/20.
 */
public interface PostExecutionThread {

    /**
     * Causes the {@link Runnable} to be added to the message queue fo the Main UI Thread of the
     * Application
     * @param runnable
     */
    void post(Runnable runnable);
}

