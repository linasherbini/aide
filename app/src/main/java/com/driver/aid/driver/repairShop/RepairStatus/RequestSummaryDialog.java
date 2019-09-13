package com.driver.aid.driver.repairShop.RepairStatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.aid.FormValidator;
import com.driver.aid.LoggedInUserManager;
import com.driver.aid.Model.Feedback;
import com.driver.aid.Model.Order;
import com.driver.aid.Model.Shop;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;

import java.util.Date;
import java.util.UUID;

import static com.driver.aid.FormValidator.extractText;
import static com.driver.aid.FormValidator.isValid;

public class RequestSummaryDialog extends DialogFragment {

    public static final String TAG = RequestSummaryDialog.class.getName();

    private static final String KEY_ORDER = "KEY_ORDER";

    public static RequestSummaryDialog newInstance(Order order) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ORDER, order);
        RequestSummaryDialog fragment = new RequestSummaryDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Order order = getArguments().getParcelable(KEY_ORDER);

        View carView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_request_summary, null);
        TextView summary = carView.findViewById(R.id.summary);
        TextView date = carView.findViewById(R.id.date);
        TextView technicianName = carView.findViewById(R.id.technician_name);
        TextView technicianNumber = carView.findViewById(R.id.technician_number);
        TextView status = carView.findViewById(R.id.status);
        TextView price = carView.findViewById(R.id.price);
        TextInputEditText feedback = carView.findViewById(R.id.feedback);
        Button submitFeedback=carView.findViewById(R.id.submitFeedback);
        feedback.setVisibility(View.GONE);
        submitFeedback.setVisibility(View.GONE);

        if (Order.STATUS_ACCEPTED.equals(order.getStatus())) {
            summary.setText(getString(R.string.your_order_accepted_by, order.getWorkshopName()));
            status.setText(R.string.on_the_way);
            price.setVisibility(View.GONE);
        } else if (Order.STATUS_FINISHED.equals(order.getStatus())) {
            summary.setText(getString(R.string.your_order_finished, order.getWorkshopName()));
            status.setText(R.string.finished);
            price.setVisibility(View.VISIBLE);
            price.setText(getString(R.string.total, order.calcualteCost()));
            feedback.setVisibility(View.VISIBLE);
            submitFeedback.setVisibility(View.VISIBLE);
            submitFeedback.setOnClickListener(v->{
                if(FormValidator.isValid(feedback)){

                    Feedback feedbackObject=new Feedback(UUID.randomUUID().toString(),order.getDriverID(),order.getDriverName(),order.getWorkshopId(),order.getOrderId(),order.getWorkshopName(),order.getTechnicianName(),FormValidator.extractText(feedback), new Date().toString());
                    RealmRemoteManager.getInstance().submitFeedback(feedbackObject);
                    Toast.makeText(getContext(),"Thanks for your Feedback!",Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        }
        date.setText(getString(R.string.order_date, order.getDate()));
        technicianName.setText(getString(R.string.technician_name_placeholder, order.getTechnicianName()));
        technicianNumber.setText(getString(R.string.technician_number_placeholder, order.getTechnicianNumber()));
        technicianNumber.setOnClickListener(c->{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + order.getTechnicianNumber()));
            startActivity(intent);
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(carView)
                .setPositiveButton("OK", (arg0, arg1) -> {

                    getDialog().dismiss();


                });
        return builder.create();
    }

}
