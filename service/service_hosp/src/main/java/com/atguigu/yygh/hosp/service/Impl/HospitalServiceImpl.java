package com.atguigu.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void saveHospital(Map<String, Object> paramMap) {
        //1数据转型paramMap=>json =>Hospital
        String jsonString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);
        //2根据hoscode查询医院信息
        Hospital targetHospital =
                hospitalRepository.getByHoscode(hospital.getHoscode());
        if(targetHospital!=null){
            //3如果医院存在，更新数据
            hospital.setId(targetHospital.getId());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setStatus(targetHospital.getStatus());
            hospital.setIsDeleted(targetHospital.getIsDeleted());
            hospitalRepository.save(hospital);
        }else{
            //4如果医院不存在，新增数据
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setStatus(0);
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }
    //查询医院
    @Override
    public Hospital getHospital(String hoscode) {
        Hospital hospital = hospitalRepository.getByHoscode(hoscode);
        return hospital;

    }
}
