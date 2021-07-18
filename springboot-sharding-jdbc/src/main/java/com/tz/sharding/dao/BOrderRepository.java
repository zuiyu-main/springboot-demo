package com.tz.sharding.dao;

import com.tz.sharding.entity.BOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName BOrderRepository
 * @Description
 * @Date 21:38 2021/7/18
 **/
public interface BOrderRepository extends JpaRepository<BOrder, Long> {
}
