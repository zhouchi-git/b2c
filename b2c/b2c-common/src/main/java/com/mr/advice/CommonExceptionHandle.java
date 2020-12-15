package com.mr.advice;

import com.mr.exception.MyException;
import com.mr.vo.ExceptionVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
//controller的通知
public class CommonExceptionHandle {
    @ExceptionHandler(MyException.class)//获取异常
    public ResponseEntity<ExceptionVo> handle(MyException e){//自定义异常
        System.out.println("执行了Handle!!!");
        //返回前台的json对象
        return  ResponseEntity.status(e.getExceptionEnums().getStatus()).body(new ExceptionVo(e.getExceptionEnums()));
    }
}
