-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
USE seckill;

-- 创建数据库表，秒杀商品表
DROP TABLE IF EXISTS tb_seckill;
CREATE TABLE tb_seckill (
	`seckill_id` INT(4) NOT NULL AUTO_INCREMENT COMMENT '秒杀商品id',
    `seckill_name` VARCHAR(255) NOT NULL COMMENT '秒杀商品名称',
    `seckill_quantity` INT(5) NOT NULL COMMENT '秒杀商品的库存',
    `seckill_start_time` DATETIME NOT NULL COMMENT '秒杀开启的时间',
    `seckill_end_time` DATETIME NOT NULL COMMENT '秒杀关闭的时间',
    `seckill_create_time` DATETIME NOT NULL COMMENT '秒杀商品上架时间',
    PRIMARY KEY (`seckill_id`)
)ENGINE=Innodb AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 COMMENT '秒杀商品表';

-- 插入测试数据
INSERT INTO tb_seckill (seckill_name,seckill_quantity,seckill_start_time,seckill_end_time,seckill_create_time) VALUES ('100元秒杀大冬瓜',100,'2017-09-06 00:00:00','2017-09-07 00:00:00','2017-09-05 00:00:00');
INSERT INTO tb_seckill (seckill_name,seckill_quantity,seckill_start_time,seckill_end_time,seckill_create_time) VALUES ('1000元秒iPad',200,'2017-09-07 00:00:00','2017-09-08 00:00:00','2017-09-05 00:00:00');
INSERT INTO tb_seckill (seckill_name,seckill_quantity,seckill_start_time,seckill_end_time,seckill_create_time) VALUES ('2000元秒杀iPhone8',300,'2017-09-08 00:00:00','2017-09-09 00:00:00','2017-09-05 00:00:00');
INSERT INTO tb_seckill (seckill_name,seckill_quantity,seckill_start_time,seckill_end_time,seckill_create_time) VALUES ('0元秒杀小冬瓜',1000,'2017-09-09 00:00:00','2017-09-10 00:00:00','2017-09-05 00:00:00');

-- 创建数据库表，用户秒杀明细表
DROP TABLE IF EXISTS tb_record;
CREATE TABLE tb_record (
	`seckill_id` INT(4) NOT NULL COMMENT '用户秒杀商品的id',
    `record_phone` VARCHAR(20) NOT NULL COMMENT '用户的手机号码',
    `record_status` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标志：-1：无效 0：成功 1：已付款 2：已发货',
    `record_seckill_count` TINYINT NOT NULL DEFAULT 1 COMMENT '用户秒杀的数量',
    `record_seckill_time` DATETIME NOT NULL COMMENT '用户秒杀的时间',
    PRIMARY KEY (`seckill_id`,`record_phone`)
)ENGINE=Innodb DEFAULT CHARSET=UTF8 COMMENT '用户秒杀明细表';