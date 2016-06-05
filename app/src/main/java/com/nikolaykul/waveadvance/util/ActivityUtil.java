package com.nikolaykul.waveadvance.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ActivityUtil {

    public static void hideKeyboard(Activity activity) {
        final View view = activity.getCurrentFocus();
        if (null != view) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
