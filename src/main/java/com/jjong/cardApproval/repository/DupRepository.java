package com.jjong.cardApproval.repository;

import com.jjong.cardApproval.model.CardApproval;
import com.jjong.cardApproval.model.CardCancel;
import com.jjong.cardApproval.model.CardInqData;

import java.util.List;

public interface DupRepository {


    void saveCheckValue(String checkVal);
    boolean getCheckValue(String checkVal);
    void removeCheckValue(String checkVal);

}
