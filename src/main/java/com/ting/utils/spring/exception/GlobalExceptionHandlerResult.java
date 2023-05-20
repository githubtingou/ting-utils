package com.ting.utils.spring.exception;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 功能描述: 全局异常捕捉 指定一个或多个注解，被这些注解所标记的 Controller 会被该 @ControllerAdvice 管理
 *
 * @author  ting
 * @date 2021/12/16
 */
@RestControllerAdvice(annotations = {RestController.class})
@Slf4j
public class GlobalExceptionHandlerResult extends ResponseEntityExceptionHandler {

    /**
     * 当出现非业务异常时通过这个方法捕捉
     *
     * @param resp
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultDTO runtimeExceptionHandler(HttpServletResponse resp,
                                             HttpServletRequest request,
                                             RuntimeException e) {
        try (OutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(JSON.toJSONString(ResultDTO.build(ResponseCodeEnum.ERROR)).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ie) {
            log.error(
                    String.format(
                            "GlobalExceptionHandler->runtimeExceptionHandler->RuntimeException:请求路径:%s，请求方地址:%s，请求方端口:%s",
                            request.getRequestURI(),
                            request.getRemoteHost(),
                            request.getRemotePort()
                    ),
                    ie
            );
        }
        log.error(String.format("GlobalExceptionHandler->runtimeExceptionHandler->RuntimeException:请求路径:%s，请求方地址:%s，请求方端口:%s",
                        request.getRequestURI(),
                        request.getRemoteHost(),
                        request.getRemotePort()),
                e
        );
        return ResultDTO.build(ResponseCodeEnum.ERROR);
    }

    /**
     * 捕捉业务异常时处理返参
     *
     * @param resp
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultDTO businessExceptionHandler(HttpServletResponse resp,
                                              HttpServletRequest request,
                                              BusinessException e) {
        try (OutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(JSON.toJSONString(ResultDTO.error(e)).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ie) {
            log.error(
                    String.format(
                            "GlobalExceptionHandler->businessExceptionHandler->IOException:请求路径:%s，请求方地址:%s，请求方端口:%s",
                            request.getRequestURI(),
                            request.getRemoteHost(),
                            request.getRemotePort()
                    ),
                    ie
            );
        }
        log.error(String.format(
                        "GlobalExceptionHandler->businessExceptionHandler->RuntimeException:请求路径:%s，请求方地址:%s，请求方端口:%s",
                        request.getRequestURI(),
                        request.getRemoteHost(),
                        request.getRemotePort()
                ),
                e);
        return ResultDTO.error(e);
    }

    /**
     * 对校验字段的异常捕捉针对于@Validated注解进行校验
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorMsg, headers, HttpStatus.OK);
    }

}
