package com.saberrrc.cmy.common.utils;

import android.text.SpannableString;
import android.text.Spanned;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {
    private static final String email   = "\\w+([-+.]\\w+)*@\\w{2,}+([-.]\\w+)*\\.\\w{2,}+([-.]\\w+)*";//^[\w/./,/?/!]+$
    private static final String PSD_VER = "^([0-9]|[a-z]|[A-Z]|[\\.]|[?]|[,]|[!]|[_])+$";//^[\w/./,/?/!]       ^[\w]|[\.]|[\,]|[\?]|[\!]+$   ^([\w]|[\.]|[?]|[,]|[!])+$

    private static final String inner_phone  = "^[1][3-8]+\\d{9}"; // "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
    private static final String inner_phone1 = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    private static final String inner_phone2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
    private static final String inner_phone3 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";


    private StrUtils() {
        // empty here
    }

    /**
     * Check the specified string is a empty string after trim().
     *
     * @param aString the checked string
     * @return return true if the string is empty string. return false
     * otherwise.
     */
    public static boolean isEmpty(String aString) {
        return aString == null || aString.trim().length() == 0 || aString.trim().equals("null");
    }

    /**
     * Insure the string is not null.
     *
     * @param aString the string object.
     * @return return "" if the string is null, otherwise return the string
     * self.
     */
    public static String stringNotNull(String aString) {
        return (aString == null) ? "" : aString;
    }

    /**
     * true is verfy passed false is not passed
     *
     * @param psd
     * @return
     */
    public static boolean verfyPasswordFormat(String psd) {
        if (isEmpty(psd))
            return false;
        Pattern pattern = Pattern.compile(PSD_VER);
        Matcher matcher = pattern.matcher(psd);
        return matcher.matches();
    }

    /**
     * Checks whether the given string is email or no.
     *
     * @param str String to test.
     * @return true when the given string is email , or false.
     */
    public static boolean isEmail(String str) {
        Pattern emailAddress = Pattern.compile(email);
        Pattern p = emailAddress;
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isPhone(String phone) {
        Pattern emailAddress = Pattern.compile(inner_phone);
        Pattern p = emailAddress;
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static String formatVideoDuration(String videoDuration) {
        try {
            String[] strings = videoDuration.split(":");
            if (Integer.parseInt(strings[0]) == 0 && Integer.parseInt(strings[1]) == 0) {
                return "0'" + strings[2];
            } else if (Integer.parseInt(strings[0]) == 0) {
                return strings[1] + "'" + strings[2];
            } else {
                return strings[0] + "'" + strings[1] + "'" + strings[0];
            }
        } catch (Exception e) {
            LogUtil.d("videoDuration  error");
            return videoDuration;
        }
    }

    /**
     * Checks whether the given string is inner phone number or no.
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isInnerMobileNO(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }
        boolean isValid = false;

        String expression = inner_phone;
        // String expression2 = inner_phone2;
        // String expression3 = inner_phone3;

        Pattern pattern1 = Pattern.compile(expression);
        Matcher matcher1 = pattern1.matcher(phoneNumber);

        // Pattern pattern2 = Pattern.compile(expression2);
        // Matcher matcher2 = pattern2.matcher(phoneNumber);
        //
        // Pattern pattern3 = Pattern.compile(expression3);
        // Matcher matcher3 = pattern3.matcher(phoneNumber);
        if (matcher1.matches()) { // || matcher2.matches() ||
            // matcher3.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Create a String for universally unique identifier
     *
     * @return String
     */
    public static String createRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String measureStr(String str) {
        if (isEmpty(str))
            return "";
        else
            return str;
    }

    public static int toInt(String str) {
        int result = 0;
        if (isEmpty(str)) {
            return result;
        }
        try {
            result = Integer.valueOf(str);
        } catch (Exception e) {
            LogUtil.d(StrUtils.class.getSimpleName(), e.toString());
            result = 0;
        }
        return result;
    }

    public static String formatTosepara(float data) {
        return formatTosepara(data, "#,###.00");
    }

    public static String formatTosepara(float data, String mattchers) {
        DecimalFormat df = new DecimalFormat(mattchers);
        return df.format(data);
    }

    public static class Mattchers {
        /**
         * #,###</br>
         * 1200->1,200
         */
        public static final String TYPE1 = "#,###";
        /**
         * #,###.00</br>
         * 1200->1,200.00
         */
        public static final String TYPE2 = "#,###.00";
    }

    public static String formartNum(int num) {
        return formartNum(String.valueOf(num));
    }

    public static String formartNum(long num) {
        return formartNum(String.valueOf(num));
    }

    //	public static String formartNum(String num){
    //		if(isEmpty(num) || !num.matches("^[\\d]+$"))//验证正整数
    //			return "0";
    //		if(num.length()<5){
    //			DecimalFormat df4 = new DecimalFormat("#,###");
    //			return df4.format(Integer.parseInt(num));
    //		}else{
    //			String subStr = num.substring(0, num.length()-3);
    //			float formatNum = Float.parseFloat(subStr)/10;
    //			DecimalFormat df4 = new DecimalFormat("#,###.#");
    //			return df4.format(formatNum)+"万";
    //		}
    //	}

    public static String formartNum(String num) {
        try {
            if (isEmpty(num) || !num.matches("^[\\d]+$"))//验证正整数
                return "0";
            if (num.length() < 5) {
                DecimalFormat df4 = new DecimalFormat("#,###");
                return df4.format(Integer.parseInt(num));
            } else {
                float floatNum = Float.parseFloat(num);
                float tempNum = floatNum / 1000.0F;
                System.out.println(new BigDecimal("" + tempNum).setScale(0, BigDecimal.ROUND_HALF_UP));
                String numStr = new BigDecimal("" + tempNum).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                num = Integer.parseInt(numStr) * 1000 + "";
                String subStr = num.substring(0, num.length() - 3);
                float formatNum = Float.parseFloat(subStr) / 10;
                DecimalFormat df4 = new DecimalFormat("#,###.#");
                return df4.format(formatNum) + "万";
            }
        } catch (Exception e) {
            return "0";
        }
    }

    public static String formartNumFloat(String num) {
        try {
            if (isEmpty(num) || !num.matches("^[\\d]+$"))//验证正整数
                return "0";
            if (num.length() < 5) {
                DecimalFormat df4 = new DecimalFormat("#,###.##");
                return df4.format(Integer.parseInt(num));
            } else {
                float floatNum = Float.parseFloat(num);
                float tempNum = floatNum / 1000.0F;
                System.out.println(new BigDecimal("" + tempNum).setScale(0, BigDecimal.ROUND_HALF_UP));
                String numStr = new BigDecimal("" + tempNum).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                num = Integer.parseInt(numStr) * 1000 + "";
                String subStr = num.substring(0, num.length() - 3);
                float formatNum = Float.parseFloat(subStr) / 10;
                DecimalFormat df4 = new DecimalFormat("#,###.#");
                return df4.format(formatNum) + "万";
            }
        } catch (Exception e) {
            return "0";
        }
    }

    public static String subStringFromEnd(String str, int subStrLength) {
        if (isEmpty(str) || str.length() <= subStrLength) {
            return str;
        }
        return str.substring(str.length() - subStrLength, str.length());
    }

    public static SpannableString setStringTypeFace(String spann, TypeFaceOptions... options) {
        /**
         * mTextView = (TextView)findViewById(R.id.myTextView);

         //创建一个 SpannableString对象
         msp = new SpannableString("字体测试字体大小一半两倍前景色背景色正常粗体斜体粗斜体下划线删除线x1x2电话邮件网站短信彩信地图X轴综合");

         //设置字体(default,default-bold,monospace,serif,sans-serif)
         msp.setSpan(new TypefaceSpan("monospace"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         msp.setSpan(new TypefaceSpan("serif"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

         //设置字体大小（绝对值,单位：像素）
         msp.setSpan(new AbsoluteSizeSpan(20), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         msp.setSpan(new AbsoluteSizeSpan(20,true), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。

         //设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
         msp.setSpan(new RelativeSizeSpan(0.5f), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //0.5f表示默认字体大小的一半
         msp.setSpan(new RelativeSizeSpan(2.0f), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //2.0f表示默认字体大小的两倍

         //设置字体前景色
         msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

         //设置字体背景色
         msp.setSpan(new BackgroundColorSpan(Color.CYAN), 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置背景色为青色

         //设置字体样式正常，粗体，斜体，粗斜体
         msp.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
         msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 20, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
         msp.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 22, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //斜体
         msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 24, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗斜体

         //设置下划线
         msp.setSpan(new UnderlineSpan(), 27, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

         //设置删除线
         msp.setSpan(new StrikethroughSpan(), 30, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

         //设置上下标
         msp.setSpan(new SubscriptSpan(), 34, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //下标
         msp.setSpan(new SuperscriptSpan(), 36, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   //上标

         //超级链接（需要添加setMovementMethod方法附加响应）
         msp.setSpan(new URLSpan("tel:4155551212"), 37, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //电话
         msp.setSpan(new URLSpan("mailto:webmaster@google.com"), 39, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //邮件
         msp.setSpan(new URLSpan("http://www.baidu.com"), 41, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //网络
         msp.setSpan(new URLSpan("sms:4155551212"), 43, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //短信   使用sms:或者smsto:
         msp.setSpan(new URLSpan("mms:4155551212"), 45, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //彩信   使用mms:或者mmsto:
         msp.setSpan(new URLSpan("geo:38.899533,-77.036476"), 47, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //地图

         //设置字体大小（相对值,单位：像素） 参数表示为默认字体宽度的多少倍
         msp.setSpan(new ScaleXSpan(2.0f), 49, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //2.0f表示默认字体宽度的两倍，即X轴方向放大为默认字体的两倍，而高度不变
         //SpannableString对象设置给TextView
         myTextView.setText(sp);
         //设置TextView可点击
         myTextView.setMovementMethod(LinkMovementMethod.getInstance());
         */
        SpannableString spannableStr = new SpannableString(spann);
        for (TypeFaceOptions typeFaceOptions : options) {

            if (typeFaceOptions.getWhat() == null)
                throw new IllegalArgumentException("what 不能为null");
            if (typeFaceOptions.start == -1)
                throw new IllegalArgumentException("start 未设置");
            if (typeFaceOptions.end == -1)
                throw new IllegalArgumentException("end 未设置");

            spannableStr.setSpan(typeFaceOptions.getWhat(), typeFaceOptions.start, typeFaceOptions.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableStr;
    }

    public class TypeFaceOptions {
        int    start = -1;
        int    end   = -1;
        Object what  = null;

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public Object getWhat() {
            return what;
        }

        public void setWhat(Object what) {
            this.what = what;
        }
    }

    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        if (StrUtils.isEmail(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String formatPhone(String phone) {
        if (isPhone(phone)) {
            try {
                String reStr = phone.substring(phone.length() - 4, phone.length());
                String preStr = phone.substring(0, phone.length() - 8);
                StringBuilder sb = new StringBuilder();
                sb.append(preStr).append("****").append(reStr);
                return sb.toString();
            } catch (Exception e) {
                return phone;
            }
        }
        //如果不是手机号 原路返回
        return phone;
    }

    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }

    public static String formatContentForSina(String url, String oldContent, int expectLength) {
        if (StrUtils.isEmpty(url) || StrUtils.isEmpty(oldContent)) {
            return " ";
        }
        if (url.length() + oldContent.length() < expectLength) {
            return oldContent;
        }
        return oldContent.substring(0, expectLength - url.length());
    }


    public static String formatTime() {
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return simpleDate.format(now.getTime());
    }

    public static DecimalFormat df = new DecimalFormat("######0.00");
}
