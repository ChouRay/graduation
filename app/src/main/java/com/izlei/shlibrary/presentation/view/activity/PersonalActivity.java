package com.izlei.shlibrary.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.izlei.shlibrary.presentation.view.fragment.PersonalFragment;
import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.domain.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by zhouzili on 2015/4/10.
 */
public class PersonalActivity extends BaseActivity{

    public static final int LOGOUT_RESULT_CODE = 12;
    public static final int CURRENT_BORROWED = 20;
    public static final int  HISTORY_BORROWED = 21;
    public static final int HISTORY_DONATION = 22;

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

        final User user = BmobUser.getCurrentUser(AppController.getInstance(), User.class);
        TextView nameText = (TextView) findViewById(R.id.username_text);
        TextView addressText = (TextView) findViewById(R.id.useraddress_text);
        if (user != null) {
            nameText.setText(user.getUsername());
            addressText.setText(user.getAddress());
        }

        LinearLayout currBorrowLayout = (LinearLayout) findViewById(R.id.current_borrow_layout);
        currBorrowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalFragment fragment = new PersonalFragment();
                Bundle args = new Bundle();
                args.putInt(PersonalFragment.ARG_SELECTION_NUMBER, PersonalActivity.CURRENT_BORROWED);
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.personal_detail_framelayout, fragment).commit();

            }
        });
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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
