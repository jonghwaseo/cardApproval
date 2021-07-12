package com.jjong.cardApproval.model;

public class CardCancel {

    private String cancelId;
    private String  payId;
    private String  cardPlan;
    private String  cardCancelStat;
    private long  cancelAmt;
    private long  cancelVat;

    private String infMsg;

    public String getCancelId() {
        return cancelId;
    }

    public void setCancelId(String cancelId) {
        this.cancelId = cancelId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getCardPlan() {
        return cardPlan;
    }

    public void setCardPlan(String cardPlan) {
        this.cardPlan = cardPlan;
    }

    public String getCardCancelStat() {
        return cardCancelStat;
    }

    public void setCardCancelStat(String cardCancelStat) {
        this.cardCancelStat = cardCancelStat;
    }

    public long getCancelAmt() {
        return cancelAmt;
    }

    public void setCancelAmt(long cancelAmt) {
        this.cancelAmt = cancelAmt;
    }

    public long getCancelVat() {
        return cancelVat;
    }

    public void setCancelVat(long cancelVat) {
        this.cancelVat = cancelVat;
    }

    public String getInfMsg() {
        return infMsg;
    }

    public void setInfMsg(String infMsg) {
        this.infMsg = infMsg;
    }
}
