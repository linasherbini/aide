package com.driver.aid.shop.requestDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.driver.aid.Model.Order;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;
import com.driver.aid.shop.requestListing.FinaliseOrderDialog;

import java.util.Objects;


public class RequestDetailsFragment extends Fragment implements FinaliseOrderDialog.RequestDetailsInteraction {


    public static final String TAG = RequestDetailsFragment.class.getName();
    private static final String KEY_ORDER = "KEY_ORDER";

    public static RequestDetailsFragment newInstance(Order order) {
        RequestDetailsFragment fragment = new RequestDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_details, container, false);
    }

    TextView title;
    TextView name;
    TextView carType;
    TextView issue;
    TextView phoneNumber;
    TextView location;
    TextView jobStatus;
    TextView total;
    Button finishJob;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        name = view.findViewById(R.id.name);
        carType = view.findViewById(R.id.carType);
        issue = view.findViewById(R.id.issue);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        location = view.findViewById(R.id.location);
        jobStatus = view.findViewById(R.id.jobStatus);
        total = view.findViewById(R.id.total);
        finishJob = view.findViewById(R.id.finishJob);
        Order order = getArguments().getParcelable(KEY_ORDER);
        if (order == null) {
            return;
        }
        title.setText(order.getDriverName());
        name.setText(getString(R.string.driver_name, order.getDriverName()));
        carType.setText(getString(R.string.cartype, order.getCarType()));
        issue.setText(getString(R.string.car_issue, order.getIssue()));
        phoneNumber.setText(getString(R.string.phone_number, order.getDriverPhone()));
        jobStatus.setText(getString(R.string.job_status, order.getStatus()));
        if(order.getLan()!=null&&order.getLan()!=null){
            location.setText(getString(R.string.location));
            location.setVisibility(View.VISIBLE);
        }else{
            location.setVisibility(View.GONE);
        }
        phoneNumber.setOnClickListener(c -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });
        location.setOnClickListener(c -> {
            if (order.getLat() != null && order.getLan() != null) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+order.getLat().toString() + "," + order.getLan().toString()));
                startActivity(intent);
            }
        });
        finishJob.setOnClickListener(c->{
            RealmRemoteManager.getInstance().updateOrderStatus(order.getOrderId(),Order.STATUS_FINISHED);
            FinaliseOrderDialog.newInstance(order).show(getChildFragmentManager(),FinaliseOrderDialog.TAG);
        });

        if(Order.STATUS_ACCEPTED.equals(order.getStatus())){
            finishJob.setVisibility(View.VISIBLE);
            total.setVisibility(View.GONE);
        }else{
            total.setVisibility(View.VISIBLE);
            finishJob.setVisibility(View.GONE);
            total.setVisibility(View.VISIBLE);
            total.setText(getString(R.string.total, order.getPrice()));

        }
    }

    @Override
    public void onOrderFinalised(String price) {
        jobStatus.setText(getString(R.string.job_status, Order.STATUS_FINISHED));
        finishJob.setVisibility(View.GONE);
        total.setVisibility(View.VISIBLE);
        total.setText(getString(R.string.total, price));
    }


}
