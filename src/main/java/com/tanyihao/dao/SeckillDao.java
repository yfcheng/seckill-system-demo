package com.tanyihao.dao;

import com.tanyihao.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by tanhao on 16-12-27.
 */
public interface SeckillDao {

    /**
     * 减少库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(long seckillId, Date killTime);

    /**
     * 查询商品
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(int offset, int limit);
}
