package com.rongdu.p2psys.account.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.account.dao.TppGlodLogDao;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.domain.TppGlodLog;
import com.rongdu.p2psys.account.model.TppGlodLogModel;
import com.rongdu.p2psys.account.service.TppGlodLogService;

/**
 * 平台账户操作记录
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年2月4日
 */
@Service("tppGlodLogService")
public class TppGlodLogServiceImpl implements TppGlodLogService {
	@Resource
	private TppGlodLogDao tppGlodLogDao;

	@Override
	public void save(TppGlodLog glodLog) {
		tppGlodLogDao.save(glodLog);
	}

	@Override
	public void update(TppGlodLog glodLog) {
		tppGlodLogDao.merge(glodLog);
	}

	@Override
	public TppGlodLog findByOrdId(long ordId) {
		return tppGlodLogDao.findObjByProperty("ordId", ordId);
	}

	@Override
	public PageDataList<TppGlodLogModel> list(TppGlodLogModel glodLogModel) {
		return tppGlodLogDao.list(glodLogModel);
	}

	@Override
	public void setTppGlodLogStatus() {
		try {
            Date d=new Date();
            long now=d.getTime();
            int dayTime=24*3600*1000;
            List<TppGlodLog> list=tppGlodLogDao.findAll();
            for (TppGlodLog log:list) {
                if((now-log.getAddtime().getTime())>= dayTime && log.getStatus()==0){
                	log.setStatus((byte)2);
                	log.setMemo("失败");
                	tppGlodLogDao.merge(log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
