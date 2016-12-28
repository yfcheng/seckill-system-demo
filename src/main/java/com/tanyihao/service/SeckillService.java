package com.tanyihao.service;

import com.tanyihao.dto.Exposer;
import com.tanyihao.dto.SeckillExecution;
import com.tanyihao.entity.Seckill;
import com.tanyihao.exception.RepeatKillException;
import com.tanyihao.exception.SeckillClosedException;
import com.tanyihao.exception.SeckillException;

import java.util.List;

/**
 * Created by tanhao on 16-12-28.
 */
public interface SeckillService {

    /**
     * 获得所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀地址
     * 否则输出系统时间秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillClosedException;


}
