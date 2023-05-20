package com.ting.utils.spring.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返参格式
 *
 * @author ting
 * @version 1.0
 * @date 2023/5/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO<T> {

    private Integer code;

    private boolean success;

    private String msg;

    private T data;

    public ResultDTO(Integer code, boolean success, String msg) {
        this.code = code;
        this.success = success;
        this.msg = msg;
    }

    public static ResultDTO<String> ok() {

        return build(ResponseCodeEnum.SUCCESS);
    }

    public static ResultDTO<String> build(ResponseCodeEnum codeEnum) {
        return build(codeEnum, "");
    }

    public static <T> ResultDTO<T> build(ResponseCodeEnum codeEnum, T t) {
        return new ResultDTO<>(codeEnum.getCode(), codeEnum.isSuccess(), codeEnum.getMsg(), t);
    }

    public static <T> ResultDTO<T> build(Integer code, boolean success, String msg, T t) {
        return new ResultDTO<>(code, success, msg, t);
    }


    public static ResultDTO<String> error() {
        return build(ResponseCodeEnum.ERROR);
    }

    public static ResultDTO<String> error(BusinessException exception) {
        return build(exception.getCode(), exception.isSuccess(), exception.getMsg(), "");
    }

    public static <T> ResultDTO<T> error(BusinessException exception, T data) {
        return build(exception.getCode(), exception.isSuccess(), exception.getMsg(), data);
    }
}
