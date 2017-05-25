package com.rongdu.p2psys.core.model;

import java.util.List;

/**
 * tree节点
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class TreeModel {
	private long id;
	private String text;
	private List<TreeModel> children;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}
}
