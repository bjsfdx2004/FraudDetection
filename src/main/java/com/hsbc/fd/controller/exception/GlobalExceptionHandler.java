package com.hsbc.fd.controller.exception;

import com.hsbc.fd.controller.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 统一错误转换
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R error(Exception e, WebRequest request) {
        log.error("[{}]Unexpected exception occurred: {}", request.getDescription(false), getExceptionInfo(e));
        return R.error().message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    private String getExceptionInfo(Exception e) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(byteArrayOutputStream));
            return byteArrayOutputStream.toString();
        } catch (Exception e2) {
            return e != null ? e.getMessage() : null;
        }
    }
}
