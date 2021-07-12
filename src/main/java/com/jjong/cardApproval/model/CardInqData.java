package com.jjong.cardApproval.model;

public class CardInqData {
    private String mngId;
    private CardInfo cardInfo;
    private String cardPayPlan;
    private String cardPayCancelType;
    private long amt;   //승인(취소)금액
    private long vat;   //승인(취소)vat
    private long cnlAmt;  //승인데이터 조회시 취소금액 합계
    private long cnlVat;  //승인데이터 조회시 취소vat 합계
    private long validAmt;  //승인데이터 조회시 취소포함된 남은금액
    private long validVat;  //승인데이터 조회시 취소포함된 남은vat

    public String getMngId() {
        return mngId;
    }

    public void setMngId(String mngId) {
        this.mngId = mngId;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getCardPayCancelType() {
        return cardPayCancelType;
    }

    public void setCardPayCancelType(String cardPayCancelType) {
        this.cardPayCancelType = cardPayCancelType;
    }

    public long getAmt() {
        return amt;
    }

    public void setAmt(long amt) {
        this.amt = amt;
    }

    public long getVat() {
        return vat;
    }

    public void setVat(long vat) {
        this.vat = vat;
    }

    public long getCnlAmt() {
        return cnlAmt;
    }

    public void setCnlAmt(long cnlAmt) {
        this.cnlAmt = cnlAmt;
    }

    public long getCnlVat() {
        return cnlVat;
    }

    public void setCnlVat(long cnlVat) {
        this.cnlVat = cnlVat;
    }

    public long getValidAmt() {
        return validAmt;
    }

    public void setValidAmt(long validAmt) {
        this.validAmt = validAmt;
    }

    public long getValidVat() {
        return validVat;
    }

    public void setValidVat(long validVat) {
        this.validVat = validVat;
    }

    public String getCardPayPlan() {
        return cardPayPlan;
    }

    public void setCardPayPlan(String cardPayPlan) {
        this.cardPayPlan = cardPayPlan;
    }
}
