package com.jjong.cardApproval.utils;


import com.jjong.cardApproval.model.CardInfo;

/**
 */
public class StringUtil {


    private StringUtil() {

    }


    /**
     */
    public static String fixLength(String input, int limit, String postfix) {
        String buffer = "";
        char[] charArray = input.toCharArray();
        if (limit >= charArray.length) {
            return input;
        }

        for (int j = 0; j < limit; j++) {
            buffer += charArray[j];

        }
        buffer += postfix;
        return buffer;
    }


    /**
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    /**
     * <pre>
     */
    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }


    /**
     */
    public static String[] split(String source, String delim)
    {
//      List list = StringUtil.stringToList("LT00^^^^", "^");
//      return (String [])list.toArray(new String[list.size()]);

        String rltarr[] = (String[])null;
        if(source == null)
            return rltarr;

        if(delim == null || "".equals(delim)){
            String [] rltarr2 = {source};
            return rltarr2;
        }

        rltarr = (source+"▩").split("\\"+ delim);
        int len = rltarr.length - 1;
        rltarr[len] = rltarr[len].substring(0, rltarr[len].length()-1);

        return rltarr;
    }



    /**
     */
    public static String replaceStr(String source, int start, int end, String toStr) {
        if (source == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(source);

        int len = source.length();
        if (start > len || end < start) {
            return result.toString();
        }

        result.replace(start, end, toStr);
        return result.toString();
    }


    /**
     */
    public static String getSubstring(byte[] bstr, int idx1, int idx2) {
        int bLength = bstr.length;

        if (idx2 > bLength || idx1 > bLength || idx1 > idx2) {
            throw new java.lang.StringIndexOutOfBoundsException();
        }

        byte chkstr[] = new byte[idx2 - idx1];
        byte c;
        int icutPos = 0;

        for (int i = 0; i < idx1; i++) {
            c = bstr[i];

            if (c >= 0 && c < 127) { // 1 byte 문자
                icutPos = icutPos + 1;
            } else { // 2byte 문자
                icutPos = icutPos + 2;
                i++;

                if (icutPos > idx1)
                    break;
            }
        }
        int iStrPos = 0; //결과 스트링 Index
        for (int i = icutPos; i < idx2; i++) {
            c = bstr[i];

            if (c >= 0 && c < 127) { // 1 byte 문자
                icutPos = icutPos + 1;
                chkstr[iStrPos++] = c;
            } else { // 2byte 문자
                icutPos = icutPos + 2;
                i++;

                if (icutPos > idx2) {
                    chkstr[iStrPos++] = ' ';
                    break;
                }
                chkstr[iStrPos++] = c;
                chkstr[iStrPos++] = bstr[i];
            }
        }

        return new String(chkstr);
    }


    /**
     */
    public static String changeStrAll(String str) {
        char[] ch = str.toCharArray();
        for(int i = 0; i < ch.length ; i ++){
            ch[i] = '*';
        }
        return String.valueOf(ch);
    }


    /**
     */
    public static String rpad(String src, int size, String filler) {
        /*
         if (src == null) src = "";
         int len = src.length();
         if (len > size) return src.substring(0, size);
         for (int i = len; i < size; i++)
         src += filler;
         return src;
         * </pre>
     */
        StringBuilder sb = new StringBuilder();
        if (src == null) {
            for (int i = 0; i < size; i++) {
                sb.append(filler);
            }
        } else {
            int len = src.length();
            if (len > size) {
                sb.append(src.substring(0, size));
            } else {
                sb.append(src);

            }
            for (int i = len; i < size; i++) {
                sb.append(filler);
            }
        }
        return sb.toString();
    }


    /**
     */
    public static boolean isNumeric (String text) {
        if (text == null || text.trim().length()==0)
            return false;

        int size = text.length();
        for(int i = 0 ; i < size ; i++) {
            if(!Character.isDigit(text.charAt(i)))
                return false;

        }
        return true;
    }



    public static String lpad(long src, int size, String filler) {
        return StringUtil.lpad(String.valueOf(src), size, filler);
    }

    /**
     */
    public static String lpad(String src, int size, String filler) {
        String pattern = "";
        int len = nvl(src).length();
        if (len > size) {
            return src.substring(len - size);
        }
        for (int i = len; i < size; i++) {
            pattern += filler;
        }
        return pattern + nvl(src);
    }


    /**
     *
     */
    public static String makeResStr(String dataType, String mngId, CardInfo cardInfo, String cardPlan, long amt, long vat, String oriPayId) throws Exception {
        //데이터 길이 숫자4  "   4"

        //데이터구분 문자10  "PAYMENT   "
        //관리번호 문자20
        //카드번호 숫자L20    "1111222233334444    "
        //할부개월수 숫자0 2   "06"
        //유효기간 숫자L 4  "2025"
        //cvc  숫자L  3
        //거래금액 숫자10  "     10000"
        //부가세 숫자0 10  "0000000100"
        //원 거래번호 문자20  <=취소시만
        //암호화된 카드번호 문자300
        //FIller 문자 47

        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.rpad(dataType, 10, " "));
        sb.append(mngId);
        sb.append(StringUtil.rpad(cardInfo.getCardNo(), 20, " "));
        sb.append(StringUtil.lpad(cardPlan, 2, "0"));
        sb.append(cardInfo.getCardValid());
        sb.append(cardInfo.getCardCvc());
        sb.append(lpad(amt, 10, " "));
        sb.append(lpad(vat, 10, "0"));
        sb.append(rpad(oriPayId, 20, " "));
        sb.append(rpad(CardUtil.encCardInfo(cardInfo), 300, " "));
        sb.append(rpad("", 47, " "));

        int len = sb.length();
        sb.insert(0, StringUtil.lpad(len, 4, " "));

        return sb.toString();
    }



    /**
     */
    public static String nvl(String str) {
        if (str ==  null)
            return "";
        return str;
    }

    ////////////////////////////////////////////////////////////////////////////////





}



















