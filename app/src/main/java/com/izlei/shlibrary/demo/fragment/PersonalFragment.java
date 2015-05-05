package com.izlei.shlibrary.demo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.domain.CurrentBorrow;
import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.demo.FindBook;
import com.izlei.shlibrary.demo.activity.PersonalActivity;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**个人中心 包括当前借阅，借阅历史，捐赠历史
 * Created by zhouzili on 2015/4/11.
 */
public class PersonalFragment extends Fragment implements FindBook.IFindBookObserver{

    public static final String ARG_SELECTION_NUMBER = "selection_number";
    FindBook find = null;
    private ListView contentListView;
    CustomAdapter adapter = null;
    List<CurrentBorrow> currentBorrows;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.personal_details_list, container, false);
        int i = getArguments().getInt(ARG_SELECTION_NUMBER);

        if (i == PersonalActivity.CURRENT_BORROWED) {
            find = new FindBook(getActivity());
            find.addObserver(this);
            find.findCurrentBorrows(BmobUser.getCurrentUser(AppController.getInstance(), User.class));
            contentListView = (ListView) rootView.findViewById(R.id.list_personal_details);

        }
        return rootView;
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentBorrows.size();
        }

        @Override
        public Object getItem(int position) {
            return currentBorrows.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null) {
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.personal_detaial, null);
            }
            TextView title = (TextView) convertView.findViewById(R.id.tv_book_title);
            TextView borrowedDate = (TextView) convertView.findViewById(R.id.tv_borrowed_date);
            TextView sendbackDate = (TextView) convertView.findViewById(R.id.tv_book_sendback_date);

            title.setText(currentBorrows.get(position).getTitle());
            borrowedDate.setText(
                    String.format(getResources().getString(R.string.borrowed_date),
                            currentBorrows.get(position).getBorrowDate()));
            sendbackDate.setText(
                    String.format(getResources().getString(R.string.sendback_date),
                            currentBorrows.get(position).getSendbackDate()));
            return convertView;
        }
    }

    @Override
    public void update(int flag, List<?> books) {
        if (flag == PersonalActivity.CURRENT_BORROWED) {
            currentBorrows = (List<CurrentBorrow>)books;
            if (adapter ==null) {
                adapter = new CustomAdapter();
                contentListView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
