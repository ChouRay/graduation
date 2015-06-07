package com.izlei.shlibrary.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.presentation.model.BookBarModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhouzili on 2015/5/24.
 */
public class BookBarAdapter extends  RecyclerView.Adapter<BookBarAdapter.LeaveWordsViewHolder>  {
    private Context context;
    private final LayoutInflater layoutInflater;

    private List<BookBarModel> bookBarModels;

    public BookBarAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        bookBarModels = new ArrayList<>();
    }


    @Override
    public LeaveWordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = this.layoutInflater.inflate(R.layout.list_leave_words_item, parent, false);
        return new LeaveWordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaveWordsViewHolder holder, int position) {
        BookBarModel bookBarModel = bookBarModels.get(position);
        holder.username.setText(bookBarModel.getUsername());
        holder.leaveWords.setText(bookBarModel.getLeaveWords());
        holder.updatedAt.setText(bookBarModel.getUpdatedAt());
    }

    @Override
    public int getItemCount() {
        return (this.bookBarModels != null) ? this.bookBarModels.size() : 0;
    }

    public void setMoments(List<BookBarModel> bookBarModelList) {
        for(BookBarModel moment : bookBarModelList) {
            this.bookBarModels.add(moment);
        }
        this.notifyDataSetChanged();
    }

    public void addMoment(BookBarModel bookBarModel) {
        this.bookBarModels.add(0,bookBarModel);
        this.notifyDataSetChanged();

    }


    class LeaveWordsViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_avatar)
        ImageView avatarThumbnail;
        @InjectView(R.id.tv_username)
        TextView username;
        @InjectView(R.id.tv_leave_words)
        TextView leaveWords;
        @InjectView(R.id.tv_updated_at)
        TextView updatedAt;
        @InjectView(R.id.rv_comments)
        RecyclerView rvComments;

        private LinearLayoutManager linearLayoutManager;

        @OnClick(R.id.btn_comment)
        public void commentOnclick() {
        }

        public LeaveWordsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            rvComments.setHasFixedSize(true);
            this.linearLayoutManager = new LinearLayoutManager(context);
            this.rvComments.setLayoutManager(linearLayoutManager);
        }
    }
}
