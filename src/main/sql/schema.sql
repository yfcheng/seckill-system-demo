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