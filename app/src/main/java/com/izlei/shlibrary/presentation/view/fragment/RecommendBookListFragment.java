package com.izlei.shlibrary.presentation.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.model.BookModel;
import com.izlei.shlibrary.presentation.navigation.Navigator;
import com.izlei.shlibrary.presentation.presenter.Presenter;
import com.izlei.shlibrary.presentation.presenter.RecommendBookListPresenter;
import com.izlei.shlibrary.presentation.view.BookListView;
import com.izlei.shlibrary.presentation.view.adapter.BookLayoutManager;
import com.izlei.shlibrary.presentation.view.adapter.BooksAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhouzili on 2015/6/4.
 */
public class RecommendBookListFragment  extends BaseFragment implements BookListView {

    public static final String TAG = RecommendBookListFragment.class.getSimpleName();
    public static final int DEFAULT_FLAG = 1;
    public static final int LOAD_MORE_FLAG = 2;
    public static final int REFRESH_FLAG = 3;
    private int currentActionFlag = DEFAULT_FLAG;
    static Navigator navigator =new Navigator();


    private int previousTotal = 0; // The total number of items in the data set after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    /**
     * Interface for listening book list events.
     */
    public interface BookListListener {
        void onBookListListener(final BookModel bookModel);
    }

    RecommendBookListPresenter presenter;

    @InjectView(R.id.books_rv)
    RecyclerView rv_books;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.rl_retry)
    RelativeLayout rl_retry;
    @InjectView(R.id.rl_progress) RelativeLayout rl_progress;
    private boolean isRefreshing = false;//ÊÇ·ñË¢ÐÂÖÐ
    private boolean isLoadingMore = false;

    private BookLayoutManager bookLayoutManager;
    private BooksAdapter booksAdapter;

    private BookListListener bookListListener;

    public RecommendBookListFragment() {
        super();
    }


    public static RecommendBookListFragment newInstance() {
        return new RecommendBookListFragment();
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BookListListener) {
            this.bookListListener = (BookListListener) activity;
        }
        this.presenter = new RecommendBookListPresenter();
    }

    @Override public View onCreateView(LayoutInflater inflater,
                                       ViewGroup container,Bundle savedInstanceState){
        View fragmentView = inflater.inflate(R.layout.fragment_book_list, container,false);

        ButterKnife.inject(this, fragmentView);
        setupUI();
        return fragmentView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            if (this.booksAdapter == null && getActivity() != null) {
                this.booksAdapter = new BooksAdapter(getActivity());
                this.booksAdapter.setOnItemClickListener(onItemClickListener);
                this.rv_books.setAdapter(this.booksAdapter);
            }
            this.initialize();
            this.LoadBookList(0,DEFAULT_FLAG);   // 0 represent load book from first index£¨descending order load£©
        }
    }

    private void setupUI() {
        this.rv_books.setHasFixedSize(true);
        this.bookLayoutManager = new BookLayoutManager(getActivity());
        this.rv_books.setLayoutManager(this.bookLayoutManager);
        refresh();
        autoLoadedMore();
    }

    private void refresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing){
                    currentActionFlag = REFRESH_FLAG;
                    RecommendBookListFragment.this.LoadBookList(0,currentActionFlag);
                    isRefreshing = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            isRefreshing = false;
                        }
                    },6000);
                }
            }
        });
    }
    private void autoLoadedMore() {
        rv_books.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = bookLayoutManager.getItemCount();
                firstVisibleItem = bookLayoutManager.findFirstVisibleItemPosition();
                //Log.e(TAG, "visibleItemCount="+visibleItemCount+" totalItemCount="+totalItemCount+" firstVisibleItem"+firstVisibleItem+" previousTotal=="+previousTotal);
                if (totalItemCount < previousTotal) {
                    previousTotal = totalItemCount;
                }
                if (loading) {
                    if (totalItemCount > previousTotal+1) {
                        loading = false;
                        previousTotal = totalItemCount;
                        //Log.e(TAG, "loading--previousTotal=="+previousTotal);
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    currentActionFlag = LOAD_MORE_FLAG;
                    RecommendBookListFragment.this.LoadBookList(booksAdapter.getItemCount(), currentActionFlag);
                    loading = true;
                    //Log.e(TAG,"not loading");
                }
            }
        });
    }

    private void initialize() {
        this.presenter.setView(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.presenter.pause();
    }

    /**
     * Load books.
     */
    private void LoadBookList(int skipIndex, int flag) {
        this.presenter.loadBookList(skipIndex, flag);
    }

    @Override
    public void renderBookList(List<BookModel> booksList) {
        if (booksList != null) {

            handler.sendMessage(handler.obtainMessage(currentActionFlag,booksList));
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEFAULT_FLAG:
                    RecommendBookListFragment.this.booksAdapter.setBookList((List<BookModel>)msg.obj);
                    break;
                case REFRESH_FLAG:
                    RecommendBookListFragment.this.booksAdapter.refreshBooks((List<BookModel>)msg.obj);
                    swipeRefreshLayout.setRefreshing(false);
                    currentActionFlag = DEFAULT_FLAG;
                    break;
                case LOAD_MORE_FLAG:
                    RecommendBookListFragment.this.booksAdapter.addBook((List<BookModel>)msg.obj);
                    currentActionFlag = DEFAULT_FLAG;
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void viewBook(BookModel bookModel) {
        if (this.bookListListener != null) {
            this.bookListListener.onBookListListener(bookModel);
        }
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

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        this.LoadBookList(0,DEFAULT_FLAG);
    }


    private BooksAdapter.OnItemClickListener onItemClickListener =
            new BooksAdapter.OnItemClickListener() {
                @Override
                public void onBookItemClickListener(BookModel bookModel) {
                    navigator.navigationToBookDetails(getActivity(), bookModel.getIsbn13());
                   /* if (BookListFragment.this.presenter != null &&
                            bookModel != null) {
                        BookListFragment.this.presenter.onBookClicked(bookModel);
                    }*/
                }
            };
}
