package com.atguigu.yygh.common.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor ： 生成一个无参数的构造方法
//@AllArgsContructor： 会生成一个包含所有变量的构造方法，默认生成的方法是 public 的
//@RequiredArgsConstructor： 会生成一个包含常量，和标识了NotNull的变量的构造方法。
// 生成的构造方法是私有的private。（可能带参数也可能不带参数）
public class YyghException extends RuntimeException{
    //状态码
    private Integer code;
    //异常信息
    private String msg;

}
