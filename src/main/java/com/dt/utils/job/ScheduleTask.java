package com.dt.utils.job;

import com.dt.utils.service.MailSendService;
import com.dt.utils.utils.HttpClientUtil;
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
import java.util.Date;

//@Component
public class ScheduleTask {

    private final static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    MailSendService mailSendService;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void scheduled() throws Exception{
        URL hsurl = new URL("http://hq.sinajs.cn/list=s_sh000001");
        URLConnection hsurlConnection = hsurl.openConnection();
        InputStream hsinputStream = hsurlConnection.getInputStream();
        InputStreamReader hsinputStreamReader = new InputStreamReader(hsinputStream,"GBK");
        BufferedReader hsbufferedReader = new BufferedReader(hsinputStreamReader);
        String hs = hsbufferedReader.readLine();
        //沪市
//        String hs = HttpClientUtil.sendGet("http://hq.sinajs.cn/list=s_sz399006");
        //创业
        URL cyurl = new URL("http://hq.sinajs.cn/list=s_sz399006");
        URLConnection cyurlConnection = cyurl.openConnection();
        InputStream cyinputStream = cyurlConnection.getInputStream();
        InputStreamReader cyinputStreamReader = new InputStreamReader(cyinputStream,"GBK");
        BufferedReader cybufferedReader = new BufferedReader(cyinputStreamReader);
        String cy = cybufferedReader.readLine();
//        String cy = HttpClientUtil.sendGet("http://hq.sinajs.cn/list=s_sz399006");

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

        String[] cy_arr = cy.split("\"");
        cy_arr = cy_arr[1].split(",");
        StringBuffer cy_sb = new StringBuffer();
        cy_sb.append(cy_arr[0]).append(":").append(cy_arr[1]);
        if (Double.parseDouble(cy_arr[2])>=0){
            cy_sb.append("，涨:").append(cy_arr[2]).append("，涨:").append(cy_arr[3]);
        }else {
            Double d1 = Math.abs(Double.parseDouble((cy_arr[2])));
            Double d2 = Math.abs(Double.parseDouble((cy_arr[3])));
            cy_sb.append("，跌:").append(d1.toString()).append("，跌:").append(d2.toString());
        }

        hs_sb.append("。\n").append(cy_sb).append("。\n");
        mailSendService.sendSimpleMail("279964527@qq.com","上证指数",hs_sb.toString());
    }
}
