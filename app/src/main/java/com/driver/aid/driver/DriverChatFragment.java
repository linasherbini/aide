package com.driver.aid.driver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.driver.aid.driver.jivoSdk.JivoDelegate;
import com.driver.aid.driver.jivoSdk.JivoSdk;
import com.driver.aid.R;

public class DriverChatFragment extends Fragment implements JivoDelegate {

    JivoSdk jivoSdk;


    public static DriverChatFragment newInstance() {
        return new DriverChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jivoSdk = new JivoSdk((WebView) view.findViewById(R.id.webview), "");
        jivoSdk.delegate = this;
        jivoSdk.prepare();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_chat, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onEvent(String name, String data) {
        if (name.equals("url.click")) {
            if (data.length() > 2) {
                String url = data.substring(1, data.length() - 1);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        }
    }

}
