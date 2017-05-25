package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.Menu;

public interface MenuDao extends BaseDao<Menu> {

	/**
	 * 根据菜单状态来获取菜单
	 * 
	 * @param isDelete 菜单状态：0：删除，1：不删除
	 * @return 菜单集合
	 */
	List<Menu> menuList(boolean isDelete);

	/**
	 * 根据pid查询当前目录的子目录
	 * 
	 * @param pid 父级id
	 * @return 菜单目录
	 */
	List<Menu> menuFindByPid(long pid);

	/**
	 * 根据父类ID删除子类信息(逻辑删除)
	 * 
	 * @param pid 父类ID
	 */
	void update(long pid);

	/**
	 * 获取使用中的菜单栏
	 * 
	 * @param userId 用户ID
	 * @param isMenu 是否菜单
	 * @return 菜单
	 */
	List<Menu> menuUseList(long userId, boolean isMenu);

	/**
	 * 获取用户是否有此url的权限
	 * 
	 * @param operatorId 用户ID
	 * @param href 菜单路径
	 * @return 菜单
	 */
	Menu getMenuPermission(long operatorId, String href);
	
	/**
	 * 获取使用中的菜单栏
	 * 
	 * @param userId 用户ID
	 * @param isMenu 是否菜单
	 * @return 菜单
	 */
	List<Menu> getMenuUseList(long userId,long parentId, boolean isMenu);

}
