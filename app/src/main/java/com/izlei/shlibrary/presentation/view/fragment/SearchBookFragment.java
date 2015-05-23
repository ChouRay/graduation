package com.izlei.shlibrary.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.izlei.shlibrary.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhouzili on 2015/5/19.
 */
public class SearchBookFragment extends BaseFragment implements SearchView.OnQueryTextListener{

    @InjectView(R.id.sv_search_book)
    SearchView searchView;

    public static SearchBookFragment newInstance() {
        return new SearchBookFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, rootView);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        return rootView;
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        this.showToastMessage("Your Input is "+query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
