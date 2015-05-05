package com.izlei.shlibrary.domain.executor;

import com.izlei.shlibrary.domain.interactor.Interactor;

/**
 * Executor implementation can be based on different f
 * execution, but every implementation will execute th
 * Use this class to execute an {@link Interactor}.
 *
 * Created by zhouzili on 2015/4/20.
 */
public interface ThreadExecutor {

    /**
     * Executes a {@link Runnable}.
     *
     * @param runnable The class that implements {@link Runnable} interface.
     */
    void execute(final Runnable runnable);
}