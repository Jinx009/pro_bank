package com.rongdu.p2psys.crowdfunding.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.crowdfunding.dao.MaterialsDao;
import com.rongdu.p2psys.crowdfunding.dao.OrderDao;
import com.rongdu.p2psys.crowdfunding.dao.ProjectBaseinfoDao;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.protocol.Html2PDF;
import com.rongdu.p2psys.crowdfunding.service.ProtocolService;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;


@SuppressWarnings("deprecation")
@Service("cfProtocolService")
public class ProtocolServiceImpl implements ProtocolService{

	@Resource
	private OrderDao cfOrderDao;
	@Resource
	private ProjectBaseinfoDao projectBaseinfoDao;
	@Resource
	private UserDao theUserdDao;
	@Resource
	private MaterialsDao materialsDao;
	
	/**
	 * 创建合同
	 * @throws Exception 
	 */
	@SuppressWarnings({"resource" })
	public String createProtocol(InvestOrder investOrder,String protocolCode) throws Exception {
		ProjectBaseinfo projectBaseinfo = investOrder.getProjectBaseinfo();
		User user = investOrder.getUser();
		String hql2 = "  FROM InvestOrder WHERE user.userId=  "+user.getUserId()+" AND projectBaseinfo.id = "+projectBaseinfo.getId()+
				" AND payStatus =  2";
		List<InvestOrder> list2 = cfOrderDao.getByHql(hql2.toString());
		Double money = 0.0;
		if(null!=list2&&!list2.isEmpty()){
			for(InvestOrder order:list2){
				money = BigDecimalUtil.add(money,order.getMoney());
			}
		}
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("projectName",projectBaseinfo.getProjectName());
		data.put("first_name",StringUtil.isNull(user.getRealName()));
		data.put("first_phone",user.getUserName());
		data.put("first_card",user.getCardId());
		data.put("money",money);
		data.put("part",0);
		if(40==projectBaseinfo.getId()){
			BigDecimal bg = new BigDecimal(money/10500000);
			BigDecimal bg2 = new BigDecimal(money/10000);
			DecimalFormat df=new DecimalFormat("0.#####");
			DecimalFormat df2=new DecimalFormat("0.##");
			String st=df.format(bg);
			String st2 = df2.format(bg2);
			data.put("money",st2);
			data.put("part",st);
		}
		data.put("companyName",projectBaseinfo.getCompany());
		String second_name = "",third_name="",second_card="",second_phone="",
				third_card="",third_phone="",fouth_name="",fouth_card="",fouth_phone="",html=""
				,second_account="";
		String hql = "  FROM Materials WHERE  projectBaseinfo.id="+projectBaseinfo.getId();
		List<Materials> list = materialsDao.getByHql(hql);
		if(null!=list&&!list.isEmpty()){
			for(Materials materials:list){
				if("second_name".equals(materials.getMaterialCode()))
					second_name = materials.getMaterialContent();
				else if("second_card".equals(materials.getMaterialCode()))
					second_card = materials.getMaterialContent();
				else if("second_phone".equals(materials.getMaterialCode()))
					second_phone = materials.getMaterialContent();
				else if("second_account".equals(materials.getMaterialCode()))
					second_account = materials.getMaterialContent();
				else if("third_name".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if("third_card".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if("third_phone".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if(protocolCode.equals(materials.getMaterialCode()))
				html = materials.getMaterialContent();
			}
		}
		data.put("second_name",second_name);
		data.put("second_card",second_card);
		data.put("second_phone",second_phone);
		data.put("third_name",third_name);
		data.put("third_card",third_card);
		data.put("third_phone",third_phone);
		data.put("fouth_name",fouth_name);
		data.put("fouth_card",fouth_card);
		data.put("fouth_phone",fouth_phone);
		if(html.contains("space!")){
			html = html.replaceAll("space!", "&nbsp;");
		}
		String path = Global.getString("protocol_host")+projectBaseinfo.getId()+"/";
		String fileName = user.getUserId()+".pdf";
		Html2PDF.createPdf(fileName,path, html, data);
		String httpUrl = Global.getString("protocol_host_post")+"?url="+path+fileName+"&second_name="+projectBaseinfo.getId()+"&second_account="+second_account;
		DefaultHttpClient httpClient = new DefaultHttpClient();  
        HttpPost post = new HttpPost(httpUrl);
        HttpResponse response = httpClient.execute(post); 
        EntityUtils.toString(response.getEntity(),"utf-8"); 
		return "success";
	}

	public String getProtocol(InvestOrder investOrder, String protocolCode) throws Exception {
		ProjectBaseinfo projectBaseinfo = investOrder.getProjectBaseinfo();
		User user = investOrder.getUser();
		String hql2 = "  FROM InvestOrder WHERE user.userId=  "+user.getUserId()+" AND projectBaseinfo.id = "+projectBaseinfo.getId()+
				" AND payStatus =  2";
		List<InvestOrder> list2 = cfOrderDao.getByHql(hql2.toString());
		Double money = 0.0;
		if(null!=list2&&!list2.isEmpty()){
			for(InvestOrder order:list2){
				money = BigDecimalUtil.add(money,order.getMoney());
			}
		}
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("projectName",projectBaseinfo.getProjectName());
		data.put("first_name",StringUtil.isNull(user.getRealName()));
		data.put("first_phone",user.getUserName());
		data.put("first_card",user.getCardId());
		data.put("money",money);
		data.put("part",0);
		if(40==projectBaseinfo.getId()){
			BigDecimal bg = new BigDecimal(money/10500000);
			BigDecimal bg2 = new BigDecimal(money/10000);
			DecimalFormat df=new DecimalFormat("0.#####");
			DecimalFormat df2=new DecimalFormat("0.##");
			String st=df.format(bg);
			String st2 = df2.format(bg2);
			data.put("money",st2);
			data.put("part",st);
		}
		data.put("companyName",projectBaseinfo.getCompany());
		String second_name = "",third_name="",second_card="",second_phone="",third_card="",third_phone="",fouth_name="",fouth_card="",fouth_phone="",html="";
		String hql = "  FROM Materials WHERE  projectBaseinfo.id="+projectBaseinfo.getId();
		List<Materials> list = materialsDao.getByHql(hql);
		if(null!=list&&!list.isEmpty()){
			for(Materials materials:list){
				if("second_name".equals(materials.getMaterialCode()))
					second_name = materials.getMaterialContent();
				else if("second_card".equals(materials.getMaterialCode()))
					second_card = materials.getMaterialContent();
				else if("second_phone".equals(materials.getMaterialCode()))
					second_phone = materials.getMaterialContent();
				else if("third_name".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if("third_card".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if("third_phone".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if(protocolCode.equals(materials.getMaterialCode()))
				html = materials.getMaterialContent();
			}
		}
		data.put("second_name",second_name);
		data.put("second_card",second_card);
		data.put("second_phone",second_phone);
		data.put("third_name",third_name);
		data.put("third_card",third_card);
		data.put("third_phone",third_phone);
		data.put("fouth_name",fouth_name);
		data.put("fouth_card",fouth_card);
		data.put("fouth_phone",fouth_phone);
		if(html.contains("space!")){
			html = html.replaceAll("space!", "&nbsp;");
		}
		String out = FreemarkerUtil.renderTemplate(html,data);
		return out;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws ParseException, IOException {
/*		String httpUrl = "http://www.800zf.cn:9080/pdf?url=/usr/local/tomcat/tsign/webapps/ROOT/doc/hehe.pdf&second_name=40&second_account=187CF6F26ED14A41AAA0C3FB3A5F54E5";
		DefaultHttpClient httpClient = new DefaultHttpClient();  
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");  
        HttpPost post = new HttpPost(httpUrl);
        HttpResponse response = httpClient.execute(post); 
        EntityUtils.toString(response.getEntity(),"utf-8"); */
		ProjectBaseinfo projectBaseinfo = new ProjectBaseinfo();
		projectBaseinfo.setId(40l);
		double money = 1000000000;
		if(40==projectBaseinfo.getId()){
			BigDecimal bg = new BigDecimal(money/1000000);
			DecimalFormat df=new DecimalFormat("0.#####");
			String st=df.format(bg);
			System.out.println(bg);
			System.out.println(st);
			double f1 = bg.setScale(5,BigDecimal.ROUND_DOWN).doubleValue();
			System.out.println(String.valueOf(f1));
		}
	}

	public String getEmptyProtocol(InvestOrder investOrder, String protocolCode)
			throws Exception {
		ProjectBaseinfo projectBaseinfo = investOrder.getProjectBaseinfo();
		User user = investOrder.getUser();
		String hql2 = "  FROM InvestOrder WHERE user.userId=  "+user.getUserId()+" AND projectBaseinfo.id = "+projectBaseinfo.getId()+
				" AND payStatus =  2";
		List<InvestOrder> list2 = cfOrderDao.getByHql(hql2.toString());
		Double money = 0.0;
		if(null!=list2&&!list2.isEmpty()){
			for(InvestOrder order:list2){
				money = BigDecimalUtil.add(money,order.getMoney());
			}
		}
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("projectName",projectBaseinfo.getProjectName());
		data.put("first_name",StringUtil.isNull(user.getRealName()));
		data.put("first_phone",user.getUserName());
		data.put("first_card",user.getCardId());
		data.put("money",money);
		data.put("part",0);
		if(40==projectBaseinfo.getId()){
			BigDecimal bg = new BigDecimal(money/10500000);
			BigDecimal bg2 = new BigDecimal(money/10000);
			DecimalFormat df=new DecimalFormat("0.#####");
			DecimalFormat df2=new DecimalFormat("0.##");
			String st=df.format(bg);
			String st2 = df2.format(bg2);
			data.put("money",st2);
			data.put("part",st);
		}
		data.put("companyName",projectBaseinfo.getCompany());
		String second_name = "",third_name="",second_card="",second_phone="",
				third_card="",third_phone="",fouth_name="",fouth_card="",fouth_phone="",html="";
		String hql = "  FROM Materials WHERE  projectBaseinfo.id="+projectBaseinfo.getId();
		List<Materials> list = materialsDao.getByHql(hql);
		if(null!=list&&!list.isEmpty()){
			for(Materials materials:list){
				if("second_name".equals(materials.getMaterialCode()))
					second_name = materials.getMaterialContent();
				else if("second_card".equals(materials.getMaterialCode()))
					second_card = materials.getMaterialContent();
				else if("second_phone".equals(materials.getMaterialCode()))
					second_phone = materials.getMaterialContent();
				else if("third_name".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if("third_card".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if("third_phone".equals(materials.getMaterialCode()))
					third_name = materials.getMaterialContent();
				else if(protocolCode.equals(materials.getMaterialCode()))
				html = materials.getMaterialContent();
			}
		}
		data.put("second_name",second_name);
		data.put("second_card",second_card);
		data.put("second_phone",second_phone);
		data.put("third_name",third_name);
		data.put("third_card",third_card);
		data.put("third_phone",third_phone);
		data.put("fouth_name",fouth_name);
		data.put("fouth_card",fouth_card);
		data.put("fouth_phone",fouth_phone);
		if(html.contains("space!")){
			html = html.replaceAll("space!", "&nbsp;");
		}
		String path = Global.getString("protocol_host")+projectBaseinfo.getId()+"/";
		String fileName = user.getUserId()+"empty.pdf";
		Html2PDF.createPdf(fileName,path, html, data);
		return "success";
	}
	
}
