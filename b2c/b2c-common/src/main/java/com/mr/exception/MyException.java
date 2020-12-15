package com.mr.exception;

import com.mr.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//有参构造
@AllArgsConstructor
//无参构造
@NoArgsConstructor
public class MyException  extends RuntimeException{
    //枚举类的调用
    private ExceptionEnums exceptionEnums;

}
