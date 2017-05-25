package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.AttentionListDao;
import com.rongdu.p2psys.crowdfunding.dao.ProjectBaseinfoDao;
import com.rongdu.p2psys.crowdfunding.domain.AttentionList;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.model.AttentionListModel;
import com.rongdu.p2psys.crowdfunding.service.AttentionListService;
import com.rongdu.p2psys.crowdfunding.service.MaterialsService;

@Service("attentionListService")
public class AttentionListServiceImpl implements AttentionListService{

	@Resource
	private AttentionListDao attentionListDao;
	@Resource
	private ProjectBaseinfoDao projectBaseinfoDao;
	@Resource
	private MaterialsService materialsService;
	
	public AttentionList findById(Integer id) {
		return attentionListDao.find(id);
	}

	/**
	 * 我关注的
	 */
	public List<AttentionListModel> getByUserId(Long userId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM AttentionList WHERE userId=");
		buffer.append(userId);
		List<AttentionList> list = attentionListDao.getByHql(buffer.toString());
		List<AttentionListModel> modelList = null;
		if(null!=list){
			modelList = new ArrayList<AttentionListModel>();
			for(int i =0;i<list.size();i++){
				AttentionListModel model = new AttentionListModel();
				model = AttentionListModel.instance(list.get(i));
				ProjectBaseinfo projectBaseinfo = projectBaseinfoDao.find(model.getProjectId());
				List<Materials> materials = materialsService.findByProjectId(model.getProjectId());
				model.setProjectName(projectBaseinfo.getProjectName());
				model.setTimeStatus(checkDate(projectBaseinfo));
				model.setType(projectBaseinfo.getType());
				model.setMaterials(materials);
				
				modelList.add(model);
			}
		}
		return modelList;
	}

	public List<AttentionList> getByProjectId(Long id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM AttentionList WHERE projectId =");
		buffer.append(id);
		return attentionListDao.getByHql(buffer.toString());
	}

	public void delete(Integer id) {
		attentionListDao.delete(id);
	}

	public AttentionList save(AttentionList attentionList) {
		return attentionListDao.save(attentionList);
	}

	public void update(AttentionList attentionList) {
		attentionListDao.update(attentionList);
	}

	/**
	 * 通过用户id和项目id查找记录
	 */
	public List<AttentionList> getByDoubleId(Long projectId, long userId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM AttentionList  WHERE projectId =");
		buffer.append(projectId);
		buffer.append(" AND userId =");
		buffer.append(userId);
		return attentionListDao.getByHql(buffer.toString());
	}

	
	
	/**
	 * 获取项目真实状态
	 * @param base
	 * @return
	 */
	private static int checkDate(ProjectBaseinfo base) {
		Date nowdate = new Date();
		Date s = base.getStartTime();
		Date e = base.getEndTime();

		boolean sflag = nowdate.before(s);
		boolean eflag = e.before(nowdate);
		if (3 == base.getStatus()) {
			return 4;
		}
		if (sflag) {
			return 1;// 预热
		}
		if (eflag) {
			return 3;// 失败
		}
		return 2;// 众筹中
	}
}
