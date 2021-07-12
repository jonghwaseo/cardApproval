package com.jjong.cardApproval.utils;

import com.jjong.cardApproval.model.CardInfo;

import java.nio.charset.StandardCharsets;

public class CardUtil {

    public static String encCardInfo(CardInfo cardInfo) throws Exception{
        String rtnVal = "";
        StringBuffer sb = new StringBuffer();
        sb.append(cardInfo.getCardNo());
        sb.append("|");
        sb.append(cardInfo.getCardValid());
        sb.append("|");
        sb.append(cardInfo.getCardCvc());

        StringUtil.fixLength(CryptUtil.encryptAES256(sb.toString()), 100, " ");

        return rtnVal;
    }

    public static CardInfo decCardInfo(String cardStr) throws Exception{
        CardInfo cardInfo = new CardInfo();
        cardStr = StringUtil.deleteWhitespace(cardStr);
        cardStr = CryptUtil.decryptAES256(cardStr);

        String[] tmpVarStr = StringUtil.split(cardStr, "|");
        if(tmpVarStr.length>=3){
            cardInfo.setCardNo(tmpVarStr[0]);
            cardInfo.setCardValid(tmpVarStr[1]);
            cardInfo.setCardCvc(tmpVarStr[2]);
        }

        return cardInfo;
    }

    public static CardInfo maskCardInfo(CardInfo cardInfo) throws Exception{
        if(cardInfo==null || cardInfo.getCardNo()==null ||cardInfo.getCardNo().length()<6 ){
            return cardInfo;
        }

        String first6 = StringUtil.getSubstring(cardInfo.getCardNo().getBytes(StandardCharsets.UTF_8), 0, 6);
        String last3 = StringUtil.getSubstring(cardInfo.getCardNo().getBytes(StandardCharsets.UTF_8), cardInfo.getCardNo().length()-3, cardInfo.getCardNo().length());
        String middle = StringUtil.getSubstring(cardInfo.getCardNo().getBytes(StandardCharsets.UTF_8), 6, cardInfo.getCardNo().length()-3);
        middle = StringUtil.changeStrAll(middle);
        String maskCardNo = first6+middle+last3;
        cardInfo.setCardNo(maskCardNo);

        return cardInfo;
    }
}
