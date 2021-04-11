package com.tz.tkmapper.service.impl;

import com.tz.tkmapper.bean.TestSys;
import com.tz.tkmapper.dao.TestSysMapper;
import com.tz.tkmapper.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName TestServiceImpl
 * @Description
 * @Date 10:44 2021/3/26
 **/
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestSysMapper testSysMapper;

    @Override
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void testTransactional() {

        TestSys testSys = new TestSys();
        testSys.setId(1);
        testSys.setName("name1");
        testSysMapper.insert(testSys);
//        if (true){
//            throw new RuntimeException();
//        }
    }
}
