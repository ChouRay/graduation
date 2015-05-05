package com.izlei.shlibrary.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.view.fragment.BookListFragment;

/**
 * Created by zhouzili on 2015/4/20.
 */
public class BookListActivity extends BaseActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BookListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        if (savedInstanceState == null) {
            this.addFragment(R.id.book_list_layout, BookListFragment.newInstance());
        }
    }
}
