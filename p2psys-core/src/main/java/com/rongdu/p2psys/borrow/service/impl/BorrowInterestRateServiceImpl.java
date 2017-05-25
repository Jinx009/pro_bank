package com.rongdu.p2psys.borrow.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowInterestRateDao;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.model.BorrowInterestRateModel;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

@Service("borrowInterestRateService")
public class BorrowInterestRateServiceImpl implements BorrowInterestRateService {

	@Resource
	private BorrowInterestRateDao borrowInterestRateDao;

	@Override
	public void save(BorrowInterestRate bir) {
		borrowInterestRateDao.save(bir);
	}

	@Override
	public List<BorrowInterestRate> findByStatus(int status) {
		return borrowInterestRateDao.findByStatus(status);
	}

	@Override
	public void doBorrowInterestRateValid() {
		List<BorrowInterestRate> list = borrowInterestRateDao.findByStatusAndAddTime(1);
		if(list != null && list.size() > 0){
			for (BorrowInterestRate borrowInterestRate : list) {
				borrowInterestRate.setStatus(3);//已经过期
				borrowInterestRateDao.update(borrowInterestRate);
			}
		}
	}

	@Override
	public void update(BorrowInterestRate bir) {
		borrowInterestRateDao.update(bir);
	}

	@Override
	public List<BorrowInterestRate> findByStatusAndUser(int status, User user) {
		return borrowInterestRateDao.findByStatusAndUser(status, user);
	}

	@Override
	public PageDataList<BorrowInterestRateModel> borrowInterestRateList(int pageNumber,
			int pageSize, UserModel model) {
		QueryParam param = QueryParam.getInstance();
		param.addPage(pageNumber, pageSize);
		if(!StringUtil.isBlank(model.getSearchName())){
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{
			if(!StringUtil.isBlank(model.getUserName())){
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if(!StringUtil.isBlank(model.getRealName())){
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
		}
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<BorrowInterestRate> pageDataList = borrowInterestRateDao.findPageList(param);
		PageDataList<BorrowInterestRateModel> modelList = new PageDataList<BorrowInterestRateModel>();
		List<BorrowInterestRateModel> list = new ArrayList<BorrowInterestRateModel>();
		modelList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowInterestRate bir = pageDataList.getList().get(i);
				BorrowInterestRateModel birm = BorrowInterestRateModel.instance(bir);
				birm.setUserName(bir.getUser().getUserName());
				birm.setRealName(bir.getUser().getRealName());
				list.add(birm);
			}
		}
		modelList.setList(list);
		return modelList;
	}

	@Override
	public BorrowInterestRate findByUserId(long userId) {
		return borrowInterestRateDao.findByUserId(userId);
	}

	@Override
	public BorrowInterestRate find(long id) {
		return borrowInterestRateDao.find(id);
	}
}
