package com.tz.sharding.dao;

import com.tz.sharding.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName PositionReposityory
 * @Description
 * @Date 10:27 2021/7/17
 **/
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query(nativeQuery = true, value = "select p.id,p.name,p.salary,p.city,pd.description from position p join position_detail pd on (p.id=pd.pid) where p.id=:id")
    public Object findPositionById(@Param("id") long id);
}
