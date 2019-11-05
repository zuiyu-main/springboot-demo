package com.tz.springbootrabbitmq.service;

/**
 * @author tz
 * @Classname StompService
 * @Description
 * @Date 2019-09-18 18:59
 */
public interface StompService {
    <T> void connectAndSend(String dest, T toSend);
}
