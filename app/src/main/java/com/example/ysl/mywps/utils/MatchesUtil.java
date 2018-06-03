package com.example.ysl.mywps.utils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class MatchesUtil {


    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3578]\\d{9}";
        // "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (CommonUtil.isEmpty(mobiles)) {
            return false;
        } else
            return mobiles.matches(telRegex);
    }

    /*方法二：推荐，速度最快
  * 判断是否为整数
  * @param str 传入的字符串
  * @return 是整数返回true,否则返回false
*/

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
