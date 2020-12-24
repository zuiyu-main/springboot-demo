package com.tz.springbootrabbitmq.service;

import java.io.IOException;
import java.util.Map;

/**
 * @author liBai
 * @Classname HelloService
 * @Description TODO
 * @Date 2019-06-02 10:49
 */
public interface HelloService {

    Map<String, Object> count() throws IOException;
}
