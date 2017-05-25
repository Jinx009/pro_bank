package com.rongdu.p2psys.nb.borrow.service.impl;

import java.text.ParseException;
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
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.InvestRecordModel;
import com.rongdu.p2psys.core.constant.ProtocolConstant;
import com.rongdu.p2psys.core.protocol.AbstractProtocolBean;
import com.rongdu.p2psys.core.protocol.ProtocolHelper;
import com.rongdu.p2psys.nb.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.nb.borrow.dao.BorrowDao;
import com.rongdu.p2psys.nb.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.nb.borrow.model.InvestDetailModel;
import com.rongdu.p2psys.nb.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolConfigDao;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolDao;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;

@Service("theBorrowTenderService")
public class BorrowTenderServiceImpl implements BorrowTenderService {

	@Resource
	private BorrowTenderDao theBorrowTenderDao;
	@Resource
	private BorrowDao theBorrowDao;
	@Resource
	private BorrowCollectionDao theBorrowCollectionDao;
	@Resource
	private ProtocolDao protocolDao;
	@Resource
	private ProtocolConfigDao protocolConfigDao;

	@Override
	public BorrowTender saveBorrowTender(BorrowTender borrowTender) {
		return theBorrowTenderDao.save(borrowTender);
	}

	@Override
	public void updateBorrowTender(BorrowTender borrowTender) {
		theBorrowTenderDao.update(borrowTender);
	}

	@Override
	public PageDataList<BorrowTenderModel> multiple(BorrowTenderModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if (null != model.getUser() && model.getUser().getBindId() > 0) {
				param.addParam("user.bindId", model.getUser().getBindId());
			} else if (null != model.getUser()
					&& model.getUser().getUserId() > 0) {
				param.addParam("user.userId", model.getUser().getUserId());
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ,
						model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getBorrowName())) {
				param.addParam("borrow.name", Operators.LIKE,
						model.getBorrowName());
			}
			Date d = DateUtil.getDayEndTime(System.currentTimeMillis() / 1000);
			Date start = null;
			if (model.getTime() == 7) {
				start = DateUtil.getDayStartTime(DateUtil.rollDay(d, -7)
						.getTime() / 1000);
				param.addParam("addTime", Operators.GTE, start);
				param.addParam("addTime", Operators.LTE, d);
			} else if (model.getTime() > 0 && model.getTime() < 4) {
				start = DateUtil.getDayStartTime(DateUtil.rollMon(d,
						-model.getTime()).getTime() / 1000);
				param.addParam("addTime", Operators.GTE, start);
				param.addParam("addTime", Operators.LTE, d);
			}
			if (model.getStatus() > -1) {
				param.addParam("status", model.getStatus());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			param.addPage(model.getPage(), model.getSize());
		}
		if(model.getFlag() == 0){//未到期
			SearchFilter ofFilter1 = new SearchFilter("borrow.status", Operators.EQ,6);
			SearchFilter ofFilter2 = new SearchFilter("borrow.status", Operators.EQ,7);
			param.addOrFilter(ofFilter1,ofFilter2);
		}
		param.addParam("borrow.type", Borrow.TYPE_ENTRUST);
		param.addOrder(OrderType.DESC, "id");

		PageDataList<BorrowTender> pageDataList = theBorrowTenderDao
				.findPageList(param);
		PageDataList<BorrowTenderModel> pageDateList = new PageDataList<BorrowTenderModel>();
		List<BorrowTenderModel> list = new ArrayList<BorrowTenderModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowTender t = (BorrowTender) pageDataList.getList().get(i);
				BorrowTenderModel tm = BorrowTenderModel.instance(t);
				Borrow borrow = t.getBorrow();
				tm.setBorrowName(borrow.getName());
				tm.setAccountYes(borrow.getAccountYes());
				tm.setUserName(t.getUser().getUserName());
				tm.setExpectedLow(borrow.getExpectedLow());
				tm.setExpectedUp(borrow.getExpectedUp());
				if (borrow.getUser() != null) {
					tm.setBorrowUserName(borrow.getUser().getUserName());
				} else {
					tm.setBorrowUserName("平台");
				}
				tm.setScales(borrow.getScales());
				tm.setBorrowId(borrow.getId());
				tm.setApr(borrow.getApr());  
				// 计算借款到期日
				if (t.getStatus() == 1) {
					if (borrow.getBorrowTimeType() == 1) {// 天标
						tm.setExpirationDate(DateUtil.dateStr2(DateUtil
								.rollDay(borrow.getReviewTime(),
										borrow.getTimeLimit())));
					} else {
						tm.setExpirationDate(DateUtil.dateStr2(DateUtil
								.rollMon(borrow.getReviewTime(),
										borrow.getTimeLimit())));
					}
				}
				tm.setUser(null);
				tm.setBorrow(null);
				if(DateUtil.compareDateS(tm.getExpirationDate())){
					tm.setFlag(1);
					tm.setExpectProfit(theBorrowCollectionDao.getFloatRate(t.getId(), model.getUser().getBindId()));
				}else{
					tm.setFlag(0);
				}
				
				list.add(tm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}
	
	@Override
	public String  builderTenderProcol(String type,long id)
	{
		String content = "";
		if(type.equals("borrow"))
		{
			BorrowTender tender = theBorrowTenderDao.find(id);
			ProtocolConfig pConfig = protocolConfigDao.findObjByProperty("protocolType",tender.getBorrow().getProtocolType());
			Protocol protocol = protocolDao.findByProperty("nid",pConfig.getNid()).get(0);
			AbstractProtocolBean protocolBean = ProtocolHelper.doProtocol(ProtocolConstant.BASE_FOR_TENDER);
			content = protocolBean.generateProtocol(tender.getBorrow().getId(), tender.getId(), protocol, tender.getUser().getUserId());
		}else{
			Borrow borrow = theBorrowDao.find(id);
			ProtocolConfig pConfig = protocolConfigDao.findObjByProperty("protocolType",borrow.getProtocolType());
			Protocol protocol = protocolDao.findByProperty("nid",pConfig.getNid()).get(0);
			content = protocol.getContent();
			if(protocol.getContent().contains("percent!"))
			{
				content = content.replaceAll("percent!", "%");
			}
			if(protocol.getContent().contains("<p >"))
			{
				content = content.replaceAll("<p >", "<p>");
			}
			if(protocol.getContent().contains("space!"))
			{
				content = content.replaceAll("space!", "&nbsp;");
			}
			content = content.replaceAll("<p>&nbsp;</p>", "");
			if(content.contains("line-height:"))
			{
				String fontSize = content.substring(content.indexOf("line-height:")+12, content.indexOf("px;"));
				content = content.replace(fontSize, "30");
			}
			content = content.replaceAll("(\\$)|(\\{.+?\\})", "&nbsp;&nbsp;");
		}
		return content;
	}

	@Override
	public PageDataList<InvestRecordModel> multipleIdentitiesList(
			InvestRecordModel model) throws ParseException {
		return theBorrowTenderDao.multipleIdentitiesList(model);
	}

	@Override
	public PageDataList<InvestDetailModel> getInvestRecordByItem(
			InvestDetailModel model) {
		return theBorrowTenderDao.getInvestRecordByItem(model);
	}

	@Override
	public void cancelBorrowTender(Long borrowId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.id", borrowId);
		param.addParam("status", BorrowTenderModel.STATUS_WAITING_FOR_PROCESS);

		List<BorrowTender> list = new ArrayList<BorrowTender>();
		List<BorrowTender> pojoList = theBorrowTenderDao.findByCriteria(param);
		for (BorrowTender pojo : pojoList) {
			pojo.setStatus(BorrowTenderModel.STATUS_FAILURE);
			list.add(pojo);
		}

		theBorrowTenderDao.update(list);
	}

	@Override
	public PageDataList<InvestRecordModel> lockCashRecord(
			InvestRecordModel model) {
		return theBorrowTenderDao.lockCashRecord(model);
	}
}
