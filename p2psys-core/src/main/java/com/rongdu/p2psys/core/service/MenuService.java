package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.p2psys.core.domain.Menu;

/**
 * 菜单Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月16日
 */
public interface MenuService {

	/**
	 * 添加菜单
	 * 
	 * @param menu 菜单对象
	 * @return 返回菜单对象
	 */
	Menu menuAdd(Menu menu);

	/**
	 * 跟新菜单
	 * 
	 * @param menu 需更新的对象
	 * @return 更新后对象
	 */
	Menu menuUpdate(Menu menu);

	/**
	 * 查询菜单
	 * 
	 * @param id 对象id
	 * @return 返回菜单对象
	 */
	Menu menuFind(long id);

	/**
	 * 删除菜单：理论删除
	 * 
	 * @param menu 删除对象
	 */
	void menuDelete(Menu menu);

	/**
	 * 获取使用中的菜单栏
	 * 
	 * @return 菜单集合
	 */
	List<Menu> menuUseList();

	/**
	 * 删除菜单，删除时，要删除当前结构下的子菜单 当前考虑三级
	 * 
	 * @param id 主键
	 */
	void menuDelete(long id);

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
	 * @return 是否拥有此菜单的权限
	 */
	boolean getMenuPermission(long operatorId, String href);

	/**
	 * 根据href查询菜单信息
	 * 
	 * @param href
	 * @return
	 */
	Menu getMenuByHref(String href);
	
	/**
	 * 获取使用中的菜单栏
	 * 
	 * @param userId 用户ID
	 * @param isMenu 是否菜单
	 * @return 菜单
	 */
	List<Menu> getMenuUseList(long userId,long parentId, boolean isMenu);
}
