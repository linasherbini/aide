package com.driver.aid.shop.requestListing;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class ApproveOrderDialog extends DialogFragment {

    public static final String TAG = ApproveOrderDialog.class.getName();

    private static final String KEY_ORDER = "KEY_ORDER";

    public static ApproveOrderDialog newInstance(Order order) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ORDER, order);
        ApproveOrderDialog fragment = new ApproveOrderDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Order order = getArguments().getParcelable(KEY_ORDER);

        View carView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_approve_order, null);
        TextView driverName = carView.findViewById(R.id.driverName);
        TextView carType = carView.findViewById(R.id.carType);
        TextInputEditText technicianName = carView.findViewById(R.id.technician_name);
        TextInputEditText technicianNumber = carView.findViewById(R.id.technician_number);
        driverName.setText(getString(R.string.driver_name,order.getDriverName()));
        carType.setText(getString(R.string.cartype,order.getCarType()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(carView)
                .setPositiveButton("Accept", (arg0, arg1) -> {

                    if (isValid(technicianName) && isValid(technicianNumber)) {
                        Shop shop = LoggedInUserManager.getInstance().getShop();
                        RealmRemoteManager.getInstance().updateOrder(order.getOrderId(),extractText(technicianName),extractText(technicianNumber),shop.getUserId(),shop.getName(),Order.STATUS_ACCEPTED);
                        getDialog().dismiss();
                    } else {
                        Toast.makeText(getContext(), "Unable to submit request, All fields required!", Toast.LENGTH_SHORT).show();
                    }

                });
        return builder.create();
    }

}
