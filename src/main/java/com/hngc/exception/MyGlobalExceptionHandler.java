package com.hngc.exception;

import com.hngc.common.R;
import com.hngc.utils.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
//定义处理哪些类
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class MyGlobalExceptionHandler {
    /**
     * 异常处理方法
     *
     * @param e exception
     * @return R
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage());

        if (e.getMessage().contains("Duplicate entry")) {
            String[] split = e.getMessage().split(" ");
            String msg = split[2] + "已存在！";
            return R.error(msg);
        }
        return R.error("未知错误！");
    }

    /**
     * 自定义业务异常处理
     *
     * @param e e
     * @return R
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e) {
        log.error(e.toString());
        return R.error(e.getMessage());
    }

    @ExceptionHandler(MySystemException.class)
    public R doSystemException(MySystemException ex) {
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        log.error("========================MySystemException========================================\n" + ex + "\n" + ex.getCode());

        return R.success(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MyBusinessException.class)
    public R doBusinessException(MyBusinessException ex) {
        log.error("========================MyBusinessException========================================\n" + ex + "\n" + ex.getCode());

        return R.success(ex.getCode(), ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public R doOtherException(Exception ex) {
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        log.error("========================Exception========================================\n" + ex);
        log.error(ex.toString());
        return R.success(Code.SYSTEM_UNKNOWN_ERR, "系统繁忙，请稍后再试！");
    }
    //除了自定义的异常处理器，保留对Exception类型的异常处理，用于处理非预期的异常
    @ExceptionHandler(AccessDeniedException.class)
    public R doAccessDeniedException(Exception ex) {
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        log.error("========================AccessDeniedException========================================\n" + ex);
        log.error(ex.toString());
        return R.error(Code.PERMISSION_ERR, "权限不足！");
    }
}
