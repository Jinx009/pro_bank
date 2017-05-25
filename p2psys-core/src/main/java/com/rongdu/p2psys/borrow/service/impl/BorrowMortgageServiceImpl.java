package com.rongdu.p2psys.borrow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.p2psys.borrow.dao.BorrowMortgageDao;
import com.rongdu.p2psys.borrow.dao.BorrowUploadDao;
import com.rongdu.p2psys.borrow.domain.BorrowMortgage;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.service.BorrowMortgageService;
import com.rongdu.p2psys.core.Global;

@Service("borrowMortgageService")
public class BorrowMortgageServiceImpl implements BorrowMortgageService {

	@Resource
	private BorrowMortgageDao borrowMortgageDao;
	@Resource
	private BorrowUploadDao borrowUploadDao;
	
	@Override
    public List<BorrowMortgage> findByBorrowId(long borrowId, int status) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.id", borrowId);
		param.addParam("status", status);
		List<BorrowMortgage> list = borrowMortgageDao.findByCriteria(param);
		return list;
	}
	@Override
    public List<BorrowMortgage> findByBorrowIdAndNum(long borrowId, int num) {
	    QueryParam param = QueryParam.getInstance();
        param.addParam("borrow.id", borrowId);
        param.addParam("num", num);
        List<BorrowMortgage> list = borrowMortgageDao.findByCriteria(param);
        
        return list;
    }
	@Override
    public List<BorrowMortgage> findByBorrowIdAndNumAndStatus(long borrowId, int num, int status) {
	    QueryParam param = QueryParam.getInstance();
        param.addParam("borrow.id", borrowId);
        param.addParam("num", num);
        param.addParam("status", status);
        List<BorrowMortgage> list = borrowMortgageDao.findByCriteria(param);
        return list;
    }
	@Override
	public List<BorrowMortgage> findInByBorrowIdAndNum(long borrowId, int num) {
	    QueryParam param = QueryParam.getInstance();
	    param.addParam("borrow.id", borrowId);
	    param.addParam("num", num);
	    SearchFilter orFilter1 = new SearchFilter("status", 1);
        SearchFilter orFilter2 = new SearchFilter("status", 3);
        param.addOrFilter(orFilter1, orFilter2);
	    List<BorrowMortgage> list = borrowMortgageDao.findByCriteria(param);
	    return list;
	}
    @Override
    public double getTotalMortgagePriceByMortgageIds(long[] ids) {
        return borrowMortgageDao.getTotalMortgagePriceByMortgageIds(ids);
    }

    @Override
    public double getTotalAssessPriceByBorrowId(long id) {
        return borrowMortgageDao.getTotalAssessPriceByBorrowId(id);
    }

    @Override
    public double getTotalAssessPriceByBorrowIdAndNum(long id, int num) {
        return borrowMortgageDao.getTotalAssessPriceByBorrowIdAndNum(id, num);
    }
	@Override
	public void updateBorrowCollateral(BorrowMortgage borrowMortgage, List<BorrowUpload> pathList) {
	    borrowMortgage.setAddIp(Global.getIP());
	    borrowMortgage.setAddTime(new Date());
	    borrowMortgageDao.save(borrowMortgage);
	    List<BorrowUpload> list = new ArrayList<BorrowUpload>();
		for (BorrowUpload bu : pathList) {
		    bu.setBorrowMortgage(borrowMortgage);
		    bu.setBorrow(borrowMortgage.getBorrow());
		    list.add(bu);
		}
		borrowUploadDao.save(list);
	}

    @Override
    public BorrowMortgage findById(long id) {
        return borrowMortgageDao.find(id);
    }

    @Override
    public void updateBorrowMortgage(List<BorrowMortgage> borrowMortgageList, List<BorrowMortgage> bms, List<BorrowUpload> list) {
        borrowMortgageDao.save(borrowMortgageList);
        for (BorrowMortgage bm : bms) {
            bm.setStatus(3);
            borrowMortgageDao.update(bm);
        }
        borrowUploadDao.save(list);
    }
    @Override
    public void addMortgage(List<BorrowMortgage> borrowMortgageList, List<BorrowUpload> list) {
        borrowMortgageDao.save(borrowMortgageList);
        borrowUploadDao.save(list);
    }

    @Override
    public int getMaxNumByBorrowId(long borrowId) {
        
        return borrowMortgageDao.getMaxNumByBorrowId(borrowId);
    }
    @Override
    public double getTotalMortgagePriceByBorrowId(long borrowId) {
        
        return borrowMortgageDao.getTotalMortgagePriceByBorrowId(borrowId);
    }
    @Override
    public Object[] getTotalPriceByBorrowIdAndNum(long id, int num) {
        return borrowMortgageDao.getTotalPriceByBorrowIdAndNum(id, num);
    }
}
