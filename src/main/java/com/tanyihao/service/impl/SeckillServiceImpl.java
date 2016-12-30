package com.tanyihao.service.impl;

import com.tanyihao.dao.SeckillDao;
import com.tanyihao.dao.SuccessKilledDao;
import com.tanyihao.dao.cache.RedisDao;
import com.tanyihao.dto.Exposer;
import com.tanyihao.dto.SeckillExecution;
import com.tanyihao.entity.Seckill;
import com.tanyihao.entity.SuccessKilled;
import com.tanyihao.enums.SeckillStateEnum;
import com.tanyihao.exception.RepeatKillException;
import com.tanyihao.exception.SeckillClosedException;
import com.tanyihao.exception.SeckillException;
import com.tanyihao.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanhao on 16-12-28.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    private final String slat = "fadfliojvlpperqczvn.fadsjoqe(*&%^$^##^";
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化,一致性维护超时
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null)
                return new Exposer(false,seckillId);
            else{
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime()||
                nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillClosedException {
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();
        try{
            int insertSuccessKilled = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if(insertSuccessKilled <= 0){
                throw new RepeatKillException("seckill repeat");
            }else{
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if(updateCount <= 0){
                    throw new SeckillClosedException("seckill is closed");
                }else{
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }

        }catch (SeckillClosedException e1){
            throw e1;
        }catch(RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5){
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime  = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result", null);
        try{
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map,"result",-2);
            if(result == 1){
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
            }else{
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
