package com.izlei.shlibrary.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.domain.Book;

import java.util.List;

/**
 * Created by zhouzili on 2015/3/27.
 */
public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Book> bookItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity,List<Book> bookItems) {
        this.activity = activity;
        this.bookItems = bookItems;
    }

    public void refresh(List<Book> items) {
        this.bookItems = items;
        notifyDataSetChanged();
    }

    public void addItem(Book item) {
        bookItems.add(item);
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

        thumbnail.setImageUrl(bookItems.get(position).getImage(), imageLoader);
        title.setText(bookItems.get(position).getTitle());
        String authorStr = "";
        for (int j=0; j<bookItems.size(); j++) {
            authorStr += bookItems.get(j).getAuthor() + " ";
        }
        author.setText(authorStr);
        stock.setText(activity.getResources().getString(R.string.number)+String.valueOf(bookItems.get(position).getStock()));
        return convertView;
    }
}
