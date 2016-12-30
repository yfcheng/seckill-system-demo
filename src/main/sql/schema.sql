CREATE DATABASE seckill;

USE seckill;
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` INT NOT NULL COMMENT '商品数量',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
`end_time` TIMESTAMP NOT NULL  COMMENT '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET='utf8' COMMENT='秒杀库存表';

INSERT INTO seckill(name, number, start_time, end_time)
    VALUES
      ('iphone秒杀', 150, '2016-12-27 12:0:0', '2016-12-29 12:0:0'),
      ('mate9秒杀', 150, '2016-12-27 12:0:0', '2016-12-29 12:0:0'),
      ('macbook秒杀', 150, '2016-12-27 12:0:0', '2016-12-29 12:0:0'),
      ('matebook秒杀', 150, '2016-12-27 12:0:0', '2016-12-29 12:0:0');

-- 秒杀成功明细表
create table  success_killed(
  `seckill_id` bigint not null comment '商品id',
  `user_phone` bigint not null comment '用户手机号',
  `state` TINYINT not null DEFAULT -1 COMMENT '状态表示',
  `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id, user_phone),
  KEY idx_create_time(create_time)
)ENGINE=InnoDB  DEFAULT CHARSET='utf8' COMMENT='秒杀成功明细表';

ALTER TABLE success_killed MODIFY `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 秒杀执行存储过程
DELIMITER $$
CREATE PROCEDURE `seckill`.`execute_seckill`
  (in v_seckill_id BIGINT, in v_phone BIGINT,in v_kill_time TIMESTAMP,out r_result INT)
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed(seckill_id, user_phone,create_time)
      VALUES (v_seckill_id,v_phone,v_kill_time);
    SELECT row_count() INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK ;
      SET r_result = -1;
    ELSEIF (insert_count < 0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill SET number = number -1
      WHERE seckill_id = v_seckill_id
      AND end_time > v_kill_time
      AND start_time < v_kill_time
      AND seckill.number > 0;
      SELECT row_count() into insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK ;
        SET r_result = 0;
      ELSEIF (insert_count< 0) THEN
        ROLLBACK ;
        SET r_result = -2;
      ELSE
        COMMIT ;
        set r_result = 1;
      END IF ;
    END IF ;

  END ;
$$

DELIMITER ;
SET @r_result = -3;
CALL execute_seckill(1003,)