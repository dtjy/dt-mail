package com.dt.utils.job;

import com.dt.utils.service.MailSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//@Component
public class SanxiaScheduleTask {

    private final static Logger logger = LoggerFactory.getLogger(SanxiaScheduleTask.class);

    @Autowired
    MailSendService mailSendService;

    @Scheduled(cron = "30 0/2 * * * ?")
    public void scheduled() throws Exception{
        URL hsurl = new URL("http://hq.sinajs.cn/list=s_sh600116");
        URLConnection hsurlConnection = hsurl.openConnection();
        InputStream hsinputStream = hsurlConnection.getInputStream();
        InputStreamReader hsinputStreamReader = new InputStreamReader(hsinputStream,"GBK");
        BufferedReader hsbufferedReader = new BufferedReader(hsinputStreamReader);
        String hs = hsbufferedReader.readLine();


        String[] hs_arr = hs.split("\"");
        hs_arr = hs_arr[1].split(",");
        StringBuffer hs_sb = new StringBuffer();
        hs_sb.append(hs_arr[0]).append(":").append(hs_arr[1]);
        if (Double.parseDouble(hs_arr[2])>=0){
            hs_sb.append("，涨:").append(hs_arr[2]).append("，涨:").append(hs_arr[3]);
        }else {
            Double d1 = Math.abs(Double.parseDouble((hs_arr[2])));
            Double d2 = Math.abs(Double.parseDouble((hs_arr[3])));
            hs_sb.append("，跌:").append(d1.toString()).append("，跌:").append(d2.toString());
        }

//        hs_sb.append("。\n").append(cy_sb).append("。\n");
//        mailSendService.sendSimpleMail("luzhou3@asiainfo.com","现价",hs_sb.toString());
        mailSendService.sendSimpleMail("1792744489@qq.com",hs_arr[0]+"现价速报",hs_sb.toString());
    }
}
