package com.tz.activiti;

import com.tz.activiti.config.SecurityUtil;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName AskLeaveTest_7x_full
 * @Description
 * @Date 17:58 2021/6/20
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AskLeaveTest_7x_full {
    private Logger logger = LoggerFactory.getLogger(AskLeaveTest_7x_full.class);

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    HistoryService historyService;

    @Test
    @Tag("test")
    public void askLeaveTest() {
        // 外系统的关联单号
        String businessKey = "ASK00001";
        String days = "5";
        //流程定义的key
        String processDefinitionKey = "askleave";
        //启动一个流程实例
        ProcessInstance processInstance = processRuntime.start(
                ProcessPayloadBuilder.start()
                        .withProcessDefinitionKey(processDefinitionKey) //key多次部署可能重复，需要加限定条件
                        .withName(processDefinitionKey + "流程实例名称")
                        .withVariable("days", 0) //初始化参数0，若流程定义中没有默认值会出错
                        .withBusinessKey(businessKey)
                        .build()
        );

        //user1 查找任务
        String assignee = "user1";

        securityUtil.logInAs(assignee);
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100));

        if (tasks.getContent().size() > 0) {
            Task task = tasks.getContent().get(0);
            //user1 处理任务，即填写请假单，把天数变量赋值 >3 天总监要加签
            //null 则为 候选任务，需要现claim
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(task.getId())
                        .build());
            }
            //处理任务并设置变量

            taskRuntime.complete(TaskPayloadBuilder
                    .complete()
                    .withTaskId(task.getId())
                    .build());
        } else {
            logger.info(assignee + " 无任务可审核");
        }

        //user2 主管查找任务并处理任务
        assignee = "user2";
        securityUtil.logInAs(assignee);
        tasks = taskRuntime.tasks(Pageable.of(0, 100));

        if (tasks.getContent().size() > 0) {
            Task task = tasks.getContent().get(0);
            //null 则为 候选任务，需要现claim
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(task.getId())
                        .build());
            }
            //处理任务
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("days", days);

            /**
             * bug 怀疑是 TaskRuntimeImpl.java 164行 那个 true应该是false
             */
            taskRuntime.complete(TaskPayloadBuilder
                    .complete()
                    .withTaskId(task.getId())
                    .withVariable("days", days) //bug?无效？
                    //.withVariables(variables) //bug？无效？
                    .build());

        } else {
            logger.info(assignee + " 无任务可审核");
        }

        //user4 总监查找任务并处理任务
        assignee = "user4";
        securityUtil.logInAs(assignee);
        tasks = taskRuntime.tasks(Pageable.of(0, 100));

        if (tasks.getContent().size() > 0) {
            Task task = tasks.getContent().get(0);
            //null 则为 候选任务，需要现claim
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(task.getId())
                        .build());
            }

            //处理任务
            taskRuntime.complete(TaskPayloadBuilder
                    .complete()
                    .withTaskId(task.getId())
                    .build());
        } else {
            logger.info(assignee + " 无任务可审核");
        }

        //user3 人事查找任务并处理任务
        assignee = "user3";
        securityUtil.logInAs(assignee);
        tasks = taskRuntime.tasks(Pageable.of(0, 100));

        if (tasks.getContent().size() > 0) {
            Task task = tasks.getContent().get(0);
            //null 则为 候选任务，需要现claim
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(task.getId())
                        .build());
            }
            //处理任务
            taskRuntime.complete(TaskPayloadBuilder
                    .complete()
                    .withTaskId(task.getId())
                    .build());
        } else {
            logger.info(assignee + " 无任务可审核");
        }

        //查看历史记录
        String processInstanceId = processInstance.getId();
        List<HistoricTaskInstance> HisList = historyService
                .createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().desc()
                .processInstanceId(processInstanceId) //流程实例ID条件
                .list();
        logger.info("");
        for (HistoricTaskInstance hi : HisList) {
            logger.info("=============================================================");
            logger.info("getId                 ：" + hi.getId());
            logger.info("getProcessDefinitionId：" + hi.getProcessDefinitionId());
            logger.info("getProcessInstanceId  ：" + hi.getProcessInstanceId());
            logger.info("getName               ：" + hi.getName());
            logger.info("getStartTime          ：" + hi.getStartTime());
            logger.info("getEndTime            ：" + hi.getEndTime());
        }
    }
}
