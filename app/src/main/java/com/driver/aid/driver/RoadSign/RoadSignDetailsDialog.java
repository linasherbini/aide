package com.driver.aid.driver.RoadSign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.driver.aid.GlideApp;
import com.driver.aid.Model.Content;
import com.driver.aid.R;

public class RoadSignDetailsDialog extends DialogFragment {

    public static final String TAG = RoadSignDetailsDialog.class.getName();

    private static final String KEY_CONTENT = "KEY_CONTENT";

    public static RoadSignDetailsDialog newInstance(Content content) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_CONTENT, content);
        RoadSignDetailsDialog fragment = new RoadSignDetailsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Content content = getArguments().getParcelable(KEY_CONTENT);

        View carView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_road_sign_details, null);
        TextView description = carView.findViewById(R.id.signDescription);
        description.setText(content.getDescription());
        ImageView signImage = carView.findViewById(R.id.roadSign);

        GlideApp.with(carView)
                .load(content.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .into(signImage);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(carView)
                .setPositiveButton("Got it!", (arg0, arg1) -> getDialog().dismiss());
        return builder.create();
    }

}
