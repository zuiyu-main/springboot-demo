package com.tz.activiti.config;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName DefaultTaskListener
 * @Description
 * @Date 17:57 2021/6/20
 **/
public class DefaultTaskListener implements TaskListener {
    private Logger logger = LoggerFactory.getLogger(DefaultTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        logger.info("任务监听器-流程实例ID: " + delegateTask.getProcessInstanceId()
                + " 执行人: " + delegateTask.getAssignee());
    }

}
