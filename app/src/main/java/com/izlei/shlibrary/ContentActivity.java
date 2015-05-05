package com.izlei.shlibrary;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;


import android.text.TextUtils;
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
import com.izlei.shlibrary.app.AppController;

import com.izlei.shlibrary.bean.Book;
import com.izlei.shlibrary.bean.User;
import com.izlei.shlibrary.test.Login;
import com.izlei.shlibrary.test.Relations;
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
 * Created by zhouzili on 2015/3/24.
 */
public class ContentActivity extends Activity implements FindBook.IFindBookObserver{
    public static final int STATE_DONATION = 0;
    public static final int STATE_BORROWING = 1;
    public static final int STATE_SEENDBACK = 2;

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

    private static final String TAG = ContentActivity.class
            .getSimpleName();
   // private NetworkImageView imgNetWorkView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
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

                        book = new Book(site,title,original_price,selling_price,pictureURL,author,
                                publisher,publish_date,url);

                    }catch (JSONException e) {
                        ToastUtil.show("JSONException");
                        e.printStackTrace();
                    }
                    if (book != null) {
                        makeImageRequest();
                        book.setIsbn(isbn);
                        tvTitle.setText(book.getTitle());
                        tvAuthor.setText(book.getAuthor());
                        tvPublisher.setText(book.getPublisher());
                        tvIntroduction.setText(result);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makeImageRequest() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        // Loading image with placeholder and error image
        imageLoader.get(book.getPictureURL(), ImageLoader.getImageListener(
                imageView, R.drawable.ico_loading, R.drawable.ico_error));

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(book.getPictureURL());
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
                        currState = ContentActivity.STATE_DONATION;
                        if (!isAdded) {
                            findBook.findBookByIsbn(book.getIsbn());
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
                        currState = ContentActivity.STATE_SEENDBACK;
                        if (!isSendBack) {
                            findBook.findBookByIsbn(book.getIsbn());
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
                        currState = ContentActivity.STATE_BORROWING;
                        findBook.findBookByIsbn(book.getIsbn());
                        /**保存当前借阅*/
                        Relations relations = new Relations();
                        relations.saveCurrentBorrow(book);
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
        if (TextUtils.isEmpty(user.getObjectId())) {
            ToastUtil.show("Please Login!");
            ///////////
            return false;
        }
        return true;
    }

    @Override
    public void update(int flag, List<Book> book) {
        if (flag == FindBook.FIND_ITEM_SUCCESS) {
            UpdateBook updateBook = new UpdateBook(this);
            if (currState == ContentActivity.STATE_DONATION
                    || currState == ContentActivity.STATE_SEENDBACK)
            {
                updateBook.updateBookStock(book.get(0), +1);
            }
            if (currState == ContentActivity.STATE_BORROWING){
                updateBook.updateBookStock(book.get(0), -1);
            }
            isAdded = true;
        }
    }
}
