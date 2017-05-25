package com.rongdu.manage.action.home;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.core.web.BaseAction;

/**
 * Layouts
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月15日
 */
public class LayoutsAction extends BaseAction {

	@Action("/layouts/north")
	public String north() {

		return "north";
	}

	@Action("/layouts/west")
	public String west() {

		return "west";
	}

	@Action("/layouts/south")
	public String south() {

		return "south";
	}

	@Action("/layouts/east")
	public String east() {

		return "east";
	}

	@Action("/layouts/main")
	public String main() {

		return "main";
	}

	@Action("/icons")
	public String icons() {

		return "icons";
	}

}
