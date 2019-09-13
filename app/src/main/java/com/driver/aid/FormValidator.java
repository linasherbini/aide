package com.driver.aid;

import android.text.TextUtils;
import android.widget.EditText;

public final class FormValidator {
    public static boolean isValid(EditText editText) {
        return !TextUtils.isEmpty(editText.getText().toString());

    }
    public static String extractText(EditText editText) {
        return editText.getText().toString().trim();
    }
}
