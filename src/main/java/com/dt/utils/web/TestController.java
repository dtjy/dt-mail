package com.dt.utils.web;

import com.dt.utils.service.MailSendService;
import com.dt.utils.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author jiangyao
 * @Date 2019/6/27 13:35
 **/
@Controller
@RequestMapping("/test")
public class TestController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    MailSendService mailSendService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/t1")
    @ResponseBody
    public String scheduled(){
        mailSendService.sendSimpleMail("","","");
        return "S";
    }

    public static Long s  =0l;

    @RequestMapping("/t2")
    @ResponseBody
    public String t2(){
        redisTemplate.watch("product_0");
        Integer count = Integer.parseInt(redisTemplate.opsForValue().get("product_0").toString());
        System.out.println("剩余："+count);
        if (count>0){
            redisTemplate.multi();
            redisTemplate.opsForValue().increment("product_0",-1);
            List<Object> exec = redisTemplate.exec();
            if (CollectionUtils.isEmpty(exec)) {
                System.out.println("---> 抢购失败，继续抢购");
                return "抢购失败";
            }else {
                System.out.println("---》》》》》》》》》》抢购成功");
            }
        }
        return "抢购成功";
    }

}
