package com.atguigu.yygh.cmn.service.imp;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.handler.YyghException;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictListener dictListener;

    @Override  //根据数据id查询子数据列表
    public List<Dict> findChildData(Long id) {
        //根据id查询子级别数据
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);

        //2遍历集合，查询是否有子级别数据
        for (Dict dict : dictList) {
            boolean isChildren = this.isChildren(dict.getId());
            dict.setHasChildren(isChildren);
        }
        return dictList;
    }

    //查询是否有子级别数据
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0;
    }
    //导出
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            //1设置response基础参数
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();

            //2查询字典所有数据
            List<Dict> dictList = baseMapper.selectList(null);
            //3转化类型List<Dict>=>List<DictEeVo>
            List<DictEeVo> dictVoList = new ArrayList<>();
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict,dictEeVo);
                dictVoList.add(dictEeVo);
            }
            //4借助工具导出文件
            EasyExcel.write(outputStream, DictEeVo.class)
                    .sheet("数据字典").doWrite(dictVoList);

        } catch (IOException e) {
            e.printStackTrace();
            throw new YyghException(20001,"导出文件失败");
        }

    }

    @Override
    public void importData(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream,DictEeVo.class,
                    dictListener).sheet().doRead();
            //new DictListener(baseMapper)
        } catch (IOException e) {
            e.printStackTrace();
            throw new YyghException(20001,"导入数据失败");
        }

    }


}
