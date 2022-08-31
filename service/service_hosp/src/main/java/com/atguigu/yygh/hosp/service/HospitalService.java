package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {
    //保存医院详情
    void saveHospital(Map<String, Object> paramMap);
    //查询医院详情
    Hospital getHospital(String hoscode);
}
