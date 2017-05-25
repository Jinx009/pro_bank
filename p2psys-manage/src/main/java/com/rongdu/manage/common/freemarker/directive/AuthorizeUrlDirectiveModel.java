package com.rongdu.manage.common.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.service.MenuService;
import com.rongdu.p2psys.core.util.BeanUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * URL授权指令, 名称为secAuthorizeUrl, 使用如下所示: <br>
 * <pre><@secAuthorizeUrl("/index.htm")</pre>
 * <pre>...</pre>
 * <pre></@secAuthorizeUrl></pre>
 * 
 * 扩展自freemarker TemplateDirectiveModel类, 用于对判断当前登录用户是否被授权访问传入的URL, 如果<br>
 * 允许访问, 则将指令之间的内容输出, 反之不输出<br>
 * 
 * 注意: 该指令不能用于用户未登录的页面, 否则会抛出异常
 * @author zhangyz
 */
public class AuthorizeUrlDirectiveModel implements TemplateDirectiveModel {
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] temp,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		 // Check if no parameters were given:
        if (params.isEmpty()) {
            throw new TemplateModelException(
                    "This directive doesn't allow parameters.");
        }
        
        Operator op = (Operator) ServletActionContext.getRequest().getSession().getAttribute(Constant.SESSION_OPERATOR);
        // If there is non-empty nested content:
        if (body != null) {
        	boolean b = false;
        	String url =  params.get("value").toString();
            if (StringUtil.isNotBlank(url)) {
                // TODO; 改进此处, 增加对用户是否登录的判断
            	MenuService menuService = (MenuService)BeanUtil.getBean("menuService");
            	Boolean flat = menuService.getMenuPermission(op.getId(), url);
                if (flat) {
                    b = true;
                }
            } 
            if (b) {
                body.render(env.getOut());
            }
        } else {
            throw new RuntimeException("missing body");
        }
	}
}
