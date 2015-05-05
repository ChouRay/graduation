package com.izlei.shlibrary.presentation.exception;

import android.content.Context;

import com.izlei.shlibrary.R;

/**
 * Created by zhouzili on 2015/4/20.
 */
public class ErrorMessageFactory {

    /**
     * Creates a String representing an error message.
     *
     * @param context Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);


        return message;
    }
}
