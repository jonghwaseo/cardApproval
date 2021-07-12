package com.jjong.cardApproval.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

    /**
     * 관리번호채번
     */
    public static String getManageId() {
        return RandomStringUtils.random(20, true, true);
    }
}
