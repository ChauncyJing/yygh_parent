package com.atguigu.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public void saveDepartment(Map<String, Object> paramMap) {
        //1.数据转型 paramMap转json转Department
        String jsonString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(jsonString, Department.class);
        //根据hoscode和depcode查询科室信息
        Department targetDepartment =
                departmentRepository.getByHoscodeAndDepcode(
                        department.getHoscode(),department.getDepcode());
        //3有数据更新
        if(targetDepartment!=null){
            department.setId(targetDepartment.getId());
            department.setCreateTime(targetDepartment.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else{
            //4没数据新增
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }

    }
}
