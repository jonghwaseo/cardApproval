package com.jjong.cardApproval.repository;

import com.jjong.cardApproval.model.CardApproval;
import com.jjong.cardApproval.model.CardCancel;
import com.jjong.cardApproval.model.CardInqData;

import java.util.List;

public interface CardRepository{

    CardApproval saveApproval(CardApproval cardApproval) throws Exception ;
    CardCancel saveCancel(CardCancel cardCancel) throws Exception ;
    CardInqData inqApprovalData(String mngId) throws Exception;
    CardInqData inqCancelData(String mngId) throws Exception;
    CardInqData inqCancelDataByPayId(String payId) throws Exception;


    String saveInfMsg(String mngId, String infMsg) throws Exception;
}
