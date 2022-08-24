package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.R;
import com.atguigu.yygh.common.handler.YyghException;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


//医院接口设置
//description： 注释说明这个类
@Api(description = "医院设置接口")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin //跨域支持
public class HospitalSetController {

    @Autowired //@Autowired一样
    private HospitalSetService hospitalSetService;


    //在hops模块实现模拟登录接口
    @ApiOperation(value = "模拟登录")
    @PostMapping("login")
    //{"code":20000,"data":{"token":"admin-token"}} 登录请求报文内容
    public  R login(){
        return R.ok().data("token","admin-token");
    }

    //模拟获取用户信息
    @ApiOperation(value = "模拟获取用户信息")
    @GetMapping("info")
    public R info(){
        //创建集合 把登录信息一一对应封装到map集合里
        HashMap<String, Object> map = new HashMap<>();
        map.put("roles","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return R.ok().data(map);
    }


    @ApiOperation(value = "医院设置列表")
    //@ApiOperation
    //value: 字段说明
    //notes: 注释说明
    //httpMethod: 说明这个方法被请求的方式
    //response: 方法的返回值的类型
    @GetMapping("findAll")
    public R findAll() {
        //统一添加异常 测试异常
        try {
            int i = 10/0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new YyghException(20001,"自定义异常处理");
        }
        List<HospitalSet> list = hospitalSetService.list();
        return R.ok().data("list",list);
    }

    @ApiOperation(value = "根据id删除医院设置")
    @DeleteMapping("{id}")
    public R removeById(@PathVariable String id){
        hospitalSetService.removeById(id);
        return R.ok();
    }

    //分页查询方法
    @ApiOperation(value = "分页医院设置列表")
    @RequestMapping(value = "{page}/{limit}",method = RequestMethod.GET)
    public R pageList(@PathVariable Long page,@PathVariable Long limit){
        //前端传过来的数据封装到Page集合里面
        Page<HospitalSet> pageParam = new Page<>(page,limit);
        //把封装好的集合变成Model模型
        Page<HospitalSet> pageModel = hospitalSetService.page(pageParam);
        return R.ok().data("total",pageModel.getTotal()).data("list",pageModel.getRecords());
        //返回结果 总记录数 and 集合数据
    }

    //实现带条件的分页查询
    @ApiOperation(value = "带条件的分页查询医院设置")
    @RequestMapping(value = "pageQuery/{page}/{limit}",method = RequestMethod.POST)
    public R pageQuery(@PathVariable long page,@PathVariable Long limit,@RequestBody
    HospitalSetQueryVo hospitalSetQueryVo)
    //@RequestBody主要***用来接收前端传递给后端的json字符串中的数据***的
    {
        //取出参数
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        //拼写条件查询
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        //判断取出的参数是否为空
        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hosname);
        }
        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hoscode);
        }

        //封装分页参数
        Page<HospitalSet> pageParam = new Page<>(page,limit);
        Page<HospitalSet> pageModel = hospitalSetService.page(pageParam,wrapper);
        return R.ok().data("total",pageModel.getTotal()).data("list",pageModel.getRecords());

    }

    //新增医院设置
    @ApiOperation(value = "新增医院设置")
    @PostMapping("save")
    public R save(@RequestBody HospitalSet hospitalSet){
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //修改医院设置
    //1.先根据id查询医院信息
    @ApiOperation(value = "根据id查询医院设置")
    @GetMapping("getById/{id}")
    public R getByid(@PathVariable Long id){
        HospitalSet hospitalSetServiceById = hospitalSetService.getById(id);
        return R.ok().data("hospitalSet",hospitalSetServiceById);
    }

    //2.根据查询的信息修改医院设置
    @ApiOperation(value = "修改医院设置")
    @PostMapping("update")
    public R update(@RequestBody HospitalSet hospitalSet){
        boolean update = hospitalSetService.updateById(hospitalSet);
        if(update){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //批量删除医院设置
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("batchRemove")
    public R batchRemoveHospitalSet(@RequestBody List<Long> idList){
        boolean removes = hospitalSetService.removeByIds(idList);
        if(removes){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //医院设置锁定和解锁
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public R lockHospitalSet(@PathVariable Long id,@PathVariable Integer status){
        //1.根据id查询出医院
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //2.跟新状态
        hospitalSet.setStatus(status);
        boolean update = hospitalSetService.updateById(hospitalSet);
        if(update){
            return R.ok();
        }else {
            return R.error();
        }
    }


}
