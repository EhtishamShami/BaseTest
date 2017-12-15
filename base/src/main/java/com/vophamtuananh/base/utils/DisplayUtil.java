package com.vophamtuananh.base.utils;

import android.content.Context;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class DisplayUtil {

    private static int deviceWidth = 0;
    private static int deviceHeight = 0;

    public static int getDeviceWidth(Context context) {
        if (deviceWidth == 0)
            deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
        return deviceWidth;
    }

    public static int getDeviceHeight(Context context) {
        if (deviceHeight == 0)
            deviceHeight = context.getResources().getDisplayMetrics().heightPixels;
        return deviceHeight;
    }

}
