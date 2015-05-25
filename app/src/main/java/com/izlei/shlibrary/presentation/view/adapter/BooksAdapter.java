package com.izlei.shlibrary.presentation.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.presentation.model.BookModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhouzili on 2015/4/20.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    public interface OnItemClickListener {
        void onBookItemClickListener(BookModel bookModel);
    }

    private Context context;
    private List<BookModel> bookModelList;
    private final LayoutInflater layoutInflater;

    private ImageLoader imageLoader;

    private OnItemClickListener onItemClickListener;

    public BooksAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.imageLoader =  AppController.getInstance().getImageLoader();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view  = this.layoutInflater.inflate(R.layout.book_list_item, viewGroup, false);
        return  new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, int i) {
        final BookModel bookModel = this.bookModelList.get(i);

        this.makeImageRequest(bookViewHolder, bookModel.getImage());
        bookViewHolder.title.setText(bookModel.getTitle());
        String author = "";
        for (int j=0; j<bookModel.getAuthor().size(); j++) {
            author += bookModel.getAuthor().get(j) + " ";
        }
        bookViewHolder.author.setText(author);
        bookViewHolder.stock.setText(context.getResources().getString(R.string.number)+
                String.valueOf(bookModel.getStock()));
        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BooksAdapter.this.onItemClickListener != null) {
                    BooksAdapter.this.onItemClickListener.onBookItemClickListener(bookModel);
                }
            }
        });
    }

    private void makeImageRequest(BookViewHolder bookViewHolder,String url) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                // handle data, like converting it to xml, json, bitmap etc.,
                Bitmap bitmap = BitmapFactory.decodeByteArray(entry.data,0,entry.data.length);
                bookViewHolder.thumbnail.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            // Loading image with placeholder and error image
            imageLoader.get(url, ImageLoader.getImageListener(bookViewHolder.thumbnail
                    , R.drawable.ico_loading, R.drawable.ico_error));

            // cached response doesn't exists. Make a network call here
            //Log.e(getClass().getSimpleName(), " cached response doesn't exists. Make a network call here");
        }
    }


    @Override
    public int getItemCount() {
        return (this.bookModelList != null) ? this.bookModelList.size() : 0;
    }
    @Override public long getItemId(int position) {
        return position;
    }

    public void setBookList(List<BookModel> booksList) {
        this.validateBooksList(booksList);
        this.bookModelList = booksList;
        this.notifyDataSetChanged();
    }

    public void refreshBooks(List<BookModel> booksList) {
        this.validateBooksList(booksList);
        if (bookModelList != null) {
            this.bookModelList.addAll(0, booksList);
            this.notifyDataSetChanged();
        }
    }

    public void addBook(List<BookModel> booksList) {
        for (BookModel bookModel : booksList) {
            this.bookModelList.add(bookModel);
        }
        this.notifyDataSetChanged();
    }


    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateBooksList(List<BookModel> bookModelList) {
        if (bookModelList == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    /**
     *
     */
    static class BookViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.img_book_thumbnail) ImageView thumbnail;
        @InjectView(R.id.tv_book_title) TextView title;
        @InjectView(R.id.tv_book_author) TextView author;
        @InjectView(R.id.tv_book_stock) TextView stock;
        /**
         * @param itemView
         */
        public BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
