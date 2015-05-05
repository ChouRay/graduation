package com.izlei.shlibrary.demo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.izlei.shlibrary.demo.activity.MainActivity;
import com.izlei.shlibrary.R;
import com.izlei.shlibrary.demo.adapter.CustomListAdapter;
import com.izlei.shlibrary.domain.Book;
import com.izlei.shlibrary.demo.FindBook;
import com.izlei.shlibrary.demo.RefreshableView;

import java.util.List;

/**
 *
 * Fragment that appears in the "content_frame", shows a content
 * Created by zhouzili on 2015/4/9.
 */
public class ContentFragment extends Fragment implements FindBook.IFindBookObserver,
        AbsListView.OnScrollListener{

    public static final String ARG_NAVIGATION_NUMBER = "navigation_number";
    FindBook find = null;
    RefreshableView refreshableView;
    private ListView contentListView;
    private View loadMoreView;
    private Button loadMoreBtn;
    private CustomListAdapter customListAdapter;

    private int visibleLastIndex = 0;
    int visibleItemCount;////////??
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_content, container, false);
        int i = getArguments().getInt(ARG_NAVIGATION_NUMBER);
        String title = getResources().getStringArray(R.array.navigation_array)[i];
        switch(i) {
            case MainActivity.NAV_FIRST_CODE:
                find = new FindBook(getActivity());
                find.addObserver(this);
                find.findALl();
                contentListView = (ListView) rootView.findViewById(R.id.content_listview);
                refreshableView = (RefreshableView) rootView.findViewById(R.id.refreshable_view);
                loadMoreView = inflater.inflate(R.layout.load_more,null);
                loadMoreBtn = (Button) loadMoreView.findViewById(R.id.loadmore_btn);
                contentListView.addFooterView(loadMoreView);
                contentListView.setOnScrollListener(this);

                /*加载更多*/
                loadMoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadMoreBtn.setText(getResources().getString(R.string.loading));
                        int count = customListAdapter.getCount();
                        find.skipFind(count);
                    }
                });

                // 将监听滑动事件绑定在listView上
                refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
                    @Override
                    public void onRefresh() {
                        find.findNew();
                        refreshableView.finishRefreshing();
                    }
                },0);
                break;
            default:
                break;
        }
        getActivity().setTitle(title);
        return rootView;
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = customListAdapter.getCount() - 1;
        int lastIndex = itemsLastIndex +1;
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex)
        {
            Log.e("", "Loading...");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount -1;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RefreshableView.STATUS_REFRESH_FINISHED:
                    customListAdapter.refresh((List<Book>) msg.obj);
                    break;
                case FindBook.FIND_SKIP_SUCCESS:
                    customListAdapter.notifyDataSetChanged();
                    loadMoreBtn.setText(getResources().getString(R.string.load_more));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void update(int flag, List<?> book) {
        switch (flag) {
            case FindBook.FIND_ALL_SUCCESS:
                if (customListAdapter == null) {
                    customListAdapter = new CustomListAdapter(getActivity(),(List<Book>)book);
                    contentListView.setAdapter(customListAdapter);
                }
                handler.sendMessage(handler.obtainMessage(RefreshableView.STATUS_REFRESH_FINISHED, book));
                break;
            case FindBook.FIND_SKIP_SUCCESS:
                for (int i=0; i<book.size();i++){
                    customListAdapter.addItem((Book)book.get(i));
                }
                handler.sendMessage(handler.obtainMessage(FindBook.FIND_SKIP_SUCCESS));
                break;
            case FindBook.FIND_NEW_SUCCESS:
                if (customListAdapter == null) {
                    customListAdapter = new CustomListAdapter(getActivity(),(List<Book>)book);
                    contentListView.setAdapter(customListAdapter);
                }
                handler.sendMessage(handler.obtainMessage(RefreshableView.STATUS_REFRESH_FINISHED, book));
                break;
            default:
                break;
        }
    }
}
