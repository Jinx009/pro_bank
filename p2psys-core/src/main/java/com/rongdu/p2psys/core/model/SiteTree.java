package com.rongdu.p2psys.core.model;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.p2psys.core.domain.Site;

public class SiteTree implements Tree<Site> {

	private Site model;

	private Tree parent;

	private List<Tree> child;

	public SiteTree() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public SiteTree(Site model, List<Tree> child) {
		super();
		this.model = model;
		this.child = child;
	}

	@SuppressWarnings("rawtypes")
	public SiteTree(Tree parent, Site modelNew) {
		super();
		this.parent = parent;
		this.model = modelNew;
		if (modelNew.getLeaf() == 0) {
			this.child = new ArrayList<Tree>();
		}
	}

	@Override
	public boolean hasChild() {
		if (child != null && child.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Tree> getChild() {
		return child;
	}

	@Override
	public Tree getParent() {
		return parent;
	}

	@Override
	public Site getModel() {
		return model;
	}

	@Override
	public void addChild(Tree t) {
		child.add(t);
	}

	public void setModel(Site model) {
		this.model = model;
	}

	public void setParent(Tree parent) {
		this.parent = parent;
	}

	public void setChild(List<Tree> child) {
		this.child = child;
	}

}
