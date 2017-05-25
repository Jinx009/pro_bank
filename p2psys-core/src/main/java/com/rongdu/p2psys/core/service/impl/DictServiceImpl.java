package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.DictDao;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.model.DictModel;
import com.rongdu.p2psys.core.service.DictService;

@Service("dicService")
public class DictServiceImpl implements DictService {

	@Resource
	private DictDao dictDao;

	@Override
	public PageDataList<Dict> list(DictModel model) {
		QueryParam param = QueryParam.getInstance();
		if(!StringUtil.isBlank(model.getSearchName())){//模糊查询条件
			SearchFilter orFilter1 = new SearchFilter("nid", Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("name", Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2);
		}else{ //精确查询条件
			if (StringUtil.isNotBlank(model.getNid())) {
				param.addParam("nid", model.getNid());
			}
			if (StringUtil.isNotBlank(model.getName())) {
				param.addParam("name", Operators.EQ, model.getName());
			}
			param.addParam("status", model.getStatus());
		}
		param.addOrder(OrderType.DESC, "id");
		param.addPage(model.getPage(), model.getRows());
		
		return dictDao.list(param);
	}

	@Override
	public Dict find(long id) {
		return dictDao.find(id);
	}

	@Override
	public Dict find(String nid, String value) {
		return dictDao.find(nid, value);
	}

	@Override
	public List<Dict> list(String nid) {
		return dictDao.list(nid);
	}

	@Override
	public Dict update(Dict dict) {
		return dictDao.update(dict);
	}

	@Override
	public void delete(long id) {
		dictDao.delete(id);
	}

	@Override
	public void add(Dict dict) {
		dictDao.save(dict);
	}

}
