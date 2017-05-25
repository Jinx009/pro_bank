package com.rongdu.p2psys.ppfund.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.p2psys.ppfund.dao.PpfundOutDao;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.model.PpfundOutModel;
import com.rongdu.p2psys.ppfund.service.PpfundOutService;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月21日
 */
@Service("ppfundOutService")
public class PpfundOutServiceImpl implements PpfundOutService {
	@Resource
	private PpfundOutDao ppfundOutDao;
	
	@Override
	public PageDataList<PpfundOutModel> pageDataList(PpfundOutModel model) {
		QueryParam param = QueryParam.getInstance().addParam("user", model.getUser()).addPage(model.getPage(),
				model.getRows());
		PageDataList<PpfundOut> pageDataList = ppfundOutDao.findPageList(param);
		PageDataList<PpfundOutModel> pageDataList_ = new PageDataList<PpfundOutModel>();
		List<PpfundOutModel> list = new ArrayList<PpfundOutModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if(pageDataList.getList() != null && pageDataList.getList().size() > 0){
			for (PpfundOut ppfundOut : pageDataList.getList()) {
				PpfundOutModel outModel = PpfundOutModel.instance(ppfundOut);
				outModel.setPpfundName(ppfundOut.getPpfund().getName());
				outModel.setInterestTotal(ppfundOut.getPpfundIn().getInterest());
				outModel.setPpfundApr(ppfundOut.getPpfund().getApr());
				list.add(outModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public PageDataList<PpfundOutModel> pageDataList(long ppfundId, int page,
			int size) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("ppfund.id", ppfundId);
		param.addOrder(OrderType.DESC, "addTime");
		if (size != 0) {
	        param.addPage(page, size);
		} else {
		    param.addPage(page);
		}
		PageDataList<PpfundOut> pageDataList = ppfundOutDao.findPageList(param);
		PageDataList<PpfundOutModel> pageDataList_ = new PageDataList<PpfundOutModel>();
		List<PpfundOutModel> list = new ArrayList<PpfundOutModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if(pageDataList.getList().size() > 0){
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				PpfundOut out = pageDataList.getList().get(i);
				PpfundOutModel model = PpfundOutModel.instance(out);
				String username = out.getUser().getUserName();
				model.setUser(null);
				if(size == 0) {
					model.setUserName(username.charAt(0)+"******"+username.charAt(username.length()-1));
				} else {
					model.setUserName(username);
				}
				list.add(model);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	//
	@Override
	public double getPresentRedeem(User user) {
		return ppfundOutDao.dayOutMoney(user);
	}

}
