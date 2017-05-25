package com.rongdu.p2psys.borrow.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.dao.BorrowConfigDao;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.model.BorrowConfigModel;
import com.rongdu.p2psys.borrow.service.BorrowConfigService;

@Service("borrowConfigService")
public class BorrowConfigServiceImpl implements BorrowConfigService {

	@Resource
	private BorrowConfigDao borrowConfigDao;

	@Override
	public List<BorrowConfig> findAll() {
		return borrowConfigDao.findAll();
	}

	@Override
	public PageDataList<BorrowConfigModel> list(BorrowConfigModel model) {
		PageDataList<BorrowConfig> plist =  borrowConfigDao.list(model);
		PageDataList<BorrowConfigModel> pageDataList = new PageDataList<BorrowConfigModel>();
		List<BorrowConfigModel> list = new ArrayList<BorrowConfigModel>();
		pageDataList.setPage(plist.getPage());
		if (plist.getList().size() > 0) {
			for (int i = 0; i < plist.getList().size(); i++) {
				BorrowConfig c = plist.getList().get(i);
				BorrowConfigModel cm = BorrowConfigModel.instance(c);
				list.add(cm);
			}
		}
		pageDataList.setList(list);
		
		return pageDataList;
	}

	@Override
	public BorrowConfig find(long id) {
		return borrowConfigDao.find(id);
	}

	@Override
	public void add(BorrowConfig borrowConfig) {
		borrowConfigDao.save(borrowConfig);
	}

	@Override
	public void update(BorrowConfig borrowConfig) {
		borrowConfigDao.update(borrowConfig);
	}

	@Override
	public List<BorrowConfig> findAllOutFlow() {
		return borrowConfigDao.findAllOutFlow();
	}

	@Override
	public List<BorrowConfig> findAllNotFlowAndSecond() {
		return borrowConfigDao.findAllNotFlowAndSecond();
	}

}
