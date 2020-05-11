package com.dt.utils.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Carter
 * @version 1.0
 * @ClassName: HttpClientUtil
 * @Description: TODO(HttpClient工具类)
 * @date 2018年10月8日 下午5:23:39
 */

public class HttpClientUtil {

    // 默认字符集
    private static final String ENCODING = "UTF-8";
    private final static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    //TODO
   // private  static   String baseUrl ="http://10.191.32.49:8001/smart/";
   // private  static   String baseUrl ="http://221.179.129.248:8081/smart/";
    /**
     * @Title: sendPost
     *
     * @throws@Description: TODO(发送post请求)
     *      * @param url 请求地址
     *      * @param headers 请求头
     *      * @param data 请求实体
     *      * @param encoding 字符集
     *      * @author Carter
     *      * @return String
     *      * @date 2018年10月10日 下午4:36:17
     */
    public static String sendPost(String url, Map<String, String> headers, JSONObject data, String encoding) {

        if (StringUtils.isEmpty(encoding)) {
            encoding = ENCODING;
        }
        log.info("进入post请求方法...");
        log.info("请求入参：URL= " + url);
        log.info("请求入参：headers=" + JSON.toJSONString(headers));
        log.info("请求入参：data=" + JSON.toJSONString(data));

        // 请求返回结果
        String resultJson = null;
        // 创建Client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost();

        try {
            // 设置请求地址
            httpPost.setURI(new URI(url));
            // 设置请求头
            if (headers != null) {
                Header[] allHeader = new BasicHeader[headers.size()];
                int i = 0;
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    allHeader[i] = new BasicHeader(entry.getKey(), entry.getValue());
                    i++;
                }
                httpPost.setHeaders(allHeader);
            }
            // 设置实体
            httpPost.setEntity(new StringEntity(JSON.toJSONString(data)));
            // 发送请求,返回响应对象
            CloseableHttpResponse response = client.execute(httpPost);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // 获取响应结果
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                log.error("响应失败，状态码：" + status);
            }

        } catch (Exception e) {
            log.error("发送post请求失败", e);
        } finally {
            httpPost.releaseConnection();
        }
        return resultJson;
    }

    /**
     * @Title: sendPost
     * @Description: TODO(发送post请求，请求数据默认使用json格式，默认使用UTF-8编码)
     * @param url 请求地址
     * @param data 请求实体
     * @author Carter
     * @return String
     * @date 2018年10月10日 下午4:37:28
     * @throws
     */
    public static String sendPost(String url, JSONObject data) {
        // 设置默认请求头

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");

        return sendPost(url, headers, data, ENCODING);
    }

    /**
     * @Title: sendPost
     * @Description: TODO(发送post请求，请求数据默认使用json格式，默认使用UTF-8编码)
     * @param url 请求地址
     * @param params 请求实体
     * @author Carter
     * @return String
     * @date 2018年10月10日 下午6:11:05
     * @throws
     */
    public static String sendPost(String url,Map<String,Object> params){
        // 设置默认请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        // 将map转成json

        JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
        return sendPost(url,headers,data,ENCODING);
    }

    /**
     * @Title: sendPost
     * @Description: TODO(发送post请求，请求数据默认使用UTF-8编码)
     * @param url 请求地址
     * @param headers 请求头
     * @param data 请求实体
     * @author Carter
     * @return String
     * @date 2018年10月10日 下午4:39:03
     * @throws
     */
    public static String sendPost(String url, Map<String, String> headers, JSONObject data) {

        return sendPost(url, headers, data, ENCODING);
    }

    /**
     * @Title: sendPost
     * @Description:(发送post请求，请求数据默认使用UTF-8编码)
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求实体
     * @author Carter
     * @return String
     * @date 2018年10月10日 下午5:58:40
     * @throws
     */
    public static String sendPost(String url,Map<String,String> headers,Map<String,String> params){

        // 将map转成json
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
        return sendPost(url, headers, data, ENCODING);
    }



    /**
     * @Title: sendGet
     * @Description: TODO(发送get请求)s
     * @param url 请求地址  相对请求路径
     * @param params 请求参数   类型Map<String,Object>
     * @param encoding 编码
     * @author Carter
     * @return String
     * @date 2018年10月14日 下午2:39:01
     * @throws
     */
    public static String sendGet(String url,Map<String,Object> params,String encoding){


        // 请求结果
        String resultJson = null;


        // 创建client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpGet
        HttpGet httpGet = new HttpGet();

        try {
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
            } else {
                // 执行 Http 请求.
                client = HttpClients.createDefault();
            }


            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            // 封装参数
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key).toString());
                }
            }
            URI uri = builder.build();

            log.info("请求入参：uri= " + uri + "请求入参：params=" + JSON.toJSONString(params));
            // 设置请求地址
            httpGet.setURI(uri);
            // 发送请求，返回响应对象
            CloseableHttpResponse response = client.execute(httpGet);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // 获取响应数据
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
                if (StringUtils.isEmpty(resultJson)){
                    log.error("{}返回数据为空",builder.getPath());
                }else {
                    JSONObject jsonObject = JSONObject.parseObject(resultJson);
                    Integer flag = jsonObject.getInteger("flag");
                    if (flag.equals(0)){
                        log.error("{}返回flag:0,error:{}",builder.getPath(),jsonObject.getString("error"));
                    }else if (flag.equals(1)){
//                        log.error("{}返回flag:1",builder.getPath());
                    }else {
                        log.error("{}返回flag异常",builder.getPath());
                    }
                }

            } else {
                log.error("响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送get请求失败", e);
        } finally {
            httpGet.releaseConnection();
        }
        return resultJson;
    }

    /**
     * 创建 SSL连接
     *
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl)
                        throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert)
                        throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns,
                                   String[] subjectAlts) throws SSLException {
                }

            });

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    /**
     * @param url    请求地址
     * @param params 请求参数
     * @author Carter
     * @return String
     * @date 2018年10月14日 下午2:32:39
     * @throws
     */
    public static String sendGet(String url, Map<String, Object> params) {
        return sendGet(url, params, ENCODING);
    }

    /**
     * @Title: sendGet
     * @Description: TODO(发送get请求)
     * @param url 请求地址
     * @author Carter
     * @return String
     * @date 2018年10月14日 下午2:33:45
     * @throws
     */
    public static String sendGet(String url){


        return sendGet(url,null,ENCODING);
    }
}
