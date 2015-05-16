package com.izlei.shlibrary.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.presentation.view.fragment.PersonalFragment;
import com.izlei.shlibrary.R;

import cn.bmob.v3.BmobUser;

/**
 * Created by zhouzili on 2015/4/10.
 */
public class PersonalActivity extends BaseActivity{

    public static final int LOGOUT_RESULT_CODE = MainActivity.PERSONAL_REQUEST_CODE;
    public static final int CURRENT_BORROWED = 20;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PersonalActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.personal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("USERMODEL");
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("USERMODEL", userModel);
            PersonalFragment personalFragment = PersonalFragment.newInstance();
            personalFragment.setArguments(bundle);
            this.addFragment(R.id.personal_details, personalFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_personal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.log_out) {
            BmobUser.logOut(this);   //清除缓存用户对象
            setResult(LOGOUT_RESULT_CODE);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
