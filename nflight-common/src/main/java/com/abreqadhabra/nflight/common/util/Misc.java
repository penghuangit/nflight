package com.abreqadhabra.nflight.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Misc {

    /**
     * <p>[概 要] システム時間取得。</p>
     * <p>[詳 細] システム時間を取得する。</p>
     * <p>[備 考] </p>
     * @return String システム時間
     */
    public static String getDateTime() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateType = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        return dateType.format(cal.getTime());
    }


}
