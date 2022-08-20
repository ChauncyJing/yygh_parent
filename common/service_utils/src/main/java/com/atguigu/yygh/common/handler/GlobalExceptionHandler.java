package com.atguigu.yygh.common.handler;



import com.atguigu.yygh.common.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


//统一异常处理控制器
@ControllerAdvice
//需要配合@ExceptionHandler使用。
//当将异常抛到controller时,可以对异常进行统一处理,规定返回的json格式或是跳转到一个错误页面
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    //@ExceptionHandler注解我们一般是用来自定义异常的。
    //可以认为它是一个异常拦截器（处理器）。
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error();
    }

    //实现特殊异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("特殊异常处理");
    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public R error(YyghException e){
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
