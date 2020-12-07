package com.macro.mall.tiny.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.tiny.mbg.mapper.PmsBrandMapper;
import com.macro.mall.tiny.mbg.model.PmsBrand;
import com.macro.mall.tiny.mbg.model.PmsBrandExample;
import com.macro.mall.tiny.service.PmsBrandService;
import com.macro.mall.tiny.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * PmsBrandService实现类
 * Created by macro on 2019/4/19.
 */
@Service
public class PmsBrandServiceImpl implements PmsBrandService{

    private static final String DICTIONARY_TREE_REDIS_KEY = "PMS_SERVICE_DICTIONARY_TREE_REDIS";

    private static final long DICTIONARY_TREE_REDIS_DURATION = 1 * 30;

    @Resource
    private PmsBrandMapper brandMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<PmsBrand> listAllBrand() {
        String s = redisService.get(DICTIONARY_TREE_REDIS_KEY);
        if (StrUtil.isNotEmpty(s)) {
            JSONArray jsonArray = JSONUtil.parseArray(s);
            return JSONUtil.toList(jsonArray,PmsBrand.class);
        }else {
            List<PmsBrand> pmsBrands = brandMapper.selectByExample(new PmsBrandExample());
            stringRedisTemplate.opsForValue().setIfAbsent(DICTIONARY_TREE_REDIS_KEY, JSONUtil.toJsonStr(pmsBrands), DICTIONARY_TREE_REDIS_DURATION, TimeUnit.SECONDS);
            return pmsBrands;
        }
    }

    @Override
    public int createBrand(PmsBrand brand) {
        return brandMapper.insertSelective(brand);
    }

    @Override
    public int updateBrand(Long id, PmsBrand brand) {
        brand.setId(id);
        return brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public int deleteBrand(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PmsBrand> listBrand(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
