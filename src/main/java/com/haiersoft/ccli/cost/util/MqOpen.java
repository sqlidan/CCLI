package com.haiersoft.ccli.cost.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class MqOpen {


    static final String QUEUE_MAIL = "appid";

    @Bean
    MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = QUEUE_MAIL)
    public void onRegistrationMessageFromMailQueue(JSONObject object){
        Object date = object.get("date");
        Object data = object.get("data");
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(data);
        Object statementNo = jsonObject.get("StatementNo");
        Object costId = jsonObject.get("CostId");
        Object type = object.get("type");
        Object command = object.get("command");
        Object orgCode = object.get("orgCode");
//        GenSettlementDoc genSettlementDoc = new GenSettlementDoc();
//        genSettlementDoc.setDate(date.toString());
//        genSettlementDoc.setStatementNo(statementNo.toString());
//        genSettlementDoc.setCostId(costId.toString());
//        genSettlementDoc.setType(type.toString());
//        genSettlementDoc.setCommand(command.toString());
//        genSettlementDoc.setOrgCode(orgCode.toString());
    }




}
