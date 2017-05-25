package com.rongdu.p2psys.borrow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowBespeakDao;
import com.rongdu.p2psys.borrow.dao.BorrowBespeakPicDao;
import com.rongdu.p2psys.borrow.domain.BorrowBespeak;
import com.rongdu.p2psys.borrow.domain.BorrowBespeakPic;
import com.rongdu.p2psys.borrow.model.BorrowBespeakModel;
import com.rongdu.p2psys.borrow.service.BorrowBespeakService;
import com.rongdu.p2psys.core.Global;

@Service("borrowBespeakService")
public class BorrowBespeakServiceImpl implements BorrowBespeakService {

	@Resource
	private BorrowBespeakDao borrowBespeakDao;
	@Resource
	private BorrowBespeakPicDao borrowBespeakPicDao;
	
	@Override
	public BorrowBespeak doBespeak(BorrowBespeak borrowBespeak) {
		borrowBespeak.setStatus(0);
		borrowBespeak.setAddTime(new Date());
		borrowBespeak.setAddIp(Global.getIP());
		return borrowBespeakDao.save(borrowBespeak);
	}

	@Override
	public PageDataList<BorrowBespeakModel> borrowBespeakList(
			BorrowBespeakModel borrowBespeak, int page, int size, String searchName) {
		QueryParam param = QueryParam.getInstance();
		param.addPage(page, size);
		if(borrowBespeak != null){
			if(!StringUtil.isBlank(searchName)){
				SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, searchName);
	    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, searchName);
	    		param.addOrFilter(orFilter1,orFilter2);
			}else{
				if(borrowBespeak.getStatus() != 0){
					param.addParam("status", borrowBespeak.getStatus());
				}else {
					param.addParam("status", 0);
				}
				if(!StringUtil.isBlank(borrowBespeak.getUserName())){
					param.addParam("user.userName", Operators.EQ, borrowBespeak.getUserName());
				}
				if(!StringUtil.isBlank(borrowBespeak.getRealName())){
					param.addParam("user.realName", Operators.EQ, borrowBespeak.getRealName());
				}
			}
			param.addOrder(OrderType.DESC, "addTime");
		}
		PageDataList<BorrowBespeak> pageDataList = borrowBespeakDao.findPageList(param);
		PageDataList<BorrowBespeakModel> modelList = new PageDataList<BorrowBespeakModel>();
		List<BorrowBespeakModel> list = new ArrayList<BorrowBespeakModel>();
		modelList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowBespeak bb = pageDataList.getList().get(i);
				BorrowBespeakModel bbm = BorrowBespeakModel.instance(bb);
				bbm.setUserName(bb.getUser().getUserName());
				bbm.setRealName(bb.getUser().getRealName());
				list.add(bbm);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public BorrowBespeak find(long id) {
		return borrowBespeakDao.find(id);
	}

	@Override
	public void borrowBespeakEdit(BorrowBespeak borrowBespeak) {
		borrowBespeakDao.update(borrowBespeak);
	}

	@Override
	public void addBorrowBespeakPic(List<BorrowBespeakPic> list) {
		borrowBespeakPicDao.addBorrowBespeakPic(list);
	}

	@Override
	public List<BorrowBespeakPic> findBorrowBespeakPicById(long id) {
		return borrowBespeakPicDao.findByProperty("id", id);
	}

	@Override
	public List<BorrowBespeakPic> findBorrowBespeakPicByBorrowBespeak(BorrowBespeak borrowBespeak) {
		return borrowBespeakPicDao.findByProperty("borrowBespeak", borrowBespeak);
	}

	
}
