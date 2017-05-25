package com.rongdu.p2psys.borrow.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.borrow.dao.BorrowUploadDao;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;

/**
 * 上传图片
 * 
 * @author sj
 * @version 2.0
 * @since 2014年4月16日
 */
@Repository("borrowUploadDao")
public class BorrowUploadDaoImpl extends BaseDaoImpl<BorrowUpload> implements BorrowUploadDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowUpload> findPicByBorrowId(long id) {
		Query query = em.createQuery("from BorrowUpload where borrow.id=?")
				.setParameter(1, id);
		return query.getResultList();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<BorrowUpload> findByMortgageId(long id) {
        Query query = em.createQuery("from BorrowUpload where borrowMortgage.id=?")
                .setParameter(1, id);
        return query.getResultList();
    }

	@Override
	public List<BorrowUpload> findByBorrowIdAndType(long id, int type) {
		 QueryParam param = QueryParam.getInstance().addParam("borrow.id", id).addParam("type", type);
	     return super.findByCriteria(param);
	}

}
