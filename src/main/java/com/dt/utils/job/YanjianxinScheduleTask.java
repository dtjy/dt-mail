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
import java.time.LocalTime;

//@Component
public class YanjianxinScheduleTask {

    private final static Logger logger = LoggerFactory.getLogger(YanjianxinScheduleTask.class);

    @Autowired
    MailSendService mailSendService;

    @Scheduled(cron = "20 0/1 * * * ?")
    public void scheduled() throws Exception{
        String[] code = {"s_sz002562"};
        for (int i=0;i<code.length;i++){
            URL hsurl = new URL("http://hq.sinajs.cn/list="+code[i]);
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
            mailSendService.sendSimpleMail("2287852025@qq.com",hs_arr[0]+"-"+ LocalTime.now().toString(),hs_sb.toString());
            hsbufferedReader.close();
            hsinputStreamReader.close();
            hsinputStream.close();
        }

    }


}
