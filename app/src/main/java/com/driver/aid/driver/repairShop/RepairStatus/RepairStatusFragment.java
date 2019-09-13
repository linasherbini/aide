package com.driver.aid.driver.repairShop.RepairStatus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.aid.Model.Order;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmResults;


public class RepairStatusFragment extends Fragment implements RepairStatusViewHolder.RepairStatusListener {


    private RealmResults<Order> result;

    public RepairStatusFragment() {
        // Required empty public constructor
    }


    public static RepairStatusFragment newInstance() {
        return new RepairStatusFragment();
    }

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recyclerView;
    RepairStatusAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repair_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.statusRecyclerView);
        adapter = new RepairStatusAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        loadStatus();

    }


    private void loadStatus() {
        result = RealmRemoteManager.getInstance().getRepairRequestStatus(orders -> {
            if (adapter != null) {
                adapter.setData(orders);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        result.removeAllChangeListeners();
    }

    @Override
    public void onOrderClicked(Order order) {
        if (Order.STATUS_ACCEPTED.equals(order.getStatus()) || Order.STATUS_FINISHED.equals(order.getStatus())) {
            RequestSummaryDialog.newInstance(order).show(getFragmentManager(), RequestSummaryDialog.TAG);
        }
    }
}
