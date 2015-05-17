package com.izlei.shlibrary.presentation.view.activity;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.izlei.shlibrary.R;

import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.view.fragment.BookDetailsFragment;


public class BookDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    BookDetailsFragment bookDetailsFragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BookDetailsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar_book_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.details));

        final String isbn = getIntent().getStringExtra("ISBN");
        bookDetailsFragment = BookDetailsFragment.newInstance();
        if (isbn != null){
            Bundle bundle = new Bundle();
            bundle.putString("ISBN", isbn);
            bookDetailsFragment.setArguments(bundle);
            this.addFragment(R.id.book_details_container, bookDetailsFragment);
        }
    }

    public void onCollectClick(View v) {

    }
    public void onBuyClick(View v) {
        bookDetailsFragment.getCurrentBook(new BookDetailsFragment.GetBookModelCallback() {
            @Override
            public void getBookModel(BookModel bookModel) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent .setData(Uri.parse(bookModel.getAlt()));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
