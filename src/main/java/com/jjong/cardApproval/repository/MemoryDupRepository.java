package com.jjong.cardApproval.repository;

import com.jjong.cardApproval.model.CardApproval;
import com.jjong.cardApproval.model.CardCancel;
import com.jjong.cardApproval.model.CardInqData;

import java.util.*;

public class MemoryDupRepository implements DupRepository  {

    private static List<String> dupCheckList = new ArrayList<String>();


    public void saveCheckValue(String checkVal){
        //승인시카드번호, 취소시관리번호를 저장 중복방지처리
        dupCheckList.add(checkVal)   ;
    }

    public boolean getCheckValue(String checkVal){
        //존재여부체크
        return dupCheckList.contains(checkVal);
    }

    public void removeCheckValue(String checkVal){
        //처리 완료시 승인시카드번호, 취소시관리번호를 삭제
        dupCheckList.remove(checkVal);
    }

}
