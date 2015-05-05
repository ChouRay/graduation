package com.izlei.shlibrary.demo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;

import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.demo.CreateBook;
import com.izlei.shlibrary.demo.FindBook;
import com.izlei.shlibrary.demo.UpdateBook;
import com.izlei.shlibrary.demo.Login;
import com.izlei.shlibrary.demo.Relations;
import com.izlei.shlibrary.utils.ToastUtil;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * 扫描的书的细节
 * Created by zhouzili on 2015/3/24.
 */
public class DetailsActivity extends Activity implements FindBook.IFindBookObserver{
    public static final int STATE_DONATION = 30;
    public static final int STATE_BORROWING = 31;
    public static final int STATE_SEENDBACK = 32;

    private int currState = -1;
    CreateBook createBook;
    FindBook findBook;
    TextView tvPublisher = null;
    TextView tvAuthor = null;
    TextView tvTitle = null;
    TextView tvIntroduction = null;
    Book book;
    boolean isAdded = false;
    boolean isSendBack = false;
    boolean isEnable = false;
    private Context context;

    private static final String TAG = DetailsActivity.class
            .getSimpleName();
   // private NetworkImageView imgNetWorkView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = DetailsActivity.this;
        findBook = new FindBook(this);
        findBook.addObserver(this);
        createBook = new CreateBook(this);

        imageView = (ImageView) findViewById(R.id.imgview);
        tvTitle = (TextView) findViewById(R.id.title);
        tvAuthor = (TextView) findViewById(R.id.author);
        tvPublisher = (TextView) findViewById(R.id.publisher);
        tvIntroduction = (TextView) findViewById(R.id.tv_introduction);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.details));
        setOverflowShowingAlways();

        Parameters params = new Parameters();
        params.add("ip", "http://v.juhe.cn/ebook/isbn");
        params.add("dtype", "json");


        Bundle bundle  = getIntent().getExtras();

        final String isbn =  bundle.getString("ISBN");

        final String isbnUrl = "http://v.juhe.cn/ebook/isbn?key=7a9ee51d4c305b5c43c97bef1216a057&isbn="+isbn;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        /**
         * 请求的方法
         * 参数:
         * 第一个参数  接口id
         * 第二个参数  接口请求的url
         * 第三个参数  接口请求的方式
         * 第四个参数  接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型;
         * 第五个参数  请求的回调方法,com.thinkland.sdk.android.DataCallBack;
         *
         */
        JuheData.executeWithAPI(136, isbnUrl, JuheData.POST, params, new DataCallBack() {

            /**
             * @param err
             *            错误码,0为成功
             * @param reason
             *            原因
             * @param result
             *            数据
             */
            @Override
            public void resultLoaded(int err, String reason, String result) {
                // TODO Auto-generated method stub
                if (err == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray ja = jsonObject.getJSONArray("result");

                        String site = ja.getJSONObject(0).getString("site");
                        String title = ja.getJSONObject(0).getString("title");
                        String original_price = ja.getJSONObject(0).getString("original_price");
                        String selling_price = ja.getJSONObject(0).getString("selling_price");
                        String pictureURL = ja.getJSONObject(0).getString("picture");

                        String author = ja.getJSONObject(0).getString("author");
                        String publisher = ja.getJSONObject(0).getString("publisher");

                        String publish_date = ja.getJSONObject(0).getString("publish_date");
                        String url = ja.getJSONObject(0).getString("url");



                    }catch (JSONException e) {
                        ToastUtil.show("JSONException");
                        e.printStackTrace();
                    }
                    if (book != null) {
                        makeImageRequest();
                        book.setIsbn13(isbn);
                        tvTitle.setText(book.getTitle());
                        String author = "";
                        for (int j=0; j<book.getAuthor().size(); j++) {
                            author += book.getAuthor().get(j) + " ";
                        }
                        tvAuthor.setText(author);
                        tvPublisher.setText(book.getPublisher());
                        tvIntroduction.setText(result);
                    }
                    pDialog.hide();
                } else {
                    pDialog.hide();
                    Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makeImageRequest() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        // Loading image with placeholder and error image
        imageLoader.get(book.getImage(), ImageLoader.getImageListener(
                imageView, R.drawable.ico_loading, R.drawable.ico_error));

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(book.getImage());
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                Log.e(TAG, "data=="+data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            // cached response doesn't exists. Make a network call here
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_donate:
                if (isLogin())
                {
                    if (book != null) {
                        currState = DetailsActivity.STATE_DONATION;
                        if (!isAdded) {
                            findBook.findBookByIsbn(book.getIsbn13());
                            if (!isAdded) {
                                createBook.addBook(book);
                            }
                        }else {
                            ToastUtil.show("此书已经添加成功！");
                        }
                    }
                }
                return true;
            case R.id.action_return:
                if (isLogin())
                {
                    if (book != null) {
                        currState = DetailsActivity.STATE_SEENDBACK;
                        if (!isSendBack) {
                            findBook.findBookByIsbn(book.getIsbn13());
                            isSendBack = true;

                        }else {
                            ToastUtil.show("此书已经退还成功！");
                        }
                    }
                }
                return true;
            case R.id.action_borrow:
                if (isLogin())
                {
                    if (book != null) {
                        currState = DetailsActivity.STATE_BORROWING;
                        findBook.findBookByIsbn(book.getIsbn13());
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isLogin() {
        User user = BmobUser.getCurrentUser(AppController.getInstance(), User.class);
        if (user == null) {
            ToastUtil.show("Please Login!");

            this.setResult(Login.LOGIN_FAILURE);
            DetailsActivity.this.finish();
            ///////////
            return false;
        }
        return true;
    }

    @Override
    public void update(int flag, List<?> books) {
        if (flag == FindBook.FIND_ITEM_SUCCESS ) {
            UpdateBook updateBook = new UpdateBook(this);
            if (currState == DetailsActivity.STATE_DONATION)
            {
                updateBook.updateBookStock((Book)books.get(0), +1);
            }
            if (currState == DetailsActivity.STATE_SEENDBACK) {
                if (books.size() != 0) {
                    updateBook.updateBookStock((Book)books.get(0), +1);
                }else {
                    ToastUtil.show("这本书还没入库呢，可以先捐了它！");
                }

            }
            if (currState == DetailsActivity.STATE_BORROWING){
                if (books.size() != 0) {
                    /*将此书与用户关联*/
                    Relations relations = new Relations();
                    relations.saveCurrentBorrow(this.book);
                    updateBook.updateBookStock((Book)books.get(0), -1);  //此书数量减去1
                }else {
                    ToastUtil.show("此书还没入库呢！");
                }

            }
            isAdded = true;
        }
    }
}
