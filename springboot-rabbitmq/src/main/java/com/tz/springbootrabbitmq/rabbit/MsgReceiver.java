package com.tz.springbootrabbitmq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tz
 * @Classname MsgReceiver
 * @Description 消费者
 * @Date 2019-09-18 17:45
 */
@Component
@Slf4j
public class MsgReceiver {
//    @RabbitListener(queues = RabbitConfig.QUEUE_A)
//    @RabbitHandler
//    public void process(String content) {
//        log.info("接收处理队列A当中的消息： " + content);
//    }
//    @RabbitListener(queues = RabbitConfig.QUEUE_A)
//    @RabbitHandler
//    public void processB(String content) {
//        log.info("处理器two接收处理队列A当中的消息： " + content);
//    }
//    @RabbitListener(queues = "test.1")
//    @RabbitHandler
//    public void processC(String content) {
//        log.info("处理器test1的消息： " + content);
//    }

//    @RabbitListener(queues = "test.2")
//    @RabbitHandler
//    public void processD(String content) {
//        log.info("处理器test2的消息： " + content);
//    }
//
//    @RabbitListener(queues = "test.2")
//    @RabbitHandler
//    public void processE(String content) {
//        log.info("处理器test3的消息： " + content);
//    }
//    @RabbitListener(queues = "greetings")
//    @RabbitHandler
//    public void processE(Message content) {
//        log.info("处理器testE的消息： " + content.getBody());
//    }

//    @RabbitListener(queues = RabbitConfig.QUEUE_PRIORITY)
//    @RabbitHandler
//    public void priority(String content) {
//        log.info("交换机 test 的消息： " + content);
//    }

//    @RabbitListener(queues = RabbitConfig.QUEUE_PRIORITY_2)
//    @RabbitHandler
//    public void QUEUE_PRIORITY_2(String content) {
//        log.info("处理器 QUEUE_PRIORITY_2 的消息： " + content);
//    }
}
