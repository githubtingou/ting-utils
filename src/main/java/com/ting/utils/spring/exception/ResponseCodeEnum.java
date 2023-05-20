package com.ting.utils.spring.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一返参枚举类
 *
 * @author ting
 * @version 1.0
 * @date 2023/5/20
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    SUCCESS(200, true, "处理成功"),
    ERROR(500, "处理失败"),

    ;


    private final Integer code;

    private boolean success;

    private final String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
