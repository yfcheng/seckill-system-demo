package com.tanyihao.dao;

/**
 * Created by tanhao on 16-12-27.
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细，可过滤重复 联合主键
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     * 根据Id查询successkilled并携带seckill
     * @param seckillId
     * @return
     */
    SuccessKilledDao queryByIdWithSeckill(long seckillId);

}
