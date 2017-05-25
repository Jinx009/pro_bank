package com.rongdu.p2psys.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ExchangeRatePacketCaptureUtil {

	/** 
     * 根据URL获得所有的html信息 
     * @param url 
     * @return 
     */  
    @SuppressWarnings({ "deprecation", "resource" })
	public static String getHtmlByUrl(String url){  
    	String html = "";
    	HttpClient httpClient = new DefaultHttpClient();  
		try {  
		    //创建HttpGet  
		    HttpGet httpGet = new HttpGet(url);  
		    //执行get请求  
		    HttpResponse response = httpClient.execute(httpGet);  
		    //获取响应实体  
		    HttpEntity entity = response.getEntity();  
		    //打印响应状态  
		    System.out.println(response.getStatusLine());  
		    if (entity != null) {  
		        //打印响应内容的长度  
		        System.out.println("Response content lenght:"  
		                + entity.getContentLength());  
		        String content = EntityUtils.toString(entity);  
		        //解决HttpClient获取中文乱码 ，用String对象进行转码  
		        //html = new String(content.getBytes("ISO-8859-1"),"gbk");
		        html = content;
		    }  
		} catch (Exception e) {  
		}finally{  
		    //关闭连接，释放资源  
		    httpClient.getConnectionManager().shutdown();  
		}
		return html;
    	
    }
    
    public static String getCashPurchasePrice() {
    	String cashPurchasePrice = "";
		Document doc = Jsoup.parse(getHtmlByUrl("http://srh.bankofchina.com/search/whpj/search.jsp?pjname=1316"));
		Elements trs = doc.select("table[align=left]").select("tbody").select("tr");
		//for(int i = 0;i < trs.size(); i++){
		    Elements tds = trs.get(1).select("td");
		    for(int j = 0;j < tds.size(); j++){
		        String text = tds.get(j).text();
		        if("美元".equals(text)){
		        	cashPurchasePrice = tds.get(5).text();
		        	break;
		        }
		    }
		//}
		return cashPurchasePrice;
    }
    public static void main(String[] args) {
		System.out.println(getCashPurchasePrice());
	}
}
