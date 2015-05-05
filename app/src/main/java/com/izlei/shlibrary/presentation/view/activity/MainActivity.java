package com.izlei.shlibrary.presentation.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.navigation.Navigator;
import com.izlei.shlibrary.presentation.view.fragment.BookListFragment;
import com.izlei.shlibrary.demo.activity.MyCaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhouzili on 2015/4/24.
 */
public class MainActivity extends BaseActivity{
    /*导航首页代码*/
    public final static int NAV_FIRST_CODE = 0;

    /*扫描请求代码*/
    public final static int SCANNING_REQUEST_CODE = 5;

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @InjectView(R.id.left_drawer_list) ListView drawerList;
    @InjectView(R.id.drawer_framelayout) FrameLayout drawerFrameLayout;
    private String[] navTitles;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;

    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        title = drawerTitle = getTitle();
        this.initDrawerLayout();
        if (savedInstanceState == null) {
            selectItem(MainActivity.NAV_FIRST_CODE);
        }
    }

    private void initDrawerLayout() {
        navTitles = getResources().getStringArray(R.array.navigation_array);
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
        this.setDrawerToggle();

    }

    private void setDrawerToggle() {
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
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
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

    private void selectItem(int position) {
        this.addFragment(R.id.content_frame, BookListFragment.newInstance());
        // update selected item and title, then close the drawer
        drawerList.setItemChecked(position, true);
        drawerTitle = navTitles[position];
        //getSupportActionBar().setTitle(navTitles[position]);
        closeDrawer();
        setTitle(drawerTitle);
    }

    /***
     * Login or into personal central
     */
    @OnClick(R.id.avatar_layout)
    public void onClick() {
        
    }

    /**
     * close to left drawer
     */
    public void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(drawerFrameLayout);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_scan) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MyCaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, MainActivity.SCANNING_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {

        if (resultCode == MyCaptureActivity.SCANED_SUCCESS_CODE) {
            String result = null;
            if (data != null) {
                result = data.getExtras().getString("result");
            }
            if (result != null) {
                if (navigator == null) {
                    navigator = new Navigator();
                }
                navigator.navigationToBookDetails(MainActivity.this,result);
            }
        }

    }

}
