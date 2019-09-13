package com.driver.aid.shop.requestListing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.aid.LoggedInUserManager;
import com.driver.aid.Model.Order;
import com.driver.aid.Model.Shop;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;

import java.util.List;

import io.realm.RealmResults;


public class RequestListingFragment extends Fragment implements CurrentRequestViewHolder.CurrentRequestListner, NewRequestViewHolder.NewRequestListener {


    public static final String TAG = RequestListingFragment.class.getName();
    RecyclerView recyclerView;
    RequestListingAdapter requestListingAdapter;
    private RealmResults<Order> queryResult;
    private RequestListingInteractionListener interactionListener;

    public static Fragment newInstance() {
        return new RequestListingFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        requestListingAdapter = new RequestListingAdapter(getContext(), this, this);
        recyclerView.setAdapter(requestListingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Shop shop = LoggedInUserManager.getInstance().getShop();

        queryResult = RealmRemoteManager.getInstance().getRepairShopRequests(shop.getUserId(), new RealmRemoteManager.ShopRequests() {
            @Override
            public void result(List<Order> currentRequest, List<Order> newRequests) {
                requestListingAdapter.setData(currentRequest, newRequests);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        queryResult = null;
    }

    @Override
    public void onCurrentRequestClicked(Order order) {
        interactionListener.onOrderClicked(order);
    }

    @Override
    public void onNewRqeustClicked(Order order) {
        ApproveOrderDialog.newInstance(order).show(getFragmentManager(), ApproveOrderDialog.TAG);
    }

    @Override
    public void onLocationClicked(Double lat, Double lang) {
        if (lat != null && lang != null) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+lat.toString() + "," + lang.toString()));
            startActivity(intent);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RequestListingInteractionListener){
            this.interactionListener= (RequestListingInteractionListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.interactionListener=null;
    }

    @Override
    public void onPhoneClicked(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    public interface RequestListingInteractionListener{
        public void onOrderClicked(Order order);
    }
}
