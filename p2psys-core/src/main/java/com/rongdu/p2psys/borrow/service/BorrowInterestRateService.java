package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.model.BorrowInterestRateModel;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

public interface BorrowInterestRateService {

	public void save(BorrowInterestRate bir);

	public List<BorrowInterestRate> findByStatus(int status);

	public void doBorrowInterestRateValid();

	public void update(BorrowInterestRate bir);

	public List<BorrowInterestRate> findByStatusAndUser(int status, User user);

	public PageDataList<BorrowInterestRateModel> borrowInterestRateList(int pageNumber,
			int pageSize, UserModel model);

	public BorrowInterestRate findByUserId(long userId);

	public BorrowInterestRate find(long id);

}
