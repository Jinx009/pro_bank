package com.rongdu.common.model.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;

public class QueryParam {

	List<SearchFilter> filters = new ArrayList<SearchFilter>();
	List<OrderFilter> orders = new ArrayList<OrderFilter>();
	Map<String, Class> joins = new HashMap<String, Class>();
	public Map<String, Object> map = new HashMap<String, Object>();
	Map<String, Join> cacheJoins = new HashMap<String, Join>();
	public Page page;

	public QueryParam() {
		super();
	}

	/**
	 * 初始化实列
	 * 
	 * @return
	 */
	public static QueryParam getInstance() {
		return new QueryParam();
	}

	public QueryParam addParam(String key, Object value) {
		if (StringUtil.isBlank(value)) {
			return this;
		}
		map.put(key, value.toString());
		SearchFilter filter = new SearchFilter(key, Operators.EQ, value);
		filters.add(filter);
		return this;
	}

	public QueryParam addParam(String key, Operators op, Object value) {
		if (StringUtil.isBlank(value)) {
			return this;
		}
		if (op != Operators.NOTEQ) {
			map.put(key, value.toString());
		}
		SearchFilter filter = new SearchFilter(key, op, value);
		filters.add(filter);
		return this;
	}

	/**
	 * 添加时间比较
	 * 
	 * @param key
	 * @param op
	 * @param value
	 * @param times 秒数
	 * @return
	 */
	public QueryParam addParam(String key, Operators op, Object value, long times) {
		if (StringUtil.isBlank(value)) {
			return this;
		}
		if (op != Operators.NOTEQ) {
			map.put(key, value.toString());
		}
		SearchFilter filter = new SearchFilter(key, op, value, times);
		filters.add(filter);
		return this;
	}

	public QueryParam addJoin(String name, Class clazz) {
		joins.put(name, clazz);
		return this;
	}

	public QueryParam addOrder(String name) {
		orders.add(new OrderFilter(name));
		return this;
	}

	public QueryParam addOrder(OrderType type, String name) {
		orders.add(new OrderFilter(type, name));
		return this;
	}

	public QueryParam addAddFilter(String lkey, Operators lo, Object lvalue, String rkey, Operators ro, Object rvalue) {
		map.put(lkey + "_Arr", new Object[] { lvalue, rvalue });
		SearchFilter leftFilter = new SearchFilter(lkey, lo, lvalue);
		SearchFilter rightFilter = new SearchFilter(rkey, ro, rvalue);
		SearchFilter expression = new SearchFilter(leftFilter, Operators.AND, rightFilter);
		filters.add(expression);
		return this;
	}

	public QueryParam addAddFilter(String key, Object lvalue, Object rvalue) {
		this.addAddFilter(key, Operators.EQ, lvalue, key, Operators.EQ, rvalue);
		return this;
	}

	public QueryParam addOrFilter(String key, Object... value) {
		SearchFilter orFilter = new SearchFilter(key, Operators.OR, value[0]);
		for (int i = 1; i < value.length; i++) {
			SearchFilter orParam = new SearchFilter(key, Operators.OR, value[i]);
			orFilter = new SearchFilter(orFilter, Operators.OR, orParam);
		}
		filters.add(orFilter);
		return this;
	}

	public QueryParam addOrFilter(SearchFilter... value) {
		SearchFilter orFilter = new SearchFilter(value[0], Operators.OR, value[1]);
		for (int i = 2; i < value.length; i++) {
			orFilter = new SearchFilter(orFilter, Operators.OR, value[i]);
		}
		filters.add(orFilter);
		return this;
	}

	/*
	 * 分页情况
	 */
	public QueryParam addPage(int current) {
		addPage(0, current, Page.ROWS);
		return this;
	}

	public QueryParam addPage(int total, int currentPage, int pernum) {
		if (currentPage < 1)
			currentPage = 1;
		this.page = new Page(total, currentPage, pernum);
		return this;
	}

	public QueryParam addPage(int currentPage, int pernum) {
		addPage(0, currentPage, pernum);
		return this;
	}

	public Page getPage() {
		return page;
	}

	/**
	 * 将所有的参数信息封装成Map 将所有的map中封装时key带对象的字段都将对象过滤掉 例如：user.username转换成username
	 * 
	 * @return
	 */
	public Map toMap() {
		Map<String, Object> maps = new HashMap<String, Object>();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			if (key.contains(".")) {
				String[] strArray = key.split("\\.");
				String newKey = strArray[strArray.length - 1];
				maps.put(newKey, map.get(key).toString());
			} else {
				maps.put(key, map.get(key).toString());
			}
		}
		return maps;
	}

	private StringBuffer parseSql(SearchFilter filter) {
		StringBuffer sql = new StringBuffer(" and ");
		switch (filter.operator) {
			case EQ:
				if (filter.value instanceof String) {
					sql.append(StringUtil.isNull(filter.fieldName)).append("='").append(filter.value + "'");
				} else {
					sql.append(StringUtil.isNull(filter.fieldName)).append("=").append(filter.value);
				}
				break;
			case NOTEQ:
				if (filter.value instanceof String) {
					sql.append(StringUtil.isNull(filter.fieldName)).append("<>'").append(filter.value + "'");
				} else {
					sql.append(StringUtil.isNull(filter.fieldName)).append("<>").append(filter.value);
				}
				break;
			case LIKE:
				if (filter.value instanceof String) {
					sql.append(StringUtil.isNull(filter.fieldName)).append(" like ").append("'%" + filter.value + "%'");
				} else {
					sql.append(StringUtil.isNull(filter.fieldName)).append(" like ").append("%" + filter.value + "%");
				}

				break;
			case GT:
				if (filter.value instanceof String) {
					sql.append(StringUtil.isNull(filter.fieldName)).append(">'").append(filter.value + "'");
				} else {
					sql.append(StringUtil.isNull(filter.fieldName)).append(">").append(filter.value);
				}
				break;
			case LT:
				if (filter.value instanceof String) {
					sql.append(StringUtil.isNull(filter.fieldName)).append("<'").append(filter.value + "'");
				} else {
					sql.append(StringUtil.isNull(filter.fieldName)).append("<").append(filter.value);
				}
				break;
			case GTE:
				if (filter.value instanceof String) {
					sql.append(StringUtil.isNull(filter.fieldName)).append(">='").append(filter.value + "'");
				} else {
					sql.append(StringUtil.isNull(filter.fieldName)).append(">=").append(filter.value);
				}

				break;
			case LTE:
				if (filter.value instanceof String) {
					sql.append(StringUtil.isNull(filter.fieldName)).append("<='").append(filter.value + "'");
				} else {
					sql.append(StringUtil.isNull(filter.fieldName)).append("<=").append(filter.value);
				}
				break;
			case AND:
				SearchFilter lfilter = (SearchFilter) filter.fieldName;
				SearchFilter rfilter = (SearchFilter) filter.value;
				StringBuffer lp = parseSql(lfilter);
				StringBuffer rp = parseSql(rfilter);
				sql.append("((").append(lp).append(") and (").append(rp).append("))");
				break;
			case OR:
				lfilter = (SearchFilter) filter.fieldName;
				rfilter = (SearchFilter) filter.value;
				lp = parseSql(lfilter);
				lp = new StringBuffer(lp.toString().substring(lp.indexOf("and")+3));
				rp = parseSql(rfilter);
				rp = new StringBuffer((rp.toString().substring(rp.indexOf("and")+3)));
				sql.append("((").append(lp).append(") or (").append(rp).append("))");
				break;
			case PROPERTY_EQ:
				sql.append(StringUtil.isNull(filter.fieldName)).append("=").append(filter.value);
				break;
			case PROPERTY_NOTEQ:
				sql.append(StringUtil.isNull(filter.fieldName)).append("<>").append(filter.value);
				break;
			case PROPERTY_GT:
				sql.append(StringUtil.isNull(filter.fieldName)).append(">").append(filter.value);
				break;
			case PROPERTY_LT:
				sql.append(StringUtil.isNull(filter.fieldName)).append("<").append(filter.value);
				break;
		}
		return sql.append(" ");
	}

	private Expression parseExpression(String name, Root root) {
		Expression expression = null;
		String property = StringUtil.isNull(name);
		String[] props = property.split("\\.");
		switch (props.length) {
			case 1:
				expression = root.get(name);
				break;
			case 2:
				Join join = cacheJoin(props[0], root);
				expression = join.get(props[1]);
				break;
			case 3:
				Join join1 = cacheJoin(props[0], root);
				Join join2 = cacheJoin(props[1], join1);
				expression = join2.get(props[2]);
				break;
			default:
				throw new IllegalArgumentException("外连接参数异常，长度为" + props.length);
		}
		return expression;
	}

	private Join cacheJoin(String joinName, Root root) {
		Join join = cacheJoins.get(joinName);
		if (join == null) {
			join = root.join(joinName, JoinType.LEFT);
			cacheJoins.put(joinName, join);
			// System.out.
		}
		return join;
	}

	private Join cacheJoin(String joinName, Join rootJoin) {
		Join join = cacheJoins.get(joinName);
		if (join == null) {
			join = rootJoin.join(joinName, JoinType.LEFT);
			cacheJoins.put(joinName, join);
		}
		return join;
	}

	private Predicate parse(SearchFilter filter, CriteriaBuilder builder, Root root) {
		Predicate p = null;
		Expression expression;
		switch (filter.operator) {
			case EQ:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				p = builder.equal(expression, filter.value);
				break;
			case NOTEQ:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				if (SearchFilter.EMPTY.equals(filter.value)) {
					p = builder.notEqual(expression, "");
				} else if (SearchFilter.NULL.equals(filter.value)){
					p = builder.isNotNull(expression);
				} else if (SearchFilter.EMPTY_AND_NULL.equals(filter.value)){
					Expression np = builder.notEqual(expression, "");
					Expression ep = builder.isNotNull(expression);
					p = builder.and(np, ep);
				} else {
					p = builder.notEqual(expression, filter.value);
				}
				break;
			case LIKE:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				p = builder.like(expression, "%" + filter.value + "%");
				break;
			case GT:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				if (filter.value instanceof Date) {
					p = builder.greaterThanOrEqualTo((Path<Date>) expression, (Date) filter.value);
				} else {
					p = builder.gt(expression, (Number) filter.value);
				}
				break;
			case LT:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				if (filter.value instanceof Date) {
					p = builder.lessThanOrEqualTo((Path<Date>) expression, (Date) filter.value);
				} else {
					p = builder.lt(expression, (Number) filter.value);
				}
				break;
			case GTE:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				if (filter.value instanceof Date) {
					p = builder.greaterThanOrEqualTo((Path<Date>) expression, (Date) filter.value);
				} else {
					p = builder.ge(expression, (Number) filter.value);
				}
				break;
			case LTE:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				if (filter.value instanceof Date) {
					p = builder.lessThanOrEqualTo((Path<Date>) expression, (Date) filter.value);
				} else {
					p = builder.le(expression, (Number) filter.value);
				}
				break;
			case AND:
				SearchFilter lfilter = (SearchFilter) filter.fieldName;
				SearchFilter rfilter = (SearchFilter) filter.value;
				Expression lp = parse(lfilter, builder, root);
				Expression rp = parse(rfilter, builder, root);
				p = builder.and(lp, rp);
				break;
			case OR:
				lfilter = (SearchFilter) filter.fieldName;
				rfilter = (SearchFilter) filter.value;
				lp = parse(lfilter, builder, root);
				rp = parse(rfilter, builder, root);
				p = builder.or(lp, rp);

				break;
			case PROPERTY_EQ:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				Expression valueExpresssion = root.get(StringUtil.isNull(filter.value));
				p = builder.equal(expression, valueExpresssion);
				break;
			case PROPERTY_NOTEQ:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				valueExpresssion = root.get(StringUtil.isNull(filter.value));
				p = builder.notEqual(expression, valueExpresssion);
				break;
			case PROPERTY_GT:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				valueExpresssion = root.get(StringUtil.isNull(filter.value));
				p = builder.greaterThan(expression, valueExpresssion);
				break;
			case PROPERTY_LT:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				valueExpresssion = root.get(StringUtil.isNull(filter.value));
				p = builder.lessThan(expression, valueExpresssion);
				break;
			case DATE_GT_TIMES:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				Expression time1 = builder.function("UNIX_TIMESTAMP", Long.class, expression);
				valueExpresssion = root.get(StringUtil.isNull(filter.value));
				Expression time2 = builder.function("UNIX_TIMESTAMP", Long.class, valueExpresssion);
				p = builder.greaterThan(builder.diff(time1, time2), filter.times);
				break;
			case DATE_LTE_TIMES:
				expression = parseExpression(StringUtil.isNull(filter.fieldName), root);
				time1 = builder.function("UNIX_TIMESTAMP", Long.class, expression);
				valueExpresssion = root.get(StringUtil.isNull(filter.value));
				time2 = builder.function("UNIX_TIMESTAMP", Long.class, valueExpresssion);
				p = builder.lessThanOrEqualTo(builder.diff(time1, time2), filter.times);
				break;
		}
		return p;
	}

	public Predicate[] bySearchFilter(Class entityClass, CriteriaBuilder builder, CriteriaQuery cq, Root root) {
		List<Predicate> predicates = Lists.newArrayList();
		cacheJoins = new HashMap<String, Join>();
		for (SearchFilter filter : filters) {
			predicates.add(parse(filter, builder, root));
		}
		return predicates.toArray(new Predicate[] {});
	}

	public StringBuffer bySearchSqlFilter() {
		StringBuffer sb = new StringBuffer();
		for (SearchFilter filter : filters) {
			sb.append(parseSql(filter));
		}
		return sb;
	}

	public Order[] orderBy(CriteriaBuilder builder, Root root) {
		int size = orders.size();
		if (size < 1)
			return null;
		Order[] orderArray = new Order[size];
		for (int i = 0; i < size; i++) {
			OrderFilter of = orders.get(i);
			Expression exp = parseExpression(of.getName(), root);
			if (of.getOrder() == OrderType.DESC) {
				orderArray[i] = builder.desc(exp);
			} else {
				orderArray[i] = builder.asc(exp);
			}
		}
		return orderArray;
	}

	public StringBuffer byOrderSqlFilter() {
		StringBuffer sb = new StringBuffer();
		for (OrderFilter filter : orders) {
			sb.append(parseOrderSql(filter));
		}
		return sb;
	}

	private StringBuffer parseOrderSql(OrderFilter filter) {
		StringBuffer sql = new StringBuffer(" order by ");
		switch (filter.order) {
			case DESC:
				sql.append(StringUtil.isNull(filter.name) + " desc ");
				break;
			case ASC:
				sql.append(StringUtil.isNull(filter.name) + " asc ");
				break;
		}
		return sql;
	}
	
}
