package com.shyj.shop.shunheapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ls
 * @description 校验手机号码
 * @date 2017/7/18
 * @package com.shyj.shop.shunheapp.utils
 */

public class ClassPathResource {


    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
}
