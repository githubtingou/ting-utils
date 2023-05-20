package com.ting.utils.common;

import java.util.UUID;

/**
 * uuid工具类
 *
 * @author ting
 * @date 2021/12/24
 **/
public class UUIDUtil {

    /**
     * 返回去掉-的uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
