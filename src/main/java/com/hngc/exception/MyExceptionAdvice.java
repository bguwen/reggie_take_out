//package com.hngc.exception;
//
//import com.hngc.common.R;
//import com.hngc.utils.Code;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//
//@Slf4j
////@ControllerAdvice用于标识当前类为异常处理器
//@RestControllerAdvice
//public class MyExceptionAdvice {
//    //@ExceptionHandler用于设置当前处理器类对应的异常类型
//    @ExceptionHandler(MySystemException.class)
//    public R doSystemException(MySystemException ex) {
//        //记录日志
//        //发送消息给运维
//        //发送邮件给开发人员,ex对象发送给开发人员
//        log.error("========================MySystemException========================================\n"+ex.getMessage()+"\n"+ex.getCode());
//
//        return R.success(ex.getCode(), ex.getMessage());
//    }
//
//    @ExceptionHandler(MyBusinessException.class)
//    public R doBusinessException(MyBusinessException ex) {
//        log.error("========================MyBusinessException========================================\n"+ex.getMessage()+"\n"+ex.getCode());
//
//        return R.success(ex.getCode(), ex.getMessage());
//    }
//
//    //除了自定义的异常处理器，保留对Exception类型的异常处理，用于处理非预期的异常
//    @ExceptionHandler(Exception.class)
//    public R doOtherException(Exception ex) {
//        //记录日志
//        //发送消息给运维
//        //发送邮件给开发人员,ex对象发送给开发人员
//        log.error("========================Exception========================================\n"+ex.getMessage());
//
//        return R.success(Code.SYSTEM_UNKNOWN_ERR, "系统繁忙，请稍后再试！");
//    }
//
//
////    @ExceptionHandler(AuthenticationException.class)
////    public void error(AuthenticationException e) {
////        throw e;
////    }
////
////
////    @ExceptionHandler(AccessDeniedException.class)
////    public void error(AccessDeniedException e) {
////        throw e;
////    }
//
//}