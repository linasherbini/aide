package com.driver.aid.driver.roadHints;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.driver.aid.GlideApp;
import com.driver.aid.Model.Content;
import com.driver.aid.R;

public class HintDetailsDialog extends DialogFragment {

    public static final String TAG = HintDetailsDialog.class.getName();

    private static final String KEY_CONTENT = "KEY_CONTENT";

    public static HintDetailsDialog newInstance(Content content) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_CONTENT, content);
        HintDetailsDialog fragment = new HintDetailsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Content content = getArguments().getParcelable(KEY_CONTENT);

        View carView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_hint_details, null);
        TextView description = carView.findViewById(R.id.signDescription);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(content.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            description.setText(Html.fromHtml(content.getDescription()));
        }

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
