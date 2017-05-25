package com.rongdu.p2psys.core.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.MenuDao;
import com.rongdu.p2psys.core.domain.Menu;
import com.rongdu.p2psys.core.service.MenuService;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

	@Resource
	private MenuDao menuDao;

	public Menu menuAdd(Menu menu) {
		menu.setAddTime(new Date());
		menu.setOpenType(Menu.getCurrOpenType());
		return menuDao.save(menu);
	}

	public Menu menuUpdate(Menu menu) {
		menu.setUpdateTime(new Date());
		menu.setOpenType(Menu.getCurrOpenType());
		return menuDao.update(menu);
	}

	public Menu menuFind(long id) {
		return menuDao.find(id);
	}

	public void menuDelete(Menu menu) {
		menu.setDelete(false);
		menuDao.update(menu);
	}

	public List<Menu> menuUseList() {
		QueryParam param = QueryParam.getInstance();
		param.addParam("isDelete", false);
		param.addParam("openType", Menu.getCurrOpenType());
		param.addOrder(OrderType.ASC, "sort");
		return menuDao.findByCriteria(param);
	}

	public List<Menu> menuUseList(long userId, boolean isMenu) {
		return menuDao.menuUseList(userId, isMenu);
	}

	public void menuDelete(long id) {
		Menu menu = menuDao.find(id); // 查询当前删除节点级别
		List<Menu> menuChilds = menuDao.menuFindByPid(menu.getId()); // 查询删除目录下子目录
		if (menuChilds != null && menuChilds.size() > 0) {
			for (Menu mu : menuChilds) {
				List<Menu> menuLastChilds = menuDao.menuFindByPid(mu.getId()); // 最后一级
				if (menuLastChilds != null && menuLastChilds.size() > 0) {
					for (Menu meu : menuLastChilds) {
						meu.setDelete(true);
					}
				}
				menuDao.update(menuLastChilds); // 更新最低一级菜单
				mu.setDelete(true);
			}
			menuDao.update(menuChilds);
		}
		menu.setDelete(true);
		menuDao.update(menu);
	}

	@Override
	public boolean getMenuPermission(long operatorId, String href) {
		QueryParam param = QueryParam.getInstance().addParam("href", href);
		param.addParam("isDelete", false);
		param.addParam("openType", Menu.getCurrOpenType());
		List<Menu> menuList = menuDao.findByCriteria(param);
		Menu menu = menuDao.getMenuPermission(operatorId, href);
		if ((menuList == null || menuList.size() <= 0) && menu == null) {
			return true;
		} else if (menuList != null && menu != null) {
			return true;
		}
		return false;
	}

	@Override
	public Menu getMenuByHref(String href) {
		QueryParam param = QueryParam.getInstance().addParam("href", href);
		param.addParam("isDelete", false);
		param.addParam("openType", Menu.getCurrOpenType());
		List<Menu> menuList = menuDao.findByCriteria(param);
		if (menuList != null && menuList.size() > 0) {
			return menuList.get(0);
		}
		return null;
	}

	@Override
	public List<Menu> getMenuUseList(long userId, long parentId, boolean isMenu) {
		// TODO Auto-generated method stub
		return menuDao.getMenuUseList(userId, parentId, isMenu);
	}
}
