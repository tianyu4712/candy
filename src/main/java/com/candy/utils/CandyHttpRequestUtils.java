package com.candy.utils;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;




/**
 * @author zhongdawei
 *	新东方支付中心网络请求工具
 */
public class CandyHttpRequestUtils {

    private static final String DEFAULT_ENCODING="UTF-8";
    public static String encoding;

    private static final HttpConnectionManager connectionManager;

    private static final HttpClient client;

    static {

        HttpConnectionManagerParams params = loadHttpConfFromFile();

        connectionManager = new MultiThreadedHttpConnectionManager();

        connectionManager.setParams(params);


        client = new HttpClient(connectionManager);
    }

    private static HttpConnectionManagerParams loadHttpConfFromFile(){

        encoding = "UTF-8";

        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setConnectionTimeout(15000);
        params.setSoTimeout(30000);
        params.setStaleCheckingEnabled(true);
        params.setTcpNoDelay(true);
        params.setDefaultMaxConnectionsPerHost(100);
        params.setMaxTotalConnections(1000);
        params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        return params;
    }

    /**
     * post方式、指定编码、提交字符串拼接参数( a=1&b=2 )格式
     * @title post
     * @param url
     * @param encoding
     * @param content
     * @return
     */
    public static String post(String url, String encoding, String content) {
        try {
            byte[] resp = post(url, content.getBytes(encoding));
            if (null == resp)
                return null;
            return new String(resp, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * post方式、指定编码、提交字符串拼接参数( a=1&b=2 )格式
     * @title post
     * @param url
     * @param encoding
     * @param content
     * @return
     */
    public static String postIncludeSsl(String url, String encoding, String content) {
        if(url.startsWith("https")){
            return sendPostSsl(url,content);
        }
        try {
            byte[] resp = post(url, content.getBytes(encoding));
            if (null == resp)
                return null;
            return new String(resp, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostSsl(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            // 打开和URL之间的连接
            HttpsURLConnection conn = CandyCertUtil.getUrlConnection(url);
            conn.setHostnameVerifier(new CandyHttpRequestUtils().new TrustAnyHostnameVerifier());


            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post方式，默认编码(UTF-8)、提交拼接字符串参数( a=1&b=2 )格式
     * @title post
     * @param url
     * @param content
     * @return
     */
    public static String post(String url, String content) {
        return post(url, encoding, content);
    }

    /**
     * post方式,指定编码(默认为UTF-8)、提交map格式参数
     * @title post
     * @param url
     * @param encoding
     * @param params
     * @return
     */
    public static String post(String url, String encoding,Map<String,String>params){

        String content = map2Str(params);
        if(encoding==null || encoding.equals("")) encoding="UTF-8";
        return post(url, encoding, content);
    }

    /**
     * 使用get方式提交
     * @title get
     * @param url
     * @param encoding
     * @return
     */
    public static String get(String url,String encoding)  {

        if (encoding==null || encoding.equals("")) encoding=DEFAULT_ENCODING;

        GetMethod method = new GetMethod(url);
        method.addRequestHeader("Connection", "Keep-Alive");
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            method.getResponseBodyAsStream();

            InputStream inputStream = method.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String str= "";
            while((str = br.readLine()) != null){
                stringBuffer .append(str );
            }

            //  byte[] bs = method.getResponseBody();

            return stringBuffer.toString();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * get方式，提交map参数
     * @title get
     * @param url
     * @param encoding
     * @param map
     * @return
     */
    public static String get(String url,String encoding,Map<String,String>map)  {
        String content = map2Str(map);
        return get(url+"?"+content, encoding);
    }

    /**
     * get方式，提交map参数
     * @title getSsl
     * @param url
     * @param map
     * @return
     */
    public static String getSsl(String url,Map<String,String>map)  {
        String content = map2Str(map);
        try {
            return getHttpResposeStrData(url+"?"+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map 转换成 str
     * @title map2Str
     * @param params
     * @return
     */
    public static String map2Str(Map<String,String>params){
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    private static byte[] post(String url, byte[] content) {
        try {
            byte[] ret = post(url, new ByteArrayRequestEntity(content));
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] post(String url, RequestEntity requestEntity) throws Exception {

        PostMethod method = new PostMethod(url);
        method.addRequestHeader("Connection", "Keep-Alive");
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        method.setRequestEntity(requestEntity);
        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = method.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String str= "";
            while((str = br.readLine()) != null){
                stringBuffer .append(str );
            }
            return stringBuffer.toString().getBytes();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            method.releaseConnection();
        }
    }

    public static String readQueryString(HttpServletRequest request,int length){
        Map m = request.getParameterMap();
        if(m == null) {return "";}
        Map<String,String> map = new HashMap<String,String>();
        for(Object key : m.keySet()){
            String[] values = (String[]) m.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            map.put(key.toString(), valueStr);
        }
        String ss =  map2Str(map);

        if(length > 0 && ss.length() > length){
            ss = ss.substring(0,length);
        }

        return ss ;
    }

    public static String readQueryString(HttpServletRequest request){
        return readQueryString(request,0);
    }


    /**
     * 后台请求http地址
     * @title getHttpResposeStrData
     * @param url
     * @return
     * @throws IOException
     */
    public static String getHttpResposeStrData(String url) throws IOException{
        String str ="";
        if(StringUtils.isBlank(url) ){return str;}
        URL uri;
        BufferedReader br = null;
        try {
            uri = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) uri.openConnection();
            conn.setRequestProperty("Accept-Charset","utf-8");
            conn.setRequestMethod("GET");
            InputStream is =conn.getInputStream();
            char[] data = new char[1024];
            br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            str = String.valueOf(data, 0, br.read(data));
            br.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(br !=null ){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    /**
     * 后台请求https地址
     * @title getHttpsResposeStrData
     * @param url
     * @return
     * @throws IOException
     */
    public static String getHttpsResposeStrData(String url) throws IOException{
        String str ="";
        if(StringUtils.isBlank(url) ){return str;}

        BufferedReader br = null;
        try {

            HttpsURLConnection conn = CandyCertUtil.getUrlConnection(url);
            conn.setRequestProperty("Accept-Charset","utf-8");
            conn.setRequestMethod("GET");
            InputStream is =conn.getInputStream();
            char[] data = new char[1024];
            br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            str = String.valueOf(data, 0, br.read(data));
            br.close();
            is.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(br !=null ){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

}
