package com.rongdu.p2psys.nb.user.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.util.RandomUtil;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.CouponStatusConstant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.nb.user.dao.CouponDao;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.user.service.CouponCategoryService;
import com.rongdu.p2psys.nb.user.service.CouponLootLogService;
import com.rongdu.p2psys.nb.user.service.CouponService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.domain.CouponCategory;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.CouponModel;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

	@Resource
	private CouponDao couponDao;
	@Resource
	private UserDao theUserDao;
	@Resource
	private BorrowTenderService borrowTenderService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private CouponCategoryService couponCategoryService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private CouponLootLogService couponLootLogService;

	Logger logger = Logger.getLogger(CouponServiceImpl.class);

	NumberFormat nf = new DecimalFormat(",###.##");

	@Override
	public Coupon findCouponInfoById(Long couponId) {
		return couponDao.find(couponId);
	}

	@Override
	public Coupon findCouponInfoByBorrowTenderId(Long borrowTenderId) {
		return couponDao.getInfoByBorrowTenderId(borrowTenderId);
	}

	@Override
	public Coupon findCouponInfoByPpfundInId(Long ppfundInId) {
		return couponDao.getInfoByPpfundInId(ppfundInId);
	}

	@Override
	public Coupon updateCouponInfo(Coupon pojo) {
		return couponDao.update(pojo);
	}

	@Override
	public void updateOverdueInfo() {
		List<Coupon> pojoList = couponDao.getOverdueCouponsList();
		List<Coupon> updateList = new ArrayList<Coupon>();
		for (Coupon pojo : pojoList) {
			pojo.setStatus(CouponStatusConstant.STATUS_OVERDUE);
			updateList.add(pojo);
		}
		couponDao.update(updateList);
	}

	@Override
	public List<CouponModel> getUnusedCouponByUserId(Long userId) {
		return couponDao.getCouponModelByUserIdAndStatus(userId,
				CouponStatusConstant.STATUS_UNUSED);
	}

	@Override
	public List<CouponModel> getUsedCouponByUserId(Long userId) {
		return couponDao.getCouponModelByUserIdAndStatus(userId,
				CouponStatusConstant.STATUS_USED);
	}

	@Override
	public List<CouponModel> getOverdueCouponByUserId(Long userId) {
		return couponDao.getCouponModelByUserIdAndStatus(userId,
				CouponStatusConstant.STATUS_OVERDUE);
	}

	@Override
	public List<CouponModel> getDonateCouponByUserId(Long userId) {
		return couponDao.getCouponModelByUserIdAndStatus(userId,
				CouponStatusConstant.STATUS_DONATED);
	}

	@Override
	public Boolean useCoupon(Long userId, Long couponId, Long borrowTenderId,
			Long ppfundInId) {
		Boolean flag = false;
		Coupon pojo = findCouponInfoById(couponId);
		if (null != pojo && null != pojo.getUserTo()
				&& userId.equals(pojo.getUserTo().getUserId())
				&& pojo.getStatus().equals(CouponStatusConstant.STATUS_UNUSED)
				&& (borrowTenderId > 0 || ppfundInId > 0)) {
			if (borrowTenderId > 0) {
				pojo.setBorrowTenderId(borrowTenderId);
			} else {
				pojo.setPpfundInId(ppfundInId);
			}
			pojo.setStatus(CouponStatusConstant.STATUS_USED);
			pojo.setUpdateTime(new Date());
			couponDao.update(pojo);
			flag = true;
		}
		return flag;
	}

	@Override
	public Boolean donateCoupon(Long userId, Long couponId, String tel) {
		Boolean flag = false;
		Coupon pojo = findCouponInfoById(couponId);
		if (null != pojo.getUserTo()
				&& userId.equals(pojo.getUserTo().getUserId())
				&& !tel.equals(pojo.getUserTo().getUserName())
				&& CouponStatusConstant.STATUS_UNUSED.equals(pojo.getStatus())) {
			// 新增转增记录
			Coupon newPojo = newDonateCoupon(pojo);
			newPojo.setToMobile(tel);
			User user = theUserDao.getByUserName(tel);
			if (null != user) {
				newPojo.setUserTo(user);
			} else {
				newPojo.setUserTo(null);
			}
			couponDao.save(newPojo);

			// 更新原纪录
			pojo.setStatus(CouponStatusConstant.STATUS_DONATED);
			pojo.setUpdateTime(new Date());
			couponDao.update(pojo);

			flag = true;
		}
		return flag;
	}

	private Coupon newDonateCoupon(Coupon original) {
		Coupon newPojo = new Coupon();
		BeanUtils.copyProperties(original, newPojo);
		newPojo.setId(null);
		newPojo.setAddTime(new Date());
		newPojo.setUpdateTime(new Date());

		newPojo.setUserFrom(original.getUserTo());
		newPojo.setFromMobile(original.getToMobile());

		// 初始化调整值
		newPojo.setToRateAdjust(newPojo.getToRate());

		return newPojo;
	}

	@Override
	public void lootCoupon(Map<String, Object> map, Long categoryId,
			String lootTel) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		// 加息券配置信息
		CouponCategory couponCategory = couponCategoryService
				.findById(categoryId);
		if (null == couponCategory) {
			map.put(ConstantUtil.CODE, ConstantUtil.CODE_FAILURE);
			map.put(ConstantUtil.MESSAGE, "失败");
			return;
		}

		// 现在时间
		Date now = new Date();
		// 加息券起始时间
		Date startDate = couponCategory.getValidFrom();
		// 加息券截止时间
		Date endDate = couponCategory.getValidTo();
		try {
			now = df.parse(df.format(now));
			startDate = df.parse(df.format(startDate));
			endDate = df.parse(df.format(endDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/**
		 * 今天是否已经抢过
		 */
		Integer lootCount = couponLootLogService.countLootRecord(now, lootTel);
		if (lootCount > 0) {
			map.put(ConstantUtil.CODE, ConstantUtil.CODE_ALREADY_HAS);
			map.put(ConstantUtil.MESSAGE, "已经抢过");
			return;
		} else {
			couponLootLogService.addLootRecord(now, lootTel);
		}

		/**
		 * 是否已经有此类加息券
		 */
		if (couponDao.countLootedCouponByMobile(categoryId, lootTel) > 0) {
			map.put(ConstantUtil.CODE, ConstantUtil.CODE_NO_COUPON);
			map.put(ConstantUtil.MESSAGE, "已经抢到过");
			return;
		}

		/**
		 * 判断是否抢完
		 */
		Long total = couponCategory.getCirculation();
		if (total > 0) {// total为0表示不限制数量
			// 剩余券数
			Long remain = total;

			// 当前可用
			if ((endDate.getTime() - startDate.getTime() != 0)
					&& (endDate.getTime() - now.getTime() != 0)) {
				remain = total * (now.getTime() - startDate.getTime())
						/ (endDate.getTime() - startDate.getTime());
			}

			// 已抢占的份额数量
			List<Coupon> lootedList = couponDao
					.getLootedCouponsListByCategoryId(categoryId);
			if (lootedList != null && lootedList.size() > 0) {
				remain -= lootedList.size();
			}

			if (remain < 1) {
				map.put(ConstantUtil.CODE, ConstantUtil.CODE_NO_COUPON);
				map.put(ConstantUtil.MESSAGE, "已抢完");
				return;
			}
		}

		// TODO 临时先用随机数
		int temp = new Random().nextInt(10);

		if (temp > 6) {
			generateCoupon(CouponModel.OPERATION_LOOT, categoryId, lootTel);
			map.put(ConstantUtil.CODE, ConstantUtil.CODE_SUCCESS);
			map.put(ConstantUtil.MESSAGE, "成功");
		} else {
			map.put(ConstantUtil.CODE, ConstantUtil.CODE_LOOT_FAILURE);
			map.put(ConstantUtil.MESSAGE, "没抢到");
		}

	}

	@Override
	public Boolean generateCoupon(String operation, Long categoryId,
			String... mobileNumbers) {
		Boolean flag = true;

		List<Coupon> pojoList = new ArrayList<Coupon>();
		for (String mobileNumber : mobileNumbers) {
			try {
				Coupon newPojo = new Coupon();
				// code & pwd & category
				String tempCode = "";
				do {
					tempCode = RandomUtil.getRandomNumbersAndString(12);
				} while (couponDao.getInfoByCouponCode(tempCode) != null);
				newPojo.setCode(tempCode);
				newPojo.setPwd(RandomUtil.getRandomNumbersAndString(12));
				CouponCategory couponCategory = couponCategoryService
						.findById(categoryId);
				newPojo.setCategory(couponCategory);
				newPojo.setStatus(CouponStatusConstant.STATUS_UNUSED);
				// from info
				newPojo.setUserFrom(theUserDao.find(1L));
				newPojo.setFromMobile("1");
				newPojo.setFromRate(0.0);
				newPojo.setFromRateAdjust(0.0);
				// to info
				newPojo.setUserTo(theUserDao.getByUserName(mobileNumber));
				newPojo.setToMobile(mobileNumber);
				if (CouponModel.OPERATION_BONUS.equals(operation)) {
					newPojo.setToRate(couponCategory.getBonusRate());
					newPojo.setToRateAdjust(couponCategory.getBonusRate());
				} else {
					newPojo.setToRate(couponCategory.getRate());
					newPojo.setToRateAdjust(couponCategory.getRate());
				}
				// time
				newPojo.setAddTime(new Date());
				newPojo.setUpdateTime(new Date());

				pojoList.add(newPojo);

				// SMS
				sendSMS(operation, mobileNumber,
						nf.format(newPojo.getToRateAdjust()));
			} catch (Exception e) {
				logger.error(mobileNumber);
				logger.error(e.getMessage());
				flag = false;
			}
		}
		couponDao.save(pojoList);
		return flag;
	}

	@Override
	public PageDataList<CouponModel> findAllPageList(CouponModel model) {
		QueryParam param = QueryParam.getInstance()
				.addPage(model.getPage(), model.getSize())
				.addOrder(OrderType.DESC, "id");

		if (StringUtil.isNotBlank(model.getSearchName())) {
			SearchFilter fromMobile = new SearchFilter("fromMobile",
					model.getSearchName());
			SearchFilter toMobile = new SearchFilter("toMobile",
					model.getSearchName());
			param.addOrFilter(fromMobile, toMobile);
		}

		PageDataList<Coupon> pojoPageList = couponDao.findPageList(param);

		List<CouponModel> modellist = new ArrayList<CouponModel>();
		for (Coupon pojo : pojoPageList.getList()) {
			CouponModel tempModel = CouponModel.instance(pojo);
			modellist.add(tempModel);
		}

		PageDataList<CouponModel> pageList = new PageDataList<CouponModel>();
		pageList.setList(modellist);
		pageList.setPage(pojoPageList.getPage());
		return pageList;
	}

	@Override
	public List<Coupon> getCouponListByMobileNumber(Long categoryId,
			String mobile) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("category.id", categoryId);
		param.addParam("toMobile", mobile);

		return couponDao.findByCriteria(param);
	}

	/**
	 * 发送短信
	 */
	private void sendSMS(String templateName, String tel, String rate)
			throws Exception {
		Global.setTransfer("rate", rate);
		NoticeType smsType = Global.getNoticeType(templateName,
				NoticeConstant.NOTICE_SMS);
		if (smsType.getSend() == 1 && StringUtil.isNotBlank(tel)) {
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("rate", rate);
			Notice sms = new Notice();
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(tel);
			sms.setNid(NoticeConstant.CASH_SUPERVISION_NOTICE);
			sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(),
					sendData));
			noticeService.sendNotice(sms);
		}

	}

}
