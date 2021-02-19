package com.thritydays.commonutil;

import android.text.TextUtils;

public class StringUtils {
    /**
     * 匹配 是否为中国手机卡 + 数据卡 + 上网卡
     * @param phone
     * @return
     */
    public static boolean isChinesePhone(String phone){
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        String pattern = "^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[01356789]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[189]\\d{2}|6[567]\\d{2}|4(?:[14]0\\d{3}|[68]\\d{4}|[579]\\d{2}))\\d{6}$";
        return phone.matches(pattern);

    }

    public static boolean isEmail(String str) {
        if (str == null) {
            return false;
        }
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return str.matches(strPattern);
        }
    }

    public static boolean isChineseIdCard(String idCard){
        if (TextUtils.isEmpty(idCard)) {
            return false;
        }
        //这个正则是最新的全国区域匹配
        String rex = "^(?:[16][1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4])\\d{4}(?:19|20)\\d{2}(?:(?:0[469]|11)(?:0[1-9]|[12][0-9]|30)|(?:0[13578]|1[02])(?:0[1-9]|[12][0-9]|3[01])|02(?:0[1-9]|[12][0-9]))\\d{3}[\\dXx]$";
//        String rex = "^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        return idCard.matches(rex);
    }
}
