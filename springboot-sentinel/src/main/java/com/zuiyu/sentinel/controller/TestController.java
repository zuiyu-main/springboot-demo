package com.zuiyu.sentinel.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName TestController
 * @Description
 * @Date 13:04 2022/6/12
 **/
@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        try (Entry entry = SphU.entry("hello")) {
            return "hello sentinel";
        } catch (BlockException e) {
            return "系统繁忙,请稍后";
        }
    }
//    @PostConstruct
//    public void initFlowRules(){
//        List<FlowRule> list = new LinkedList<>();
//        FlowRule rule = new FlowRule();
//        rule.setResource("hello");
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        rule.setCount(2);
//        list.add(rule);
//        FlowRuleManager.loadRules(list);
//    }
}
