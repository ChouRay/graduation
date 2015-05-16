package com.izlei.shlibrary.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.view.fragment.LoginActivityFragment;
import com.izlei.shlibrary.presentation.view.UIRefresh;

public class LoginActivity extends BaseActivity implements UIRefresh{

    public final static int LOGIN_OR_SIGNUP_SUCCESS = MainActivity.LOGIN_REQUEST_CODE;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            this.addFragment(R.id.fragment, LoginActivityFragment.newInstance());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void notifyUIRefresh() {
        this.setResult(LOGIN_OR_SIGNUP_SUCCESS);
        finish();
    }
}
