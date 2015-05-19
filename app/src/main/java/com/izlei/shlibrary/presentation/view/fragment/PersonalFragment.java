package com.izlei.shlibrary.presentation.view.fragment;

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
import com.izlei.shlibrary.data.entity.CurrentBorrow;
import com.izlei.shlibrary.data.repository.Relations;
import com.izlei.shlibrary.presentation.model.UserModel;
import com.izlei.shlibrary.presentation.view.activity.PersonalActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**个人中心 包括当前借阅，借阅历史，捐赠历史
 * Created by zhouzili on 2015/4/11.
 */
public class PersonalFragment extends Fragment implements Relations.IRelationBook{

    public static final String ARG_SELECTION_NUMBER = "selection_number";
    @InjectView(R.id.list_personal_details)
    ListView contentListView;
    @InjectView(R.id.username_text)
    TextView nameText;
    @InjectView(R.id.user_email_text)
    TextView emailTextView;

    private CustomAdapter adapter = null;
    private List<CurrentBorrow> currentBorrows;
    private UserModel userModel;
    private LayoutInflater inflater;
    private int currSelection;

    private Relations relations;

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmen_personal_details, container, false);
        ButterKnife.inject(PersonalFragment.this, rootView);
        currSelection = getArguments().getInt(ARG_SELECTION_NUMBER);
        userModel = (UserModel)getArguments().getSerializable("USERMODEL");
        setUserInfoText(userModel);
        relations = new Relations();
        relations.initIRelationBookObject(this);

        return rootView;
    }

    public void setUserInfoText(UserModel userModel) {
        nameText.setText(userModel.getUsername());
        emailTextView.setText(userModel.getEmail());
    }

    @OnClick(R.id.current_borrow_layout)
    public void onCurrentBorrowClick() {
        relations.findMyCurrentBorrow();
    }

    @Override
    public void getCurrentBorrowedBook(List<?> books) {
        currentBorrows = (List<CurrentBorrow>) books;
        if (adapter ==null) {
            adapter = new CustomAdapter();
            contentListView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    class CustomAdapter extends BaseAdapter {
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
            CurrentBorrowHolder holder = null;
            if (convertView != null) {
                holder = (CurrentBorrowHolder) convertView.getTag();
            }else {
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.personal_borrow_lsit_item, null);
                holder = new CurrentBorrowHolder(convertView);
                convertView.setTag(holder);
            }
            holder.title.setText(currentBorrows.get(position).getTitle());
            holder.borrowedDate.setText(
                    String.format(getResources().getString(R.string.borrowed_date),
                            currentBorrows.get(position).getBorrowDate()));
            holder.sendBackDate.setText(
                    String.format(getResources().getString(R.string.sendback_date),
                            currentBorrows.get(position).getSendbackDate()));
            return convertView;
        }
    }


    static class CurrentBorrowHolder {
        @InjectView(R.id.tv_book_title)
        TextView title;
        @InjectView(R.id.tv_borrowed_date)
        TextView borrowedDate;
        @InjectView(R.id.tv_book_sendback_date)
        TextView sendBackDate;

        public CurrentBorrowHolder(View view) {
            ButterKnife.inject(CurrentBorrowHolder.this, view);
        }
    }

}
