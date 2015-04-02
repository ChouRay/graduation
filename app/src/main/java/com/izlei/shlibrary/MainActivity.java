package com.izlei.shlibrary;


import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.com.izlei.app.AppController;
import com.izlei.shlibrary.utils.ToastUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class MainActivity extends ActionBarActivity implements FindBook.IFindBookObserver{

	private final static String TAG="MainActivity";
    private final static int SCANNIN_GREQUEST_CODE = 1;
    String url;

    private ProgressDialog pDialog;

    /**
     * 显示扫描结果
     */
    private TextView mTextView ;
    /**
     * 显示扫描拍的图片
     */
    private ImageView mImageView;


    ///////////////////////////////////////////////////
    /**
     * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
     */
    private ThreeDSlidingLayout slidingLayout;

    private List<Book> bookList;
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "4fca2c82deb6bc1ac889f5bab7b2fa13");
//////////////////////////////////////////////////////////////////////////////////
        slidingLayout = (ThreeDSlidingLayout) findViewById(R.id.slidingLayout);
        bookList = new ArrayList<>();

        FindBook find = new FindBook();
        find.addObserver(MainActivity.this);

        find.findALl();

        listView = (ListView) findViewById(R.id.contentList);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case android.R.id.home:
                if (slidingLayout.isLeftLayoutVisible()) {
                    slidingLayout.scrollToRightLayout();
                } else {
                    slidingLayout.scrollToLeftLayout();
                }
                return true;
            case R.id.action_scan:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)  {
        String result = data.getExtras().getString("result");
        if (result != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("ISBN", result);
            intent.putExtras(bundle);
            intent.setClass(MainActivity.this, ContentActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void update(int flag, List<Book> book) {
        switch (flag) {
            case FindBook.FIND_SUCESS:
                bookList = book;
                adapter = new CustomListAdapter(this, bookList);
                listView.setAdapter(adapter);
               // adapter.notifyDataSetChanged();


                break;
            default:
                break;
        }
    }
}
