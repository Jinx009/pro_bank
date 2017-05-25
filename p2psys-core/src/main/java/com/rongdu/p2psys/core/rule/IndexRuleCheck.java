package com.rongdu.p2psys.core.rule;

import java.util.List;

/**
 * 首页规则
 * 
 * @author cx
 * @version 2.0
 * @since 2014-02-14
 */
public class IndexRuleCheck extends RuleCheck {

	public BorrowRule borrow;
	public int newTender;
	public int success;
	public int statics;
	public Links links;
	public Rank rank;
	/** 统计月标（默认1） **/
	public int total_rank_list_ofmonth;

	public static class BorrowRule {
		/** 1=招标状态 **/
		public int status_type;
		/** 排序规则 **/
		public String sort_type;
		/** 需要显示的标种 **/
		public List<Integer> borrow_type;
		public List<Integer> borrow_num;
		/** 是否启用 0不启用 1启用 **/
		public int status;
		/** 显示的方式，可以组合或者全表排序 **/
		public int display;
		/** 默认显示个数 **/
		public int default_num;
	}

	public static class Links {
		public int status;
		public int num;
	}

	public static class Rank {
		public int status;
		public int day;
	}

	@Override
	public boolean checkRule() {
		return false;
	}

}
