package com.mr.vo;

import com.mr.enums.ExceptionEnums;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionVo {
    private int status;
    private String message;
    private  Long timestamp;
    //提供了有参构造
    public  ExceptionVo(ExceptionEnums enums){
        this.status = enums.getStatus();
        this.message = enums.getMessage();
        this.timestamp = System.currentTimeMillis();
    }
}
