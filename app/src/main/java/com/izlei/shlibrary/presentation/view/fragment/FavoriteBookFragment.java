package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.navigation.Navigator;
import com.izlei.shlibrary.presentation.presenter.FavoriteBookListPresenter;
import com.izlei.shlibrary.presentation.view.BookListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhouzili on 2015/5/18.
 */
public class FavoriteBookFragment extends BaseFragment implements BookListView,AdapterView.OnItemClickListener {

    FavoriteBookListPresenter presenter;
    private BookModel bookModel;
    private List<BookModel> bookModelList;
    private MyAdapter adapter;

    Navigator navigator;
    @InjectView(R.id.list_favorite)
    ListView contentListView;

    @InjectView(R.id.rl_retry)
    RelativeLayout rl_retry;
    @InjectView(R.id.rl_progress)
    RelativeLayout rl_progress;

    public static FavoriteBookFragment newInstance() {
        return new FavoriteBookFragment();
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.presenter = new FavoriteBookListPresenter();
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       ViewGroup container,Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.inject(FavoriteBookFragment.this, fragmentView);
        return fragmentView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (adapter == null) {
            adapter = new MyAdapter();
            contentListView.setAdapter(adapter);
            contentListView.setOnItemClickListener(this);
        }
        this.initialize();
        this.LoadBookList();
    }
    private void initialize() {
        this.presenter.setView(this);
    }

    private void LoadBookList() {
        this.presenter.loadBookList();
    }

    @OnClick(R.id.bt_retry)
    public void onRetryClick() {
        LoadBookList();
    }

    @Override
    public void renderBookList(List<BookModel> booksList) {
        this.bookModelList = booksList;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (navigator == null) {
            navigator =new Navigator();
        }
        navigator.navigationToBookDetails(getActivity(), bookModelList.get(position).getIsbn13());
    }

    @Override
    public void viewBook(BookModel bookModel) {

    }

    private class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        @Override
        public int getCount() {
            return (bookModelList != null) ? bookModelList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return bookModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BookModel bookModel = bookModelList.get(position);
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            }else {
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_favorite_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            makeImageRequest(holder, bookModel.getImage());
            holder.textViewTitle.setText(bookModel.getTitle());
            holder.textViewPrice.setText(String.format(getResources().getString(R.string.book_price),bookModel.getPrice()));

            return convertView;
        }
    }

    private void makeImageRequest(ViewHolder bookViewHolder,String url) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                // handle data, like converting it to xml, json, bitmap etc.,
                Bitmap bitmap = BitmapFactory.decodeByteArray(entry.data, 0, entry.data.length);
                bookViewHolder.imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            // Loading image with placeholder and error image
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageLoader.get(url, ImageLoader.getImageListener(bookViewHolder.imageView
                    , R.drawable.ico_loading, R.drawable.ico_error));

            // cached response doesn't exists. Make a network call here
            //Log.e(getClass().getSimpleName(), " cached response doesn't exists. Make a network call here");
        }
    }

    class ViewHolder{
        @InjectView(R.id.iv_favorite)
        ImageView imageView;
        @InjectView(R.id.tv_book_title)
        TextView textViewTitle;
        @InjectView(R.id.tv_book_price)
        TextView textViewPrice;
        public ViewHolder(View view) {
            ButterKnife.inject(this,view);
        }

    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
