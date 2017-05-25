package com.rongdu.p2psys.tpp.chinapnr.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 后台方式访问请求
 * 不再使用apache commons HttpClient项目，使用apache HttpClient和HttpCore项目
 * 
 * apache commons HttpClient官网的解释是：
 * <a href="http://hc.apache.org/httpclient-3.x/">
 * The Commons HttpClient project is now end of life, and is no longer being developed. 
 * It has been replaced by the Apache HttpComponents project in its HttpClient and HttpCore modules, 
 * which offer better performance and more flexibility.</a>
 * 
 * @author jason.yang
 * @version $Id: SSLServersUtils.java, v 0.1 2012-12-28 上午10:16:21 jason.yang Exp $
 */
public class HttpClientUtils {

    /**
     * 请求Https协议地址
     * 
     * <a href="http://blog.csdn.net/jadyer/article/details/7802139">使用HttpClient向HTTPS地址发送POST请求</a>
     * 
     * @param postUrl 地址
     * @param requestParams 请求参数
     * @param charset 字符编码集
     * @param timeout 超时时间
     * @return
     * @throws PayException
     */
    public static String doHttpsPost(String postUrl, Map<String, String> requestParams,
                                     String charset, int timeout) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            SSLContext ctx = SSLContext.getInstance(SSLSocketFactory.TLS);
            ctx.init(null, new TrustManager[] { new HttpsX509TrustManager() }, null);
            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(ctx);

            httpClient
                .getConnectionManager()
                .getSchemeRegistry()
                .register(
                    new Scheme(WebProtocolConstants.HTTPS_PROTOCOL_SCHEME, sslSocketFactory,
                        WebProtocolConstants.DEFAULT_PORT_NUMBER_FOR_HTTPS));

            HttpPost httpPost = new HttpPost(postUrl);
            List<NameValuePair> formParams = convert2NameValuePair(requestParams);
            if (!formParams.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(formParams, charset));
            }

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            String responseContent = "";
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, charset);
                entity.consumeContent();
            }

            return responseContent;
        } catch (NoSuchAlgorithmException noSuex) {
        } catch (KeyManagementException keyMaEx) {
        } catch (UnsupportedEncodingException unsuEx) {
        } catch (ClientProtocolException clPrEx) {
        } catch (IOException ioEx) {
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    private static List<NameValuePair> convert2NameValuePair(Map<String, String> requestParams) {
        if (requestParams == null || requestParams.isEmpty()) {
            return null;
        }

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formParams;
    }

    private static class HttpsX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                                                                throws CertificateException {
            //不校验服务器端证书，什么都不做，视为通过检查
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                                                                throws CertificateException {
            //不校验服务器端证书，什么都不做，视为通过检查
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
}
