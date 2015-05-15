package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.izlei.shlibrary.presentation.presenter.BookDetailsPresenter;
import com.izlei.shlibrary.presentation.view.BookDetailsView;
import com.izlei.shlibrary.utils.ToastUtil;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhouzili on 2015/5/3.
 */
public class BookDetailsFragment extends BaseFragment implements BookDetailsView{

    @InjectView(R.id.imageView_book) ImageView imageViewBook;
    @InjectView(R.id.textView_title) TextView textViewTitle;
    @InjectView(R.id.textView_author) TextView textViewAuthor;
    @InjectView(R.id.textView_publisher) TextView textViewPublisher;
    @InjectView(R.id.textView_pubdate) TextView textViewPubdate;
    @InjectView(R.id.textView_summary) TextView textViewSummary;
    @InjectView(R.id.textView_author_intro) TextView textViewAuthorIntro;
    @InjectView(R.id.textView_catalog) TextView textViewCatalog;

    boolean isSummaryExpended = false;
    boolean isAuthorIntroExpended = false;
    boolean isCatalogExpended = false;

    private BookModel bookModel;
    private Context context;

    private BookDetailsPresenter bookDetailsPresenter;

    public static BookDetailsFragment newInstance() {
        return new BookDetailsFragment();
    }

    public BookDetailsFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        this.bookDetailsPresenter = new BookDetailsPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_details, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();

        String isbn = getArguments().getString("ISBN");
        if (isbn!= null) {
            this.bookDetailsPresenter.LoadBookDetails(isbn);
        }
    }

    private void initialize() {
        this.bookDetailsPresenter.setView(this);
    }

    @OnClick(R.id.button_donation)
    public void donate() {
        if (bookModel != null) {
            this.bookDetailsPresenter.createBookIntoRepository(bookModel);
        }
    }
    @OnClick(R.id.button_borrow)
    public void borrow() {

    }
    @OnClick(R.id.button_sendBack)
    public void sendBack() {

    }


    @OnClick(R.id.frameLayout_summary)
    public void summary() {
        if (!isSummaryExpended) {
            textViewSummary.setMaxLines(100);
            isSummaryExpended = true;
        }else {
            textViewSummary.setMaxLines(4);
            isSummaryExpended = false;
        }
    }
    @OnClick(R.id.frameLayout_author_intro)
    public void authorIntro() {
        if (!isAuthorIntroExpended) {
            textViewAuthorIntro.setMaxLines(100);
            isAuthorIntroExpended = true;
        }else {
            textViewAuthorIntro.setMaxLines(3);
            isAuthorIntroExpended = false;
        }
    }
    @OnClick(R.id.frameLayout_catalog)
    public void catalog() {
        if (!isCatalogExpended) {
            textViewCatalog.setMaxLines(500);
            isCatalogExpended = true;
        }else {
            textViewCatalog.setMaxLines(4);
            isCatalogExpended = false;
        }
    }


    private void makeImageRequest(ImageView imageView,String url) {
        // Loading image with placeholder and error image
        AppController.getInstance().getImageLoader().get(url, ImageLoader.getImageListener(imageView
                , R.drawable.ico_loading, R.drawable.ico_error));

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            // cached response doesn't exists. Make a network call here
            Log.e(getClass().getSimpleName(), " cached response doesn't exists. Make a network call here");
        }
    }

    @Override
    public void renderBookDetails(BookModel bookModel) {
        this.bookModel = bookModel;
        this.makeImageRequest(imageViewBook, bookModel.getImage());
        this.textViewTitle.setText(bookModel.getTitle());
        String author = "";
        for (int j=0; j<bookModel.getAuthor().size(); j++) {
            author += bookModel.getAuthor().get(j) + " ";
        }
        this.textViewAuthor.setText(author);
        this.textViewPublisher.setText(bookModel.getPublisher());
        this.textViewPubdate.setText(bookModel.getPubdate());
        this.textViewSummary.setText(bookModel.getSummary());
        this.textViewAuthorIntro.setText(bookModel.getAuthor_intro());
        this.textViewCatalog.setText(bookModel.getCatalog());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getContext() {
        return this.context;
    }
}

