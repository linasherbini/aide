package com.driver.aid.driver.RoadSign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.aid.Model.Content;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;

import java.util.List;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;


public class RoadSignListFragment extends Fragment implements RoadSignAdapter.RoadSignClickListner {

    public static final String TAG = RoadSignListFragment.class.getName();
    private RecyclerView recyclerView;
    private RoadSignAdapter roadSignAdapter;
    private RealmResults<Content> result;

    public RoadSignListFragment() {
        // Required empty public constructor
    }

    public static RoadSignListFragment newInstance() {
        return new RoadSignListFragment();
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        roadSignAdapter = new RoadSignAdapter(this);
        recyclerView.setAdapter(roadSignAdapter);

        result = RealmRemoteManager.getInstance()
                .getContent(contentList -> roadSignAdapter.setData(contentList), Content.sign);
    }

    @Override
    public void onRoadSignClicked(Content content) {
        RoadSignDetailsDialog.newInstance(content).show(getFragmentManager(), RoadSignDetailsDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        result.removeAllChangeListeners();

    }
}
