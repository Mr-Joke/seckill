-- 秒杀存储过程
DELIMITER $$ -- Console ; 转换为 $$
-- 定义存储过程
-- 参数：in 输入参数，out 输出参数
-- row_count():返回上一条修改类型的sql（update,delete,insert)影响的行数
-- row_count(): 0：未修改数据，>0 ：表示修改的行数， <0 ：sql错误/未执行修改sql
CREATE PROCEDURE `seckill`.`execute_seckill`
  (IN v_seckill_id int,IN v_phone varchar(20),
    IN v_kill_time timestamp,OUT r_result int)
  BEGIN
    DECLARE affect_count int DEFAULT 0;
    START TRANSACTION;
    insert ignore into tb_record
      (seckill_id,record_phone,record_seckill_time)
      values (v_seckill_id,v_phone,v_kill_time);
    select row_count() into affect_count;
    IF (affect_count = 0) THEN
      ROLLBACK;
      SET r_result = -1; -- 重复秒杀
    ELSEIF (affect_count < 0) THEN
      ROLLBACK;
      SET r_result = -2; -- 系统异常
    ELSE
      update tb_seckill
      set seckill_quantity = seckill_quantity - 1
      where seckill_id = v_seckill_id
        and seckill_start_time <= v_kill_time
        and seckill_end_time >= v_kill_time
        and seckill_quantity > 0;
      select row_count() into affect_count;
      IF (affect_count = 0) THEN
        ROLLBACK;
        SET r_result = 1; -- 秒杀关闭
      ELSEIF (affect_count < 0) THEN
        ROLLBACK;
        SET r_result = -2; -- 系统异常
      ELSE
        COMMIT;
        SET r_result = 0;
      END IF;
    END IF;
  END
$$
-- 存储过程定义结束
DELIMITER ;

set @r_result = -3;
-- 执行存储过程
call execute_seckill(3,'13544494187',now(),@r_result);
-- 获取结果
select @r_result;

-- 存储过程
-- 1：存储过程优化：事务行级锁持有时间
-- 2：不要过多地依赖存储过程
-- 3：简单的逻辑可以运用存储过程
-- 4：QPS：一个秒杀单6000/QPS
