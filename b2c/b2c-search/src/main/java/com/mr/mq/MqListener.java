package com.mr.mq;

import com.mr.dao.GoodRepository;
import com.mr.pojo.Goods;
import com.mr.service.GoodsService;
import com.mr.utils.MqMessageConstant;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
mq队列监听
 **/
@Component
public class MqListener {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodRepository goodRepository;

     @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqMessageConstant.SPU_QUEUE_SEARCH_SAVE, durable = "true"),
            exchange = @Exchange(
                    value = MqMessageConstant.SPU_EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {MqMessageConstant.SPU_ROUT_KEY_SAVE}))
     public void saveListen(Long id){
         Goods good = goodsService.getGoods(id);
         goodRepository.save(good);
         System.out.println("保存");
     }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqMessageConstant.SPU_QUEUE_SEARCH_UPDATE, durable = "true"),
            exchange = @Exchange(
                    value = MqMessageConstant.SPU_EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {MqMessageConstant.SPU_ROUT_KEY_UPDATE}))
    public void updateListen(Long id){
        Goods good = goodsService.getGoods(id);
        goodRepository.save(good);
        System.out.println("修改");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqMessageConstant.SPU_QUEUE_SEARCH_DELETE, durable = "true"),
            exchange = @Exchange(
                    value = MqMessageConstant.SPU_EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {MqMessageConstant.SPU_ROUT_KEY_DELETE}))
    public void deleteListen(Long id){
        Goods good = goodsService.getGoods(id);
        goodRepository.save(good);
        System.out.println("删除");
    }

}