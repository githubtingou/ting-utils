package com.ting.utils.spring.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author ting
 * @version 1.0
 * @date 2023/5/20
 */
@Getter
public class BusinessException extends Exception {
    private final Integer code;

    private final String msg;

    private final boolean success;

    public BusinessException(Integer code, boolean success, String msg) {
        super("code:" + code + ",success" + success + ",msg" + msg);
        this.code = code;
        this.success = success;
        this.msg = msg;
    }

}
