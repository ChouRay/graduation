package com.izlei.shlibrary.demo.activity;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;

import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.demo.impl.IMyListener;
import com.izlei.shlibrary.demo.Login;
import com.izlei.shlibrary.demo.fragment.ContentFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MainActivity extends ActionBarActivity implements View.OnClickListener,IMyListener {

	public final static String TAG="MainActivity";
    /*扫描请求代码*/
    public final static int SCANNIN_REQUEST_CODE = 5;
    /*个人中心请求代码*/
    public final static int PERSONAL_REQUEST_CODE = 6;
    /*注册请求代码*/
    public final static int SIGNUP_REQUEST_CODE = 7;
    /*登录请求代码*/
    public final static int LOGIN_REQUEST_CODE = 8;

    /*导航首页代码*/
    public final static int NAV_FIRST_CODE = 0;
    /*导航收藏代码*/
    public final static int NAV_SECOND_CODE = 1;
    /*导航搜索代码*/
    public final static int NAV_THREE_CODE = 2;
    /*导航下载代码*/
    public final static int NAV_FOUR_CODE = 3;
    /*导航作者代码*/
    public final static int NAV_FIVE_CODE = 4;
    ///////////////////////////////////////////////////

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    /**左侧抽屉视图中的导航listview*/
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    /**头像跟姓名所在Layout*/
    private FrameLayout drawerFrameLayout;

    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] navTitles;
    private AlertDialog loginDialog;
    private EditText editName;
    private EditText editPassword;

    private User user;  //用户
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = drawerTitle = getTitle();
        initDrawerLayout();
        if (savedInstanceState == null) {
            selectItem(MainActivity.NAV_FIRST_CODE);
        }
         /* get user cache to login*/
        user  = BmobUser.getCurrentUser(AppController.getInstance(), User.class);
        if (user != null) {
            setAvatarName(user.getUsername());
        }
    }

    private void initDrawerLayout() {
        navTitles = getResources().getStringArray(R.array.navigation_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer_list);
        drawerFrameLayout = (FrameLayout) findViewById(R.id.drawer_framelayout);

        LinearLayout avatarLayout = (LinearLayout) findViewById(R.id.avatar_layout);

        avatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    loginDialog(context);
                }else {
                    startPersonalActivity();
                }
            }
        });

        /*item's images and text of navigation in Drawerlayout*/
        int[] itemImages = new int[] { R.mipmap.ic_action_home,
                R.mipmap.ic_action_favorite,R.mipmap.ic_action_communiction,
                R.mipmap.ic_action_find, R.mipmap.ic_action_search,
                R.mipmap.ic_action_aboutme
        };
        List<String> itemTexts = new ArrayList<>();
        for (int i=0; i<getResources().getStringArray(R.array.navigation_array).length; i++) {
            itemTexts.add(getResources().getStringArray(R.array.navigation_array)[i]);
        }
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i=0; i< 6; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItemImage", itemImages[i]);
            map.put("ItemText", itemTexts.get(i));
            data.add(map);
        }

        // 动态数组数据源中与ListItem中每个显示项对应的Key
        String[] from = new String[] {"ItemImage", "ItemText"};
        // ListItem的XML文件里面的一个ImageView ID和TextView ID
        int[] to = new int[] { R.id.item_image, R.id.item_text };

        // 将动态数组数据源data中的数据填充到ListItem的XML文件drawer_list_item.xml中去
        // 从动态数组数据源data中，取出from数组中key对应的value值，填充到to数组中对应ID的控件中去
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.drawer_list_item, from,to);
        // set up the drawer's list view with items and click listener
        drawerList.setAdapter(adapter);

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,                /*Toolbar */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }


    /*进入个人中心*/
    private void startPersonalActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, PersonalActivity.class);
        startActivityForResult(intent, MainActivity.PERSONAL_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onListenerMessage(Object obj, int flag) {
        if (flag == Login.LOGIN_SUCCESS) {
            user = (User) obj;
            setAvatarName(user.getUsername());
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

            if (parent.getTag() != null) {
                ((View)parent.getTag()).setBackgroundResource(0);
            }
            parent.setTag(view);
            view.setBackgroundColor(getResources().getColor(R.color.light_blue));
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(ContentFragment.ARG_NAVIGATION_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        drawerList.setItemChecked(position, true);
        getSupportActionBar().setTitle(navTitles[position]);
        closeDrawer();
    }

    /**关闭左侧的抽屉视图*/
    public void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(drawerFrameLayout);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getSupportActionBar().setTitle(this.title);
    }
    /**
     *
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_scan) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MyCaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, MainActivity.SCANNIN_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    /*弹出用户登录框*/
    public void loginDialog(Context context) {

        final LayoutInflater inflater =getLayoutInflater();
        final View view = inflater.inflate(R.layout.log_in, (ViewGroup) findViewById(R.id.dialog_user));
        loginDialog = new AlertDialog.Builder(context)
                .setTitle("登录")
                .setView(view).show();

        editName = (EditText) view.findViewById(R.id.edit_name);
        editPassword = (EditText) view.findViewById(R.id.edit_password);
        editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Button loginBtn = (Button) view.findViewById(R.id.button_log_in);
        Button signUpBtn = (Button) view.findViewById(R.id.button_sign_up);
        Button cancelBtn = (Button) view.findViewById(R.id.button_cancel);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_log_in:
                Login login = new Login();
                login.setListener(this);
                login.login(editName.getText().toString(), editPassword.getText().toString());
                loginDialog.cancel();
                break;
            case R.id.button_sign_up:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SignUpActivity.class);
                startActivityForResult(intent,MainActivity.SIGNUP_REQUEST_CODE);
                loginDialog.cancel();
                break;
            case R.id.button_cancel:
                loginDialog.cancel();
                break;
            default:
                break;
        }
    }


    /*设置头像旁边的用户名*/
    public void setAvatarName(String name) {
        TextView userName = (TextView) findViewById(R.id.avatar_text);
        userName.setText(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)  {
        switch (resultCode) {
            case MyCaptureActivity.SCANED_SUCCESS_CODE:
                String result = null;
                if (data != null) {
                    result = data.getExtras().getString("result");
                }
                if (result != null) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ISBN", result);
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, DetailsActivity.class);
                    startActivityForResult(intent, MainActivity.LOGIN_REQUEST_CODE);
                }
                break;
            case PersonalActivity.LOGOUT_RESULT_CODE:
                 /*注销用户成功*/
                user = null;
                setAvatarName(getResources().getString(R.string.unlogin));
                break;
            case SignUpActivity.SIGNUP_SUCCESS_CODE:
            //if sign up success, log in immediately
                if (data != null) {
                    user = (User)data.getExtras().get("User");
                    setAvatarName(user.getUsername());
                }
                break;
            case Login.LOGIN_FAILURE:
                /*请求登录*/
                loginDialog(context);
                break;
            default:
                break;
        }
    }

}
