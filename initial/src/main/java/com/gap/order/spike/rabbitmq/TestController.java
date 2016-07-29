package com.gap.order.spike.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/planReceipt",method = RequestMethod.POST)
    public void sendPlanReceipt(@RequestBody PlanReceipt planReceipt) {

        String separator = ".";
        String routingKey = new StringBuffer().append(planReceipt.getBrandId()).append(separator).append(planReceipt.getMarketId()).append(separator).append(planReceipt.getChannleId()).toString();
       rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("spring-boot-exchange",routingKey,planReceipt);
    }

//    @RabbitListener(queues = "rrQueue")
//    public void handleRRMessages(PlanReceipt planReceipt) {
//        LOGGER.info("Received PlanReceipt for RR {}",planReceipt);
//    }
//
//
//    @RabbitListener(queues = "tnrQueue")
//    public void handleTNRMessages(PlanReceipt planReceipt) {
//        LOGGER.info("Received PlanReceipt for TNR {}",planReceipt);
//    }

//    @RabbitListener(queues = "deadLetterQueue")
//    public void handleDeadMessages(PlanReceipt planReceipt) {
//        LOGGER.info("Received Dead Message {}",planReceipt);
//    }


}