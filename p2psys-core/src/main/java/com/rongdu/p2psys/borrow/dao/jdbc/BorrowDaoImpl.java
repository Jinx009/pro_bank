package com.rongdu.p2psys.borrow.dao.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowUploadDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.exception.UserException;

@Repository("borrowDao")
public class BorrowDaoImpl extends BaseDaoImpl<Borrow> implements BorrowDao
{
	@Resource
	private BorrowUploadDao borrowUploadDao;

	@Override
	public void modifyBorrowAndRepay(Borrow borrow)
	{
		StringBuffer sql = new StringBuffer(
				"UPDATE rd_borrow SET repayment_account = CAST(repayment_account AS DECIMAL(20,2))+ :repayment_account,");
		sql.append(" repayment_yes_account=CAST(repayment_yes_account AS DECIMAL(20,2))+ :repayment_yes_account ,");
		sql.append(" repayment_yes_interest=CAST(repayment_yes_interest AS DECIMAL(20,2))+ :repayment_yes_interest,");
		sql.append(" status = :status WHERE id = :id ");
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("repayment_account", borrow.getRepaymentAccount());
		query.setParameter("repayment_yes_account",
				borrow.getRepaymentYesAccount());
		query.setParameter("repayment_yes_interest",
				borrow.getRepaymentYesInterest());
		query.setParameter("status", borrow.getStatus());
		query.setParameter("id", borrow.getId());
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new UserException("撤回标失败！", 1);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BorrowModel> getIndexList(BorrowModel model)
	{
		List<Borrow> list;
		if (model.getType() != Borrow.TYPE_FLOW && model.getType() != 0)
		{ // 非流转标的借款需要判断是否超过有效时间
			List params = new ArrayList();
			params.add(model.getType());
			params.add(" and borrow.type=?) order by add_time desc");
			list = getInviteList(model, params).getList();
		}
		else
		{ // 流转标的借款不需要判断是否超过有效时间
			QueryParam param = QueryParam.getInstance();
			if (model.getType() != 0 && model.getType() != 100)
			{
				param.addParam("type", model.getType());
			}
			/*
			 * if (model.getType() == Constant.TYPE_FLOW) {// 招标的借款
			 * param.addParam("status", 1).addParam("account",
			 * Operators.PROPERTY_NOTEQ, "accountYes"); } else { SearchFilter
			 * orFilter1 = new SearchFilter("status", Operators.EQ, 3);
			 * SearchFilter orFilter2 = new SearchFilter("status", Operators.EQ,
			 * 6); SearchFilter orFilter3 = new SearchFilter("status",
			 * Operators.EQ, 7); SearchFilter orFilter4 = new
			 * SearchFilter("status", Operators.EQ, 8);
			 * param.addOrFilter(orFilter1, orFilter2, orFilter3, orFilter4); }
			 */
			param.addParam("status", 1);
			param.addParam("scales", Operators.NOTEQ, 100);
			param.addOrder(OrderType.DESC, "addTime");
			list = super.findByCriteria(param, 0, model.getSize());
		}
		List<BorrowModel> modelList = new ArrayList<BorrowModel>();
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");
		for (int i = 0; i < list.size(); i++)
		{
			Borrow borrow = list.get(i);
			BorrowModel model_ = BorrowModel.instance(borrow);
			VerifyLog log = verifyLogDao
					.findByType(borrow.getId(), "borrow", 1);
			if (log != null)
			{
				model_.setStartTime(DateUtil.dateStr2(log.getTime()));
			}
			modelList.add(model_);
		}
		return modelList;
	}

	public int unfinshJinBorrowCount(long userId)
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		param.addOrFilter("status", 0, 1);
		param.addParam("type", Borrow.TYPE_PROPERTY);
		return super.countByCriteria(param);
	}

	public double getRepayTotalWithJin(long userId)
	{
		String jpql = "SELECT SUM(repaymentAccount) FROM BorrowRepayment WHERE status = 0 AND user.userId = :userId AND borrow.type = "
				+ Borrow.TYPE_PROPERTY;
		Query query = em.createQuery(jpql);
		query.setParameter("userId", userId);
		Object obj = query.getSingleResult();
		if (obj == null)
		{
			obj = 0;
		}
		return Double.parseDouble(obj.toString());
	}

	public void update(double account, double scales, int status, long id)
	{
		String jpql = "UPDATE Borrow SET tenderTimes = tenderTimes + 1, accountYes = round(accountYes + :account, 2), scales = :scales WHERE id = :id AND status = :status AND ROUND(accountYes + :account) <= account";
		Query query = em.createQuery(jpql);
		query.setParameter("account", account);
		query.setParameter("scales", scales);
		query.setParameter("status", status);
		query.setParameter("id", id);
		int count = query.executeUpdate();
		if (count < 1)
		{
			throw new BorrowException("投标失败！此标可能已满！", 1);
		}
		super.refresh(find(id));
	}

	@Override
	public void updateFlowTotalYesCount(Borrow borrow)
	{
		// StringBuffer jpql = new
		// StringBuffer("UPDATE Borrow SET flowTotalYesCount = :flowTotalYesCount where id = :id AND type = ");
		// jpql.append(Constant.TYPE_FLOW);
		// if(borrow.getFlowTime()>0){
		// jpql.append(" and :flowTotalYesCount <= (flowTime*flowCount)");
		// }
		// Query query = em.createQuery(jpql.toString());
		// query.setParameter("flowTotalYesCount",
		// borrow.getFlowTotalYesCount());
		// query.setParameter("id", borrow.getId());
		// int count = query.executeUpdate();
		// if (count != 1) {
		// throw new BorrowException("投标失败！超过流转标可流转次数，还剩"
		// + ((borrow.getFlowTime() * borrow.getFlowCount()) -
		// borrow.getFlowTotalYesCount()) + "份可以投标！", 1);
		// }
	}

	@Override
	public void updateStatus(long id, int status, int preStatus)
	{
		String sql = "UPDATE Borrow SET status = :status WHERE id = :id AND status = :preStatus";
		Query query = em.createQuery(sql);
		query.setParameter("status", status);
		query.setParameter("id", id);
		query.setParameter("preStatus", preStatus);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new BussinessException("标审核，更改标状态失败！", 1);
		}
		refresh(super.find(id));
	}

	@Override
	public void updateStatus(long id, int status)
	{
		String sql = "UPDATE Borrow SET status = :status WHERE id = :id";
		Query query = em.createQuery(sql);
		query.setParameter("status", status);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new BussinessException("更改标状态失败！", 1);
		}
		refresh(super.find(id));
	}

	@Override
	public void updatefixedTime(long id, BorrowModel model)
	{
		String sql = "UPDATE Borrow SET fixedTime = :fixedTime, expirationTime = :expirationTime, status = 1 WHERE id = :id";
		Query query = em.createQuery(sql);
		query.setParameter("fixedTime", model.getFixedTime());
		query.setParameter("expirationTime", model.getExpirationTime());
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new BussinessException("更新失败！", 1);
		}
		refresh(super.find(id));
	}

	@Override
	public void updateRegisterTime(long id, Date time)
	{
		String sql = "UPDATE Borrow SET registerTime = :registerTime WHERE id = :id";
		Query query = em.createQuery(sql);
		query.setParameter("registerTime", time);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new BussinessException("更改标登记失败！", 1);
		}
		refresh(super.find(id));
	}

	@Override
	public Borrow getBorrowByName(String name)
	{
		List<Borrow> list = super.findByProperty("name", name);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public int trialCount(int status)
	{
		QueryParam param = QueryParam.getInstance().addParam("status", status);
		return countByCriteria(param);
	}

	@Override
	public int fullCount(int status)
	{

		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 1);
		param.addParam("account", Operators.PROPERTY_EQ, "accountYes");
		param.addParam("type", Operators.NOTEQ, Borrow.TYPE_FLOW);

		return super.countByCriteria(param);
	}

	@Override
	public Object[] countByFinish()
	{
		String jpql = "select count(id),sum(account),avg(apr),sum(repaymentYesInterest) from Borrow where status in (3,6,7,8)";
		Query query = em.createQuery(jpql);
		Object[] object = (Object[]) query.getSingleResult();
		return object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowModel> spreadBorrowList(BorrowModel model)
	{
		StringBuffer buffer = new StringBuffer(
				"select borrow.* from rd_borrow borrow,rd_user user,rd_verify_log log where borrow.user_id=user.user_id and borrow.id=log.fid and log.type='borrow' and "
						+ "date_add(log.time, INTERVAL borrow.valid_time day) < now() and borrow.status = 1 and borrow.account>borrow.account_yes and borrow.type !=110");
		getString(buffer, model);
		buffer.append(" order by borrow.id  desc");
		if (model.getPage() == 1)
		{
			buffer.append(" limit " + model.getSize());
		}
		else
		{
			buffer.append(" limit "
					+ (model.getSize() * model.getPage() - model.getSize())
					+ "," + model.getSize());
		}
		Query query = em.createNativeQuery(buffer.toString(), Borrow.class);
		List<Borrow> list = (List<Borrow>) query.getResultList();
		List<BorrowModel> modelList = new ArrayList<BorrowModel>();
		for (int i = 0; i < list.size(); i++)
		{
			BorrowModel bm = BorrowModel.instance(list.get(i));
			bm.setUserName(list.get(i).getUser().getUserName());
			bm.setRealName(list.get(i).getUser().getRealName());
			modelList.add(bm);
		}
		return modelList;
	}

	private StringBuffer getString(StringBuffer buffer, BorrowModel model)
	{
		if (StringUtil.isNotBlank(model.getSearchName()))
		{
			buffer.append(" and (borrow.name like '%" + model.getSearchName()
					+ "%' or borrow.company_name like '%"
					+ model.getSearchName() + "%')");
		}
		else
		{
			if (StringUtil.isNotBlank(model.getCompanyName()))
			{
				buffer.append(" and borrow.company_name = '"
						+ model.getCompanyName() + "'");
			}
			if (StringUtil.isNotBlank(model.getStartTime()))
			{
				String start = model.getStartTime();
				buffer.append(" and borrow.add_time >='" + start + "'");
			}
			if (StringUtil.isNotBlank(model.getEndTime()))
			{
				String end = model.getEndTime();
				buffer.append(" and borrow.add_time <= '" + end + "'");
			}
			if (model.getType() != 100 && model.getType() != 0)
			{
				buffer.append(" and borrow.type=" + model.getType());
			}
		}
		return buffer;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PageDataList<Borrow> getInviteList(BorrowModel model, List params)
	{
		PageDataList<Borrow> pageDataList = new PageDataList<Borrow>();
		StringBuffer sb = new StringBuffer(
				"select borrow.* from rd_borrow borrow,rd_verify_log log "
						+ "where (borrow.id=log.fid and log.type='borrow' "
						+ "and date_add(log.time, INTERVAL borrow.valid_time day) > now() "
						+ "and borrow.status = 1 and borrow.scales!=100");
		sb.append(params.get(params.size() - 1));
		Query query = em.createNativeQuery(sb.toString(), Borrow.class);
		for (int i = 0; i < params.size() - 1; i++)
		{
			query.setParameter(i + 1, params.get(i));
		}
		Page page = new Page(query.getResultList().size(), model.getPage(),
				model.getSize());
		query.setFirstResult((model.getPage() - 1) * model.getSize());
		query.setMaxResults(model.getSize());
		List<Borrow> list = query.getResultList();
		pageDataList.setList(list);
		pageDataList.setPage(page);
		return pageDataList;
	}

	@Override
	public Borrow findNotFlow(long borrowId)
	{
		String sql = "select borrow.* from rd_borrow borrow,rd_verify_log log "
				+ "where (borrow.id=log.fid and log.type='borrow' and log.verify_type=1 "
				+ "and ((date_add(log.time, INTERVAL borrow.valid_time day) > now() "
				+ "and borrow.account != borrow.account_yes) or (borrow.account = borrow.account_yes)) "
				+ "or borrow.type =110) and borrow.id=?";
		Query query = em.createNativeQuery(sql, Borrow.class);
		query.setParameter(1, borrowId);
		return (Borrow) query.getSingleResult();
	}

	@Override
	public void modifyBidNo(long borrowId, String bidNo)
	{
		String sql = "UPDATE Borrow SET bid_no = :bidNo WHERE id = :borrowId ";
		Query query = em.createQuery(sql);
		query.setParameter("borrowId", borrowId);
		query.setParameter("bidNo", bidNo);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new BussinessException("更新标号失败", 1);
		}
		refresh(super.find(borrowId));
	}

	@Override
	public void modifyGuaranteeNo(long borrowId, String guaranteeNo)
	{
		String sql = "UPDATE Borrow SET guarantee_no = :guaranteeNo WHERE id = :borrowId ";
		Query query = em.createQuery(sql);
		query.setParameter("borrowId", borrowId);
		query.setParameter("guaranteeNo", guaranteeNo);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new BussinessException("更新担保号失败", 1);
		}
		refresh(super.find(borrowId));
	}

	@Override
	public int findByStatusAndUserId(long userId, int status1, int status2)
	{
		if (status2 != 0)
		{
			StringBuffer sql = new StringBuffer(
					"SELECT COUNT(DISTINCT p1.id) FROM rd_borrow p1 WHERE p1.user_id = :userId AND ( p1.status = :statusA OR p1.status = :statusB )");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("userId", userId);
			query.setParameter("statusA", status1);
			query.setParameter("statusB", status2);
			Object count = query.getSingleResult();
			if (count != null)
			{
				return Integer.parseInt(count.toString());
			}
		}
		else if (status1 == 1)
		{
			StringBuffer sql = new StringBuffer(
					"SELECT COUNT(DISTINCT p1.id) as num FROM rd_borrow p1,rd_user p2,rd_verify_log p3 WHERE");
			sql.append(" p1.user_id = p2.user_id AND p1.user_id = :userId AND( p1.id=p3.fid AND p3.type='borrow'");
			sql.append(" AND p3.result = 1 AND p3.verify_type = 1 AND DATE_ADD(p3.time, INTERVAL p1.valid_time DAY) >= NOW()");
			sql.append(" AND p1.status = :statusA AND p1.account_yes <= account OR p1.type =110 AND p1.status = :statusB )");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("userId", userId);
			query.setParameter("statusA", status1);
			query.setParameter("statusB", status1);
			Object count = query.getSingleResult();
			if (count != null)
			{
				return Integer.parseInt(count.toString());
			}
		}
		return 0;
	}

	@Override
	public double findAccountTotalByStatus(long userId, int status1, int status2)
	{
		if (status2 != 0)
		{
			StringBuffer sql = new StringBuffer(
					"SELECT SUM(p1.repayment_account - p1.repayment_yes_account) FROM rd_borrow p1 WHERE p1.user_id = :userId AND ( p1.status = :statusA OR p1.status = :statusB )");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("userId", userId);
			query.setParameter("statusA", status1);
			query.setParameter("statusB", status2);
			Object count = query.getSingleResult();
			if (count != null)
			{
				return Double.parseDouble(count.toString());
			}

		}
		else if (status1 == 1)
		{// 正在借款金额
			StringBuffer sql = new StringBuffer(
					"SELECT SUM(p1.account) FROM rd_borrow p1,rd_user p2,rd_verify_log p3 WHERE");
			sql.append(" p1.user_id = p2.user_id AND p1.user_id = :userId AND ( p1.id=p3.fid AND p3.type='borrow'");
			sql.append(" AND p3.result = 1 AND p3.verify_type = 1 AND DATE_ADD(p3.time, INTERVAL p1.valid_time DAY) >= NOW()");
			sql.append(" AND p1.status = :status AND p1.account_yes <= account OR p1.type =110 AND p1.status = :status)");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("userId", userId);
			query.setParameter("status", status1);
			Object count = query.getSingleResult();
			if (count != null)
			{
				return Double.parseDouble(count.toString());
			}
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<Borrow> getMemberInvestList(User user)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT p1.* FROM rd_borrow p1,rd_user p2,rd_verify_log p3 WHERE");
		sql.append(" p1.user_id = p2.user_id AND p1.user_id <> :userId AND");
		sql.append(" ( p1.id=p3.fid AND p3.type='borrow'  AND p3.verify_type = 1 AND p3.result = 1 AND DATE_ADD(p3.time, INTERVAL p1.valid_time DAY) >= NOW()");
		sql.append(" AND p1.status = 1  AND p1.account_yes <= p1.account OR p1.type =110 AND p1.status = 1 AND p1.scales!=100");
		sql.append(" OR p1.status IN (3,6,7,8) ) GROUP BY p1.id ORDER BY p1.status ASC , p1.id DESC LIMIT 4");
		Query query = em.createNativeQuery(sql.toString(), Borrow.class);
		query.setParameter("userId", user.getUserId());
		List<Borrow> list = (List<Borrow>) query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageDataList<Borrow> getList(BorrowModel model)
	{
		QueryParam param = QueryParam.getInstance();
		StringBuffer selectSql;
		if (model.getType() == Borrow.TYPE_ENTRUST)
		{
			selectSql = new StringBuffer(
					"SELECT p1.* FROM rd_borrow p1,rd_verify_log p3 WHERE 1 = 1 ");
		}
		else
		{
			selectSql = new StringBuffer(
					"SELECT p1.* FROM rd_borrow p1,rd_user p2,rd_verify_log p3 WHERE 1 = 1 and p1.user_id = p2.user_id ");
		}
		StringBuffer sql = new StringBuffer();
		if (model != null)
		{
			long userId = 0;
			if (model.getUser() != null && model.getUser().getUserId() > 0)
			{
				sql.append(" AND p1.user_id = :userId");
				userId = model.getUser().getUserId();
			}
			int status = -2;
			if (model.getStatus() != 99)
			{
				// switch (model.getStatus()) {
				switch (-2)
				{
				case -2:// -2代表投资列表筛选条件是：状态（全部），在搜索全部的状态时候，尤其是状态等于1的时候，需要特殊处理，将过期的标剔除
					sql.append(" AND (p1.id=p3.fid AND p3.type='borrow' AND p3.result = 1 AND p3.verify_type = 1 AND DATE_ADD(p3.time, INTERVAL p1.valid_time DAY) >= NOW()");
					sql.append(" AND p1.status = 1  AND p1.account_yes <= p1.account");
					sql.append(" OR p1.type =110 AND p1.status = 1 AND p1.scales!=100 OR p1.status IN (3,6,7,8))");
					break;
				case 1:
					String scalesSql = "";
					if (model.getScales() == 100)
					{
						scalesSql = " AND p1.account_yes = account AND p1.scales = 100";
					}
					else
					{
						scalesSql = " AND p1.account_yes < account";
					}
					/*
					 * sql.append(
					 * " AND (p1.id=p3.fid AND p3.type='borrow' AND p3.result = 1 AND p3.verify_type = 1 AND DATE_ADD(p3.time, INTERVAL p1.valid_time DAY) >= NOW()"
					 * ); sql.append(" AND (p1.status = 1" + scalesSql);
					 */
					sql.append(" AND (p1.id=p3.fid AND p3.type='borrow' AND p3.result = 1 AND p3.verify_type = 1 AND DATE_ADD(p3.time, INTERVAL p1.valid_time DAY) >= NOW()");
					sql.append(" AND p1.status = 1" + scalesSql);
					sql.append(" OR p1.type =110 AND p1.status = 1 "
							+ scalesSql + ")");
					break;
				case 6:
					sql.append(" AND p1.status in (3,6,7)");
					break;
				case 11:
					sql.append(" AND p1.status in (0,11)");
					break;
				case 19:
					sql.append(" AND p1.status = 1 AND p1.account_yes = account");
					break;
				case 49:
					sql.append(" AND p1.status in (4,49)");
					break;
				case 59:
					sql.append(" AND p1.status in (-2,5,59)");
					break;
				default:
					status = model.getStatus();
					sql.append(" AND p1.status = :status");
					break;
				}
			}
			if (model.getIsNovice() != 99)
			{
				int isNovice = model.getIsNovice();
				switch (isNovice)
				{
				case 1:
					sql.append(" AND p1.is_novice = 1");
					break;
				case 0:
					sql.append(" AND p1.is_novice = 0");
					break;
				default:
					break;
				}
			}
			/*
			 * if (model.getScales() == 99) {
			 * sql.append(" AND p1.account_yes > account"); //
			 * param.addParam("type", Operators.NOTEQ, 110); 删除对流转标的限制 }
			 */
			String searchName = "";
			if (StringUtil.isNotBlank(model.getSearchName()))
			{
				searchName = model.getSearchName();
				sql.append(" AND ( p1.name LIKE :searchName or p1.company_name LIKE :searchName )");
			}
			String borrowName = "";
			if (StringUtil.isNotBlank(model.getBorrowName()))
			{
				borrowName = model.getBorrowName();
				sql.append(" AND p1.name LIKE :borrowName");
			}
			String companyName = "";
			if (StringUtil.isNotBlank(model.getCompanyName()))
			{
				companyName = model.getCompanyName();
				sql.append(" AND p1.company_name = :companyName");
			}
			Date d = DateUtil.getDate(System.currentTimeMillis() / 1000 + "");
			Date startTime = null;
			Date endTime = null;
			if (model.getTime() == 7)
			{
				sql.append(" AND p1.add_time >= :startTime");
				sql.append(" AND p1.add_time <= :endTime");
				startTime = DateUtil.rollDay(d, -7);
				endTime = d;
			}
			else if (model.getTime() > 0 && model.getTime() < 4)
			{
				sql.append(" AND p1.add_time >= :startTime");
				sql.append(" AND p1.add_time <= :endTime");
				startTime = DateUtil.rollMon(d, -model.getTime());
				endTime = d;
			}
			if (StringUtil.isNotBlank(model.getStartTime()))
			{
				startTime = DateUtil.valueOf(model.getStartTime());
				sql.append(" AND p1.add_time > :startTime");
			}
			if (StringUtil.isNotBlank(model.getEndTime()))
			{
				endTime = DateUtil.valueOf(model.getEndTime());
				sql.append(" AND p1.add_time < :endTime");
			}
			int type = 0;
			if (model.getType() != 100 && model.getType() != 0)
			{
				type = model.getType();
				sql.append(" AND p1.type = :type");
			}
			sql.append(this.searchParam(model));
			param.addPage(model.getPage(), model.getSize());
			PageDataList<Borrow> pageDataList = new PageDataList<Borrow>();
			selectSql.append(sql);
			Query query = em.createNativeQuery(selectSql.toString(),
					Borrow.class);
			if (userId > 0)
			{
				query.setParameter("userId", userId);
			}
			if (status > -2)
			{
				query.setParameter("status", status);
			}
			if (startTime != null)
			{
				query.setParameter("startTime", startTime);
			}
			if (endTime != null)
			{
				query.setParameter("endTime", endTime);
			}
			if (searchName.length() > 0)
			{
				query.setParameter("searchName", "%" + searchName + "%");
			}
			if (borrowName.length() > 0)
			{
				query.setParameter("borrowName", "%" + borrowName + "%");
			}
			if (companyName.length() > 0)
			{
				query.setParameter("companyName", companyName);
			}
			if (type > 0)
			{
				query.setParameter("type", type);
			}
			Page page = new Page(query.getResultList().size(), model.getPage(),
					model.getSize());
			query.setFirstResult((model.getPage() - 1) * model.getSize());
			query.setMaxResults(model.getSize());
			List<Borrow> list = query.getResultList();
			pageDataList.setList(list);
			pageDataList.setPage(page);
			return pageDataList;
		}
		return null;
	}

	private String searchParam(BorrowModel model)
	{
		// 利率
		StringBuffer sql = new StringBuffer("");
		if (model.getAprSearch() != -1)
		{
			switch (model.getAprSearch())
			{
			case 1:
				sql.append(" AND p1.apr <= 6");
				break;
			case 2:
				sql.append(" AND p1.apr >= 6 AND p1.apr <= 12");
				break;
			case 3:
				sql.append(" AND p1.apr >= 12 AND p1.apr <= 18");
				break;
			case 4:
				sql.append(" AND p1.apr >= 18 AND p1.apr <= 20");
				break;
			case 5:
				sql.append(" AND p1.apr >= 20");
				break;
			default:
				break;
			}
		}
		// 金额
		if (model.getMoneySearch() != -1)
		{
			switch (model.getMoneySearch())
			{
			case 2:
				sql.append(" AND p1.account <= 10000");
				break;
			case 3:
				sql.append(" AND p1.account >= 10000 AND p1.account <= 50000");
				break;
			case 4:
				sql.append(" AND p1.account >= 50000 AND p1.account <= 100000");
				break;
			case 5:
				sql.append(" AND p1.account >= 100000 AND p1.account <= 200000");
				break;
			case 6:
				sql.append(" AND p1.account >= 200000 AND p1.account <= 500000");
				break;
			case 7:
				sql.append(" AND p1.account >= 500000");
				break;
			default:
				break;
			}
		}
		// 期限
		if (model.getTimeSearch() != -1)
		{
			switch (model.getTimeSearch())
			{
			case 2:
				sql.append(" AND (p1.borrow_time_type = 1 OR p1.time_limit <= 1)");
				break;
			case 3:
				sql.append(" AND p1.borrow_time_type = 0 AND p1.time_limit >= 1 AND p1.time_limit <= 3");
				break;
			case 4:
				sql.append(" AND p1.borrow_time_type = 0 AND p1.time_limit >= 3 AND p1.time_limit <= 6");
				break;
			case 5:
				sql.append(" AND p1.borrow_time_type = 0 AND p1.time_limit >= 6 AND p1.time_limit <= 12");
				break;
			case 6:
				sql.append(" AND p1.borrow_time_type = 0 AND p1.time_limit >= 12");
				break;
			default:
				break;
			}
		}
		sql.append(" GROUP BY p1.id");
		// 排序 1金额 2利率 3进度 4信用
		switch (model.getOrder())
		{
		case 0:
			sql.append(" ORDER BY p1.status ASC , p1.id DESC");
			break;
		case -1:
			sql.append(" ORDER BY p1.account ASC");
			break;
		case 1:
			sql.append(" ORDER BY p1.account DESC");
			break;
		case -2:
			sql.append(" ORDER BY p1.apr ASC");
			break;
		case 2:
			sql.append(" ORDER BY p1.apr DESC");
			break;
		case -3:
			sql.append(" ORDER BY p1.scales ASC");
			break;
		case 3:
			sql.append(" ORDER BY p1.scales DESC");
			break;
		case -4:
			break;
		case 4:
			break;
		default:
			sql.append(" ORDER BY p1.is_recommend DESC, p1.id DESC");
			break;
		}
		return sql.toString();
	}

	@Override
	public double getBorrowAccountByDate(String date)
	{
		String dateFormat = "%Y-%m-%d";
		if (date.length() == 7)
		{
			dateFormat = "%Y-%m";
		}
		String sql = "select sum(account) from rd_borrow where date_format(add_time,'"
				+ dateFormat + "') = ?";
		Query query = em.createNativeQuery(sql).setParameter(1, date);
		Object obj = query.getSingleResult();
		double count = 0;
		if (obj != null)
		{
			count = ((BigDecimal) obj).doubleValue();
		}
		return count;
	}

	@Override
	public Borrow getBorrowByBidNo(String bidNo)
	{
		List<Borrow> list = super.findByProperty("bidNo", bidNo);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public int count()
	{
		String sql = "SELECT COUNT(p1.id) FROM rd_borrow p1 WHERE (p1.type <> 110 AND p1.status IN (3,6,7,8)) OR (p1.type = 110 AND p1.status IN (1,8))";
		Query query = em.createNativeQuery(sql);
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public int getGuaranteeingCount(long userId)
	{
		String sql = "SELECT count(t.id) FROM rd_borrow t WHERE t.vouch_id=:vouch_id AND t.status IN ('1','3','6','7') AND LENGTH(t.guarantee_no) >0 AND t.guarantee_rate > 0";
		Query query = em.createNativeQuery(sql);
		query.setParameter("vouch_id", userId);
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public double getGuaranteeingAccount(long userId)
	{
		String sql = "SELECT SUM(t.account) FROM rd_borrow t WHERE t.vouch_id=:vouch_id AND t.status IN ('1','3','6','7') AND LENGTH(t.guarantee_no) >0 AND t.guarantee_rate > 0";
		Query query = em.createNativeQuery(sql);
		query.setParameter("vouch_id", userId);
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Double.parseDouble(count.toString());
		}
		return 0;
	}

	@Override
	public int getNeedGuaranteeRegisteCount(long userId)
	{
		String sql = "SELECT count(t.id) FROM rd_borrow t WHERE t.vouch_id=:vouch_id AND t.status ='11' AND (t.guarantee_no is null OR LENGTH(t.guarantee_no) = 0)  AND t.guarantee_rate > 0";
		Query query = em.createNativeQuery(sql);
		query.setParameter("vouch_id", userId);
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public double getNeedGuaranteeRegisteAccount(long userId)
	{
		String sql = "SELECT SUM(t.account) FROM rd_borrow t WHERE t.vouch_id=:vouch_id AND t.status ='11' AND (t.guarantee_no is null OR LENGTH(t.guarantee_no) = 0) AND t.guarantee_rate > 0";
		Query query = em.createNativeQuery(sql);
		query.setParameter("vouch_id", userId);
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Double.parseDouble(count.toString());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Borrow> getNeedGuaranteeRegisteList(long userId)
	{
		String sql = "SELECT t.* FROM rd_borrow t WHERE t.vouch_id=:vouch_id AND t.status ='11' AND (t.guarantee_no is null OR LENGTH(t.guarantee_no) = 0) AND t.guarantee_rate > 0";
		Query query = em.createNativeQuery(sql, Borrow.class);
		query.setParameter("vouch_id", userId);
		List<Borrow> list = query.getResultList();
		return list;
	}

	@Override
	public int getAllOverduedCount(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT COUNT(DISTINCT b.id) FROM rd_borrow b, rd_borrow_repayment r WHERE b.id = r.borrow_id AND b.status = 8 AND r.repayment_time < r.repayment_yes_time");
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND r.repayment_yes_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND r.repayment_yes_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public int getAllOverdueingCount(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT COUNT(DISTINCT b.id) FROM rd_borrow b, rd_borrow_repayment r WHERE b.id = r.borrow_id AND b.status in(6, 7)  AND r.status = 0 AND r.repayment_time < NOW()");
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND r.repayment_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND r.repayment_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public double getAllOverdueingMomeny()
	{
		String sql = "SELECT SUM(b.account) FROM rd_borrow b WHERE b.status in(6, 7)  AND b.id IN(SELECT DISTINCT r.borrow_id FROM rd_borrow_repayment r WHERE r.status = 0 AND r.repayment_time < NOW())";
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getAllOverdueingMoney(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT SUM(b.account) FROM rd_borrow b WHERE b.status in(6, 7)");
		StringBuffer str = new StringBuffer(
				"SELECT DISTINCT r.borrow_id FROM rd_borrow_repayment r WHERE r.status = 0 AND r.repayment_time < NOW()");
		if (StringUtil.isNotBlank(startTime))
		{
			str.append(" AND r.repayment_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			str.append(" AND r.repayment_time <= :endTime");
		}
		sql.append(" AND b.id IN (").append(str).append(")");
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public int getAllCount(int status, String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT COUNT(id) FROM rd_borrow WHERE 1=1 ");
		if (status != 99)
		{
			sql.append(" AND status = :status");
		}
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (status != 99)
		{
			query.setParameter("status", status);
		}
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public double getAllMomeny()
	{
		String sql = "SELECT SUM(b.account) FROM rd_borrow b where  b.status in(6, 7,8)";
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getAllOverduedMomeny(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT SUM(b.account) FROM rd_borrow b WHERE b.status =8 ");
		StringBuffer str = new StringBuffer(
				"SELECT DISTINCT r.borrow_id FROM rd_borrow_repayment r WHERE r.repayment_time < r.repayment_yes_time");
		if (StringUtil.isNotBlank(startTime))
		{
			str.append(" AND r.repayment_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			str.append(" AND r.repayment_time <= :endTime");
		}
		sql = sql.append(" AND b.id IN (").append(str).append(")");
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getMomenyByStatus(int status)
	{
		String sql = "SELECT SUM(b.account) FROM rd_borrow b WHERE b.status = :status ";
		Query query = em.createNativeQuery(sql);
		query.setParameter("status", status);
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getVerifyFullMomeny()
	{
		String sql = "SELECT SUM(b.account) FROM rd_borrow b WHERE b.status = 1 AND b.scales = 100 ";
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getVerifyFullMoney(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT SUM(b.account) FROM rd_borrow b WHERE b.status = 1 AND b.scales = 100 ");
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageDataList<Borrow> getConfirmedBorrowList(BorrowModel model)
	{

		QueryParam param = QueryParam.getInstance();
		StringBuffer selectSql = new StringBuffer(
				"SELECT p1.* FROM rd_borrow p1, rd_user p2 WHERE p1.user_id = p2.user_id AND p1.status = 11 AND ((LENGTH(p1.guarantee_no) >0 AND p1.guarantee_rate > 0) OR p1.guarantee_rate = 0) ");
		StringBuffer sql = new StringBuffer();
		if (model != null)
		{
			long userId = 0;
			if (model.getUser() != null && model.getUser().getUserId() > 0)
			{
				sql.append(" AND p1.user_id = :userId");
				userId = model.getUser().getUserId();
			}
			int status = 99;
			if (model.getStatus() != 99)
			{
				switch (model.getStatus())
				{
				case 1:
					String scalesSql = "";
					if (model.getScales() == 100)
					{
						scalesSql = " AND p1.account_yes = account AND p1.scales = 100";
					}
					else
					{
						scalesSql = " AND p1.account_yes <= account";
					}
					sql.append(" AND p1.status = 1 " + scalesSql);
					break;
				case 19:
					sql.append(" AND p1.status = 1 AND p1.account_yes = account");
					break;
				case 49:
					sql.append(" AND p1.status in (4,49)");
					break;
				case 59:
					sql.append(" AND p1.status in (5,59)");
					break;
				default:
					status = model.getStatus();
					sql.append(" AND p1.status = :status");
					break;
				}
			}
			if (model.getScales() == 99)
			{
				sql.append(" AND p1.account_yes > account");
			}
			String borrowName = "";
			if (StringUtil.isNotBlank(model.getName())
					&& !"undefined".equals(model.getName())
					&& model.getName().length() > 0)
			{
				borrowName = model.getName();
				sql.append(" AND p1.name LIKE :borrowName");
			}
			String userName = "";
			if (StringUtil.isNotBlank(model.getUserName()))
			{
				userName = model.getUserName();
				sql.append(" AND p2.user_name LIKE :userName");
			}
			Date d = DateUtil.getDate(System.currentTimeMillis() / 1000 + "");
			Date startTime = null;
			Date endTime = null;
			if (model.getTime() == 7)
			{
				sql.append(" AND p1.add_time >= :startTime");
				sql.append(" AND p1.add_time <= :endTime");
				startTime = DateUtil.rollDay(d, -7);
				endTime = d;
			}
			else if (model.getTime() > 0 && model.getTime() < 4)
			{
				sql.append(" AND p1.add_time >= :startTime");
				sql.append(" AND p1.add_time <= :endTime");
				startTime = DateUtil.rollMon(d, -model.getTime());
				endTime = d;
			}
			if (StringUtil.isNotBlank(model.getStartTime()))
			{
				startTime = DateUtil.valueOf(model.getStartTime());
				sql.append(" AND p1.add_time > :startTime");
			}
			if (StringUtil.isNotBlank(model.getEndTime()))
			{
				endTime = DateUtil.valueOf(model.getEndTime());
				sql.append(" AND p1.add_time < :endTime");
			}
			int type = 0;
			if (model.getType() != 100 && model.getType() != 0)
			{
				type = model.getType();
				sql.append(" AND p1.type = :type");
			}
			sql.append(this.searchParam(model));
			param.addPage(model.getPage(), model.getSize());
			PageDataList<Borrow> pageDataList = new PageDataList<Borrow>();
			selectSql.append(sql);
			Query query = em.createNativeQuery(selectSql.toString(),
					Borrow.class);
			if (userId > 0)
			{
				query.setParameter("userId", userId);
			}
			if (status != 99)
			{
				query.setParameter("status", status);
			}
			if (startTime != null)
			{
				query.setParameter("startTime", startTime);
			}
			if (endTime != null)
			{
				query.setParameter("endTime", endTime);
			}
			if (borrowName.length() > 0)
			{
				query.setParameter("borrowName", "%" + borrowName + "%");
			}
			if (userName.length() > 0)
			{
				query.setParameter("userName", "%" + userName + "%");
			}
			if (type > 0)
			{
				query.setParameter("type", type);
			}
			Page page = new Page(query.getResultList().size(), model.getPage(),
					model.getSize());
			query.setFirstResult((model.getPage() - 1) * model.getSize());
			query.setMaxResults(model.getSize());
			List<Borrow> list = query.getResultList();
			pageDataList.setList(list);
			pageDataList.setPage(page);
			return pageDataList;
		}
		return null;
	}

	@Override
	public void updateRepaymentAccount(long borrowId, double repayMoney)
	{
		String sql = "UPDATE Borrow SET repaymentAccount = :repayMoney WHERE id = :id";
		Query query = em.createQuery(sql);
		query.setParameter("repayMoney", repayMoney);
		query.setParameter("id", borrowId);
		int result = query.executeUpdate();
		if (result != 1)
		{
			throw new BussinessException(
					"标审核，更改borrow表中repaymentAccount与repayment表中repaymentAccount保持一致失败！",
					1);
		}
		refresh(super.find(borrowId));
	}

	@Override
	public long ipsTrialCount()
	{
		String sql = "select count(*) from Borrow where status=11 AND ((LENGTH(guaranteeNo) >0 AND guaranteeRate > 0) OR guaranteeRate = 0)";
		Query query = em.createQuery(sql);
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Borrow getLastBorrow()
	{
		String sql = "SELECT p1.* FROM rd_borrow p1,rd_user p2,rd_verify_log p3 WHERE p1.user_id = p2.user_id AND "
				+ "(p1.id=p3.fid AND p3.type='borrow' AND p3.result = 1 AND p3.verify_type = 1 AND DATE_ADD(p3.time, INTERVAL p1.valid_time DAY) >= NOW() "
				+ "AND p1.status = 1  AND p1.account_yes <= p1.account OR p1.type =110 AND p1.status = 1 AND p1.scales!=100 OR p1.status IN (3,6,7,8))"
				+ " GROUP BY p1.id ORDER BY p1.status ASC , p1.id DESC limit 1";
		Query query = em.createNativeQuery(sql, Borrow.class);
		List<Borrow> list = query.getResultList();
		Borrow borrow = new Borrow();
		if (list != null && list.size() > 0)
		{
			borrow = list.get(0);
		}
		return borrow;
	}

	@Override
	public int getInviteCount()
	{
		String sql = "SELECT count(*) FROM rd_borrow p1,rd_verify_log p2 "
				+ "WHERE  (p1.id=p2.fid AND p2.type='borrow' AND p2.result = 1 AND p2.verify_type = 1 AND DATE_ADD(p2.time, INTERVAL p1.valid_time DAY) >= NOW() "
				+ "AND p1.status = 1  AND p1.account_yes <= p1.account  AND p1.scales!=100)";
		Query query = em.createNativeQuery(sql);
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public int getInviteCount(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT count(*) FROM rd_borrow p1,rd_verify_log p2 "
						+ "WHERE  (p1.id=p2.fid AND p2.type='borrow' AND p2.result = 1 AND p2.verify_type = 1 AND DATE_ADD(p2.time, INTERVAL p1.valid_time DAY) >= NOW() "
						+ "AND p1.status = 1  AND p1.account_yes <= p1.account  AND p1.scales!=100)");
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND p1.add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND p1.add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public double getInviteMoney()
	{
		String sql = "SELECT sum(p1.account) FROM rd_borrow p1,rd_verify_log p2 "
				+ "WHERE  (p1.id=p2.fid AND p2.type='borrow' AND p2.result = 1 AND p2.verify_type = 1 AND DATE_ADD(p2.time, INTERVAL p1.valid_time DAY) >= NOW() "
				+ "AND p1.status = 1  AND p1.account_yes <= p1.account  AND p1.scales!=100)";
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public double getInviteMoney(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT sum(p1.account) FROM rd_borrow p1,rd_verify_log p2 "
						+ "WHERE  (p1.id=p2.fid AND p2.type='borrow' AND p2.result = 1 AND p2.verify_type = 1 AND DATE_ADD(p2.time, INTERVAL p1.valid_time DAY) >= NOW() "
						+ "AND p1.status = 1  AND p1.account_yes <= p1.account  AND p1.scales!=100)");
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND p1.add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND p1.add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@SuppressWarnings("unused")
	@Override
	public int getBorrowCount(String startTime, String endTime, int... status)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT COUNT(1) FROM rd_borrow where status in ");
		StringBuffer str = new StringBuffer("(");
		int i = 0;
		for (int s : status)
		{
			if (i == status.length - 1)
			{
				str.append(status[i] + ")");
			}
			else
			{
				str.append(status[i] + ",");
			}
			i++;
		}
		sql.append(str);
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@SuppressWarnings("unused")
	@Override
	public double getBorrowAccount(String startTime, String endTime,
			int... status)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT SUM(account) FROM rd_borrow where 1=1 ");
		if (status[0] != 99)
		{
			StringBuffer str = new StringBuffer("(");
			int i = 0;
			for (int s : status)
			{
				if (i == status.length - 1)
				{
					str.append(status[i] + ")");
				}
				else
				{
					str.append(status[i] + ",");
				}
				i++;
			}
			sql.append(" AND status in").append(str);
		}
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object obj = query.getSingleResult();
		if (obj != null)
		{
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}

	@Override
	public int getVerifyFullCount(String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer(
				"SELECT COUNT(id) FROM rd_borrow WHERE status=1 AND scales != 100 ");
		if (StringUtil.isNotBlank(startTime))
		{
			sql.append(" AND add_time >= :startTime");
		}
		if (StringUtil.isNotBlank(endTime))
		{
			sql.append(" AND add_time <= :endTime");
		}
		Query query = em.createNativeQuery(sql.toString());
		if (StringUtil.isNotBlank(startTime))
		{
			query.setParameter("startTime", startTime);
		}
		if (StringUtil.isNotBlank(endTime))
		{
			query.setParameter("endTime", endTime);
		}
		Object count = query.getSingleResult();
		if (count != null)
		{
			return NumberUtil.getInt(count.toString());
		}
		return 0;
	}

	@Override
	public int getBorrowUserCount()
	{
		String sql = "select count(distinct(user_id)) from rd_borrow";
		Query query = em.createNativeQuery(sql);
		Object count = query.getSingleResult();
		if (count != null)
		{
			return Integer.parseInt(count.toString());
		}
		return 0;
	}

	@Override
	public List<Borrow> getBorrowListByStatusAndType()
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("status", 6);
		param.addParam("type", 119);
		return super.findByCriteria(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Borrow> getAppointmentBorrow()
	{
		String sql = "select * from rd_borrow where TO_DAYS(fixed_time) = TO_DAYS(now()) and status = 0";
		Query query = em.createNativeQuery(sql, Borrow.class);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Borrow> getFullAndFailBorrow()
	{
		String sql = " select b.* from rd_borrow b join rd_verify_log l on b.id = l.fid " +
				" where b.status = 1 and l.verify_type = 1 and l.type = 'borrow' " +
				" and date_add(l.time,interval b.valid_time day) >= NOW() and  " +
				" date_add(date_add(l.time,interval b.valid_time day), interval -2 hour) <= NOW() " +
				/*上面查询流标  下面查询满标*/
				" union  " +
				" select b.* from rd_borrow b join rd_verify_log l on b.id = l.fid " +
				" where b.status = 1 and b.scales >= 80 and l.verify_type = 1 and l.type = 'borrow' " +
				" and date_add(l.time,interval b.valid_time day) > NOW() "; 
		Query query = em.createNativeQuery(sql, Borrow.class);
		return query.getResultList();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public BorrowModel getLastBorrow(int borrowType)
	{
		String sql = "SELECT p1.* FROM rd_borrow p1, rd_user p2, rd_verify_log p3 "
				+ "WHERE p1.user_id = p2.user_id AND ( p1.id = p3.fid AND p3.type = 'borrow' AND p3.result = 1 "
				+ "AND p3.verify_type = 1 AND ((DATE_ADD( p3.time, INTERVAL p1.valid_time DAY ) >= NOW() AND p1.status = 1) or p1.STATUS in (3,6,7,8))) "
				+ "AND p1.type = :type GROUP BY p1.id ORDER BY  p1.scales ASC, p1.id DESC limit 1";
		Query query = em.createNativeQuery(sql, Borrow.class).setParameter(
				"type", borrowType);
		List<Borrow> list = query.getResultList();
		if (list != null && list.size() > 0)
		{
			Borrow borrow = list.get(0);
			// 获取借款标头像
			QueryParam param = QueryParam.getInstance()
					.addParam("borrow.id", borrow.getId()).addParam("type", 5);
			List<BorrowUpload> uploads = borrowUploadDao.findByCriteria(param);
			BorrowModel model = BorrowModel.instance(borrow);
			if (uploads != null && uploads.size() > 0)
			{
				model.setBorrowImg(uploads.get(0).getPicPath());
			}
			String userName = borrow.getUser().getUserName();
			model.setUserName(userName.charAt(0) + "******"
					+ userName.charAt(userName.length() - 1));
			return model;
		}
		return null;
	}

	@Override
	public BorrowModel getLastUnRecommendBorrow(int borrowType)
	{
		String sql = "SELECT p1.* FROM rd_borrow p1, rd_user p2, rd_verify_log p3 "
				+ "WHERE p1.user_id = p2.user_id AND ( p1.id = p3.fid AND p3.type = 'borrow' AND p3.result = 1 "
				+ "AND p3.verify_type = 1 AND DATE_ADD( p3.time, INTERVAL p1.valid_time DAY ) >= NOW() AND p1. STATUS = 1 "
				+ "AND p1.account_yes < account OR p1.type = 110 AND p1. STATUS = 1 AND p1.account_yes < account ) "
				+ "AND p1.is_novice = 0 AND p1.type = :type GROUP BY p1.id ORDER BY p1. STATUS ASC, p1.id DESC limit 1";
		Query query = em.createNativeQuery(sql, Borrow.class).setParameter(
				"type", borrowType);
		List<Borrow> list = query.getResultList();
		if (list != null && list.size() > 0)
		{
			Borrow borrow = list.get(0);
			// 获取借款标头像
			QueryParam param = QueryParam.getInstance()
					.addParam("borrow.id", borrow.getId()).addParam("type", 5);
			List<BorrowUpload> uploads = borrowUploadDao.findByCriteria(param);
			BorrowModel model = BorrowModel.instance(borrow);
			if (uploads != null && uploads.size() > 0)
			{
				model.setBorrowImg(uploads.get(0).getPicPath());
			}
			String userName = borrow.getUser().getUserName();
			model.setUserName(userName.charAt(0) + "******"
					+ userName.charAt(userName.length() - 1));
			return model;
		}
		return null;
	}

	@Override
	public List<Borrow> getListByIsRecommend()
	{
		List<Borrow> list = super.findByProperty("isRecommend", true);
		return list;
	}

}
