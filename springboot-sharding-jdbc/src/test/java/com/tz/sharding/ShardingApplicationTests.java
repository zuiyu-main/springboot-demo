package com.tz.sharding;

import com.tz.sharding.dao.BOrderRepository;
import com.tz.sharding.dao.PositionDetailRepository;
import com.tz.sharding.dao.PositionRepository;
import com.tz.sharding.entity.BOrder;
import com.tz.sharding.entity.Position;
import com.tz.sharding.entity.PositionDetail;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShardingApplicationTests {

    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private PositionDetailRepository positionDetailRepository;
    @Autowired
    private BOrderRepository bOrderRepository;

    @Test
    public void contextLoads() {
        for (int i = 1; i < 20; i++) {
            Position position = new Position();
            position.setId(i);
            position.setName("tz:" + i);
            position.setSalary("1000000");
            position.setCity("北京");
            positionRepository.save(position);
        }

    }

    @Test
    public void contextLoadsDetail() {
        for (int i = 1; i < 20; i++) {
            Position position = new Position();
//            position.setId(i);
            position.setName("tz:" + i);
            position.setSalary("1000000");
            position.setCity("北京");
            positionRepository.save(position);
            PositionDetail detail = new PositionDetail();
            detail.setPid(position.getId());
            detail.setDescription("this is a message :" + i);

            positionDetailRepository.save(detail);
        }

    }

    @Test
    public void testFindById() {
        Object positionById = positionRepository.findPositionById(623653368602034177L);
        Object[] objects = (Object[]) positionById;
        System.out.println(objects[0] + " " + objects[1] + " " + objects[2] + " " + objects[3] + " " + objects[4]);
    }

    //     @Repeat(value = 100),重复执行多少次
    // TODO: 2021/7/18 repeat 注解未生效
    @Test
    @Repeat(100)
    public void testShardingBOrder() {
        Random random = new Random();
        int companyId = random.nextInt(10);
        BOrder order = new BOrder();
        order.setCompanyId(companyId);
        order.setDel(false);
        order.setPositionId(11231231);
        order.setUserId(222);
        order.setPublishUserId(21313);
        order.setResumeType(1);
        order.setStatus("AUTO");
        order.setCreateTime(new Date());
        order.setOperateTime(new Date());
        order.setWorkYear("2");
        order.setName("zuiyu");
        order.setPositionName("JAVA");
        order.setResumeId(2123);
        bOrderRepository.save(order);

    }
}
