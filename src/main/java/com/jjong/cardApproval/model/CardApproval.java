package com.jjong.cardApproval.model;

public class CardApproval {

    private String  payId;
    private CardInfo cardInfo;
    private String  cardPlan;
    private String  cardAprvStat;
    private long  payAmt;
    private long  payVat;
    private String infMsg;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) { this.payId = payId; }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getCardPlan() {
        return cardPlan;
    }

    public void setCardPlan(String cardPlan) {
        this.cardPlan = cardPlan;
    }

    public String getCardAprvStat() {
        return cardAprvStat;
    }

    public void setCardAprvStat(String cardAprvStat) {
        this.cardAprvStat = cardAprvStat;
    }

    public long getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(long payAmt) {
        this.payAmt = payAmt;
    }

    public long getPayVat() {
        return payVat;
    }

    public void setPayVat(long payVat) {
        this.payVat = payVat;
    }

    public String getInfMsg() {
        return infMsg;
    }

    public void setInfMsg(String infMsg) {
        this.infMsg = infMsg;
    }
}
