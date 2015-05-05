package com.izlei.shlibrary.domain.interactor;

/**
 * Common interface for an Interactor {@link java.lang.Runnable} declared in the application.
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each Interactor implementation will return the result using a Callback that should
 * be executed in the UI thread.
 *
 * Created by zhouzili on 2015/4/20.
 */
public interface Interactor extends Runnable{
    /**
     * Everything inside this method will be executed asynchronously
     */
    void run();
}
