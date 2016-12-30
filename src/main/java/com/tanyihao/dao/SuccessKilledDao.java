package com.tanyihao.dao;

import com.tanyihao.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

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
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据Id查询successkilled并携带seckill
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);


}
