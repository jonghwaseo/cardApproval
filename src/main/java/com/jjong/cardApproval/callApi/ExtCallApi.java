package com.jjong.cardApproval.callApi;

import java.util.HashMap;
import java.util.Map;


public class ExtCallApi {

    public Map callAPI(String data) {

        Map result = new HashMap();
        String rtnStat = "00";  //정상

        try {

            //TODO:카드사 API 호출


            //정상으로 간주
            result.put("DATA", data);
            result.put("STAT",rtnStat );


        } catch (Exception e) {
            result.put("STAT", "999");
        }

        return result;

    }

}

