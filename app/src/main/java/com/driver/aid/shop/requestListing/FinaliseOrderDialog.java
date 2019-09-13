package com.driver.aid.shop.requestListing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.aid.LoggedInUserManager;
import com.driver.aid.Model.Order;
import com.driver.aid.Model.Shop;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;

import static com.driver.aid.FormValidator.extractText;
import static com.driver.aid.FormValidator.isValid;

public class FinaliseOrderDialog extends DialogFragment {

    public static final String TAG = FinaliseOrderDialog.class.getName();

    private static final String KEY_ORDER = "KEY_ORDER";
    private RequestDetailsInteraction requestDetailsInteraction;

    public static FinaliseOrderDialog newInstance(Order order) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ORDER, order);
        FinaliseOrderDialog fragment = new FinaliseOrderDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Order order = getArguments().getParcelable(KEY_ORDER);

        View carView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_finalise_order, null);
        TextView driverName = carView.findViewById(R.id.driverName);
        TextView carType = carView.findViewById(R.id.carType);
        TextInputEditText cost = carView.findViewById(R.id.cost);
        driverName.setText(getString(R.string.driver_name,order.getDriverName()));
        carType.setText(getString(R.string.cartype,order.getCarType()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(carView)
                .setPositiveButton("Finish", (arg0, arg1) -> {

                    if (isValid(cost)) {
                        RealmRemoteManager.getInstance().updateOrderCost(order.getOrderId(),extractText(cost)+" SAR",Order.STATUS_FINISHED);
                        getDialog().dismiss();
                        requestDetailsInteraction.onOrderFinalised(extractText(cost)+" SAR");
                        Toast.makeText(getContext(), "Order finalised, thank you!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Unable to submit request, All fields required!", Toast.LENGTH_SHORT).show();
                    }

                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getParentFragment() instanceof RequestDetailsInteraction){
            this.requestDetailsInteraction=(RequestDetailsInteraction)getParentFragment();
        }
    }

    public interface RequestDetailsInteraction{
        public void onOrderFinalised(String price);
    }
}
