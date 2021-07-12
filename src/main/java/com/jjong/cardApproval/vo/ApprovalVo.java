package com.jjong.cardApproval.vo;

public class ApprovalVo extends CommonVo {
    private String payId;
    private String cardNo;
    private String cardValid;
    private String cardCvc;
    private String cardPayPlan;
    private long payAmt;
    private long payVat;
    private String cardAprvStat;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardValid() {
        return cardValid;
    }

    public void setCardValid(String cardValid) {
        this.cardValid = cardValid;
    }

    public String getCardCvc() {
        return cardCvc;
    }

    public void setCardCvc(String cardCvc) {
        this.cardCvc = cardCvc;
    }

    public String getCardPayPlan() {
        return cardPayPlan;
    }

    public void setCardPayPlan(String cardPayPlan) {
        this.cardPayPlan = cardPayPlan;
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

    public String getCardAprvStat() {
        return cardAprvStat;
    }

    public void setCardAprvStat(String cardAprvStat) {
        this.cardAprvStat = cardAprvStat;
    }


}
