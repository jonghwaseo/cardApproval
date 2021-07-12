package com.jjong.cardApproval.vo;

public class CancelVo extends CommonVo{
    private String cancelId;
    private String payId;

    private long cancelAmt;
    private long cancelVat;
    private String cardCancelStat;

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

    public String getCardCancelStat() {
        return cardCancelStat;
    }

    public void setCardCancelStat(String cardCancelStat) {
        this.cardCancelStat = cardCancelStat;
    }
}
