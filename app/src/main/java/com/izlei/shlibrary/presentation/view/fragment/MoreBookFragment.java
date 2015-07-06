package com.izlei.shlibrary.presentation.view.fragment;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.data.net.ApiConnection;
import com.izlei.shlibrary.domain.executor.PostExecutionThread;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.view.BookListView;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhouzili on 2015/6/9.
 */
public class MoreBookFragment extends BaseFragment implements BookListView {
    public static String URL_STRING ="http://book.douban.com/";
    @InjectView(R.id.tv_web_content)
    TextView textView;

    String strWebContent;

    @InjectView(R.id.rl_retry)
    RelativeLayout rl_retry;
    @InjectView(R.id.rl_progress) RelativeLayout rl_progress;

    //PostExecutionThread postExecutionThread;

    public static MoreBookFragment newInstance() {
        return new MoreBookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more_book, container, false);
        ButterKnife.inject(MoreBookFragment.this, rootView);
        return rootView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // strWebContent = getStringWebContent(URL_STRING);
        textView.setMaxLines(10000);
        PageTask task = new PageTask();
        task.execute(URL_STRING);


    }

    

    private String getStringWebContent(String apiUrl) {
        String webContent = null;
        if (isThereInternetConnection()) {
            try {
                ApiConnection apiConnection = ApiConnection.createGET(apiUrl);
                FutureTask<String> ft= new FutureTask<>(apiConnection);
                new Thread(ft).start();
                webContent = ft.get();
            } catch (Exception e) {
                showToastMessage("request error");
                e.printStackTrace();
            }
        }else {
            showToastMessage("No Network!");
        }
        return webContent;
    }

    class PageTask extends AsyncTask<String, Integer, String> {

        public PageTask() {
            showLoading();
        }
        @Override
        protected String doInBackground(String... params) {
            strWebContent = getStringWebContent(URL_STRING);
            String regStr = "<div class=\"cover\">\\s*?<a\\s*?href=(\"|')([\\s\\S.]*?)(\"|')[\\s\\S.]*?>";
            Pattern pattern = Pattern.compile(regStr);
            Matcher matcher = pattern.matcher(strWebContent);
            String ISBNList = "";
            while(matcher.find())
            {
                String href = "";
                href = matcher.group(2);
                String bookDetail = getStringWebContent(href);
                regStr = "class=\"pl\">ISBN:</span>([\\s\\S.]*?)<";
                Pattern patternDetail = Pattern.compile(regStr);
                Matcher matcherDetail = patternDetail.matcher(bookDetail);
                while(matcherDetail.find())
                    ISBNList += matcherDetail.group(1) + "\n";
            }
            return ISBNList;
        }
        @Override
        protected void onPostExecute(String result) {
            hideLoading();

            textView.setText(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    private boolean isThereInternetConnection() {
        boolean isConnected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected  = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

    @Override
    public void renderBookList(List<BookModel> booksList) {

    }

    @Override
    public void viewBook(BookModel bookModel) {

    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            this.getActivity().setProgressBarIndeterminateVisibility(true);
        }
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        if (getActivity() != null) {
            this.getActivity().setProgressBarIndeterminateVisibility(false);
        }
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
        this.showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }
}
