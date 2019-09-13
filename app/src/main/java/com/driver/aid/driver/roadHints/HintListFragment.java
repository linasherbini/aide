package com.driver.aid.driver.roadHints;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.aid.Model.Content;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;

import io.realm.RealmResults;


public class HintListFragment extends Fragment implements HintsAdapter.HintClickListener {

    public static final String TAG = HintListFragment.class.getName();
    private RecyclerView recyclerView;
    private HintsAdapter hintsAdapter;
    private RealmResults<Content> result;

    public HintListFragment() {
        // Required empty public constructor
    }

    public static HintListFragment newInstance() {
        return new HintListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.images);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        hintsAdapter = new HintsAdapter(this);
        recyclerView.setAdapter(hintsAdapter);

        result = RealmRemoteManager.getInstance()
                .getContent(contentList -> hintsAdapter.setData(contentList), Content.hint);
    }

    @Override
    public void onRoadSignClicked(Content content) {
        HintDetailsDialog.newInstance(content).show(getFragmentManager(), HintDetailsDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        result.removeAllChangeListeners();

    }
}
