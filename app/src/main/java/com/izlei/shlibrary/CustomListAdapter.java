package com.izlei.shlibrary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.com.izlei.app.AppController;
import com.izlei.shlibrary.Book;
import com.izlei.shlibrary.R;

import java.net.NetworkInterface;
import java.util.List;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Book> bookItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Book> bookItems) {
        this.activity = activity;
        this.bookItems = bookItems;
    }

    @Override
    public int getCount() {
        return bookItems.size();
    }

    @Override
    public Object getItem(int position) {
        return bookItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        NetworkImageView thumbnail = (NetworkImageView) convertView.findViewById(R.id.img_book_thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.tv_book_title);
        TextView author = (TextView) convertView.findViewById(R.id.tv_book_author);
        TextView stock = (TextView) convertView.findViewById(R.id.tv_book_stock);

        Book book = bookItems.get(position);
        thumbnail.setImageUrl(book.getPictureURL(), imageLoader);
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        stock.setText("库存:"+String.valueOf(book.getStock()));
        return convertView;
    }
}
