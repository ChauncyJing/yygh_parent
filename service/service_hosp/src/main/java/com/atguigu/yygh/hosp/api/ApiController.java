package com.atguigu.yygh.hosp.api;

import com.atguigu.yygh.common.Result;
import com.atguigu.yygh.common.handler.YyghException;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.utils.HttpRequestHelper;
import com.atguigu.yygh.hosp.utils.MD5;
import com.atguigu.yygh.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "医院管理API接口")
@RestController
//@Controller:在对应的方法上，视图解析器可以解析return 的jsp,html页面，并且跳转到相应页面
//若返回json等内容到页面，则需要加@ResponseBody注解
//@RestController:相当于@Controller+@ResponseBody两个注解的结合，
// 返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;


    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        //1从request取出参数，转型
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        //2签名校验
        //2.1获取参数
        String hoscode = (String) paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        //2.2根据hoscode查询签名、MD5加密
        String targetSign = hospitalSetService.getSignKey(hoscode);
        String targetSignMD5 = MD5.encrypt(targetSign);
        //2.3校验签名
        System.out.println("sign = " + sign);
        System.out.println("targetSignMD5 = " + targetSignMD5);
        if(!sign.equals(targetSignMD5)){
            throw new YyghException(20001,"签名校验失败");
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = (String)paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);

        //3调用service方法实现保存医院信息
        hospitalService.saveHospital(paramMap);
        return Result.ok();
    }
    //查询医院信息
    @ApiOperation(value = "获取医院信息")
    @PostMapping("hospital/show")
    public Result hospital(HttpServletRequest request){
        //1.从request中取出参数 然后转型
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(parameterMap);
        //2.取出参数
        String hoscode = (String)stringObjectMap.get("hoscode");
        String sign = (String) stringObjectMap.get("sign");
        //根据hoscode查询医院信息
        Hospital hospital = hospitalService.getHospital(hoscode);
        return Result.ok(hospital);
    }
    @ApiOperation(value = "上传科室")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //从request中获取参数转型
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        //调用保存方法
        departmentService.saveDepartment(paramMap);
        return Result.ok();
    }


}