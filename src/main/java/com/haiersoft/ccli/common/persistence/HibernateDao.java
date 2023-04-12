package com.haiersoft.ccli.common.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.haiersoft.ccli.common.persistence.PropertyFilter.MatchType;
import com.haiersoft.ccli.common.utils.Reflections;
import com.haiersoft.ccli.common.utils.StringUtils;


/**
 * 封装SpringSide扩展功能的Hibernat DAO泛型基类.
 * 扩展功能包括分页查询,按属性过滤条件列表查询.
 * 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 *
 * @param <T>  DAO操作的对象类型
 * @param <PK> 主键类型
 * @author calvin
 */
public class HibernateDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {

	protected static final String SUCCESS = "success";

	protected static final String FAILED = "failed";

	protected static final String ERROR = "error";

	/**
	 * 用于Dao层子类的构造函数.
	 * 通过子类的泛型定义取得对象类型Class.
	 * eg.
	 * public class UserDao extends HibernateDao<User, Long>{
	 * }
	 */
	public HibernateDao() {
		super();
	}

	/**
	 * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数.
	 * 在构造函数中定义对象类型Class.
	 * eg.
	 * HibernateDao<User, Long> userDao = new HibernateDao<User, Long>(sessionFactory, User.class);
	 */
	public HibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		super(sessionFactory, entityClass);
	}

	//-- 分页查询函数 --//

	/**
	 * 分页获取全部对象.
	 */
	public Page<T> getAll(final Page<T> page) {
		return findPage(page);
	}

	/**
	 * 按HQL分页查询.
	 *
	 * @param page   分页参数. 注意不支持其中的orderBy参数.
	 * @param hql    hql语句.
	 * @param values 数量可变的查询参数,按顺序绑定.
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Page<T> findPage(final Page<T> page, final String hql, final Object... values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * @param page
	 * @param sql
	 * @param paramType
	 * @param params
	 * @return
	 * @throws
	 * @author Connor.M
	 * @Description: SQL的page分页  返回存在实体对象
	 * @date 2016年3月10日 上午9:37:04
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPageSql(final Page<T> page, final String sql, final Map<String, Object> paramType, final Map<String, Object> params) {
		Assert.notNull(page, "page不能为空");

		SQLQuery q = createSQLQuery(sql, params);

		if (page.isAutoCount()) {
			long totalCount = countSqlResult(sql, params);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		if (!CollectionUtils.isEmpty(paramType)) {
			Iterator<?> i = paramType.entrySet().iterator();
			while (i.hasNext()) {//只遍历一次,速度快
				Map.Entry<String, Object> e = ((Entry<String, Object>) i.next());
				if (String.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.STRING);
				} else if (Date.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.TIMESTAMP);
				} else if (Long.class == e.getValue() || long.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.LONG);
				} else if (Double.class == e.getValue() || double.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.DOUBLE);
				} else if (Integer.class == e.getValue() || int.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.INTEGER);
				} else if (Character.class == e.getValue() || char.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.CHARACTER);
				}
			}
			q.setResultTransformer(Transformers.aliasToBean(entityClass));
		}

		List<T> result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * @param sql
	 * @param paramType
	 * @param params
	 * @return
	 * @throws
	 * @author Connor.M
	 * @Description: sql  list 集合查询
	 * @date 2016年4月19日 下午4:03:11
	 */
	@SuppressWarnings("unchecked")
	public List<T> findSql(final String sql, final Map<String, Object> paramType, final Map<String, Object> params) {
		SQLQuery q = createSQLQuery(sql, params);

		if (!CollectionUtils.isEmpty(paramType)) {
			Iterator<?> i = paramType.entrySet().iterator();
			while (i.hasNext()) {//只遍历一次,速度快
				Map.Entry<String, Object> e = ((Entry<String, Object>) i.next());
				if (String.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.STRING);
				} else if (Date.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.TIMESTAMP);
				} else if (Long.class == e.getValue() || long.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.LONG);
				} else if (Double.class == e.getValue() || double.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.DOUBLE);
				} else if (Integer.class == e.getValue() || int.class == e.getValue()) {
					q.addScalar(e.getKey().toString(), StandardBasicTypes.INTEGER);
				}
			}
			q.setResultTransformer(Transformers.aliasToBean(entityClass));
		}
		return q.list();
	}

	/**
	 * @param sql
	 * @param params
	 * @return
	 * @throws
	 * @author Connor.M
	 * @Description: List  查询
	 * @date 2016年4月25日 下午2:00:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findSql(final String sql, final Map<String, Object> params) {
		SQLQuery q = createSQLQuery(sql, params);
		q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findSqlOrSelect(final String sql, final Map<String, Object> params) {
		SQLQuery q = createSQLQuery(sql, params);
		q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q.list();
	}

	/**
	 * @param page
	 * @param sql
	 * @param params
	 * @return
	 * @throws
	 * @author Connor.M
	 * @Description: SQL的page分页  返回MAP对象
	 * @date 2016年3月14日 上午11:42:32
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPageSql(final Page<T> page, final String sql, final Map<String, Object> params) {
		Assert.notNull(page, "page不能为空");

		SQLQuery q = createSQLQuery(sql, params);

		if (page.isAutoCount()) {
			long totalCount = countSqlResult(sql, params);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<T> result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按HQL分页查询.
	 *
	 * @param page   分页参数. 注意不支持其中的orderBy参数.
	 * @param hql    hql语句.
	 * @param values 命名参数,按名称绑定.
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Page<T> findPage(final Page<T> page, final String hql, final Map<String, ?> values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按Criteria分页查询.
	 *
	 * @param page       分页参数.
	 * @param criterions 数量可变的Criterion.
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);

		if (page.isAutoCount()) {
			long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriteria(c, page);

		List result = c.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameterToQuery(final Query q, final Page<T> page) {
		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");
		//hibernate的firstResult的序号从0开始
		q.setFirstResult(page.getFirst() - 1);
		q.setMaxResults(page.getPageSize());
		return q;
	}

	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageParameterToCriteria(final Criteria c, final Page<T> page) {

		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		//hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getFirst() - 1);
		c.setMaxResults(page.getPageSize());

		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');
			String[] orderNullsArray = StringUtils.split(page.getOrderNulls(), ',');

			Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

			for (int i = 0; i < orderByArray.length; i++) {
                String order=  orderArray[i];
                String orderBy=orderByArray[i];

                if (null!=orderNullsArray) {
                    String orderNulls=orderNullsArray[i];
                    dealOrderOfNullsLast(c, order, orderBy, orderNulls);
                } else {

                    dealOrderOfNullsLast(c, order, orderBy, "false");
				}

			}
		}
		return c;
	}


    private void dealOrderOfNullsLast(Criteria c, String order, String orderBy, String orderNulls) {
        if ("true".equals(orderNulls)) {
            if (Page.ASC.equals(order)) {
                c.addOrder(Order.asc(orderBy).nulls(NullPrecedence.LAST));
            } else {
                c.addOrder(Order.desc(orderBy).nulls(NullPrecedence.LAST));
            }
        } else {

            if (Page.ASC.equals(order)) {
                c.addOrder(Order.asc(orderBy));
            } else {
                c.addOrder(Order.desc(orderBy));
            }

        }
    }

    /**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * <p>
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);
		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	/**
	 * @param sql
	 * @param values
	 * @return
	 * @throws
	 * @author Connor.M
	 * @Description: sql的count查询
	 * @date 2016年3月2日 下午4:36:47
	 */
	protected long countSqlResult(final String sql, final Object... values) {
		String countSql = prepareCountSql(sql);
		try {
			BigDecimal count = findUniqueSQL(countSql, values);
			return count.longValue();
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countSql, e);
		}
	}

	protected long countSqlResult(final String sql, final Map<String, ?> values) {
		String countSql = prepareCountSql(sql);
		try {
			BigDecimal count = findUniqueSQL(countSql, values);
			return count.longValue();
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countSql, e);
		}
	}


	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * <p>
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		//select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	private String prepareCountSql(String orgSql) {
		String countSql = "select count(*) from ( " + orgSql + ")";
		return countSql;
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected long countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) Reflections.getFieldValue(impl, "orderEntries");
			Reflections.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			Reflections.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

	//-- 属性过滤条件(PropertyFilter)查询函数 --//

	/**
	 * 按属性查找对象列表,支持多种匹配方式.
	 *
	 * @param matchType 匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
	 */
	public List<T> findBy(final String propertyName, final Object value, final MatchType matchType) {
		Criterion criterion = buildCriterion(propertyName, value, matchType);
		return find(criterion);
	}

	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public List<T> find(List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return find(criterions);
	}

	/**
	 * 按属性过滤条件列表 排序查找对象列表.
	 */
	public List<T> findByOrder(List<PropertyFilter> filters, String orderByProperty, boolean isAsc) {

		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		Criteria c = createCriteria(criterions);
		if (isAsc) {
			c.addOrder(Order.asc(orderByProperty));
		} else {
			c.addOrder(Order.desc(orderByProperty));
			// c.addOrder(Order.desc("updateTime"));
		}
		return c.list();
	}

	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public Page<T> findPage(final Page<T> page, final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPage(page, criterions);
	}

	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	protected Criterion buildCriterion(final String propertyName, final Object propertyValue, final MatchType matchType) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = null;
		//根据MatchType构造criterion
		switch (matchType) {
			case EQ:
				criterion = Restrictions.eq(propertyName, propertyValue);
				break;
			case NEQ:
				criterion = Restrictions.ne(propertyName, propertyValue);
				break;
			case IN:
				criterion = Restrictions.in(propertyName, (Object[]) propertyValue);
				break;
			case NIN:
				criterion = Restrictions.not(Restrictions.in(propertyName, (Object[]) propertyValue));
				break;
			case LIKE:
				criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE).ignoreCase();
				break;
			case SLIKE:
				criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.START);
				break;
			case ELIKE:
				criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.END);
				break;
			case LE:
				criterion = Restrictions.le(propertyName, propertyValue);
				break;
			case LT:
				criterion = Restrictions.lt(propertyName, propertyValue);
				break;
			case GE:
				criterion = Restrictions.ge(propertyName, propertyValue);
				break;
			case GT:
				criterion = Restrictions.gt(propertyName, propertyValue);
				break;
			case NULL:
				criterion = Restrictions.isNull(propertyName);
				break;
			case NNULL:
				criterion = Restrictions.isNotNull(propertyName);
				break;
			case BETWEEN:
				criterion = Restrictions.between(propertyName, ((Object[]) propertyValue)[0], ((Object[]) propertyValue)[1]);
				break;
			case NBETWEEN:
				criterion = Restrictions.not(Restrictions.between(propertyName, ((Object[]) propertyValue)[0], ((Object[]) propertyValue)[1]));
				break;
		}
		return criterion;
	}

	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.
	 */
	protected Criterion[] buildCriterionByPropertyFilter(final List<PropertyFilter> filters) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (PropertyFilter filter : filters) {
			if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
				Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(), filter.getMatchType());
				criterionList.add(criterion);
			} else {//包含多个属性需要比较的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				for (String param : filter.getPropertyNames()) {
					Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
					disjunction.add(criterion);
				}
				criterionList.add(disjunction);
			}
		}
		return criterionList.toArray(new Criterion[criterionList.size()]);
	}

	/**
	 * @return
	 * @throws
	 * @author Connor.M
	 * @Description: 获得SEQUENCE Id
	 * @date 2016年2月27日 上午11:16:58
	 */
	public int getSequenceId(String seqName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT " + seqName + ".nextval AS NUM FROM DUAL ");
		int maxId = (Integer) (this.getSession().createSQLQuery(sb.toString()).addScalar("NUM", StandardBasicTypes.INTEGER)).uniqueResult();
		return maxId;
	}

	/*全文检索
	 *//**
	 * 获取全文Session
	 *//*
    public FullTextSession getFullTextSession(){
		return Search.getFullTextSession(getSession());
	}
	
	*//**
	 * 建立索引
	 *//*
    public void createIndex(){
		try {
			getFullTextSession().createIndexer(entityClass).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	*//**
	 * 全文检索
	 * @param page 分页对象
	 * @param query 关键字查询对象
	 * @param queryFilter 查询过滤对象
	 * @param sort 排序对象
	 * @return 分页对象
	 *//*
    @SuppressWarnings("unchecked")
	public Page<T> search(Page<T> page, BooleanQuery query, BooleanQuery queryFilter, Sort sort){
		
		// 按关键字查询
		FullTextQuery fullTextQuery = getFullTextSession().createFullTextQuery(query, entityClass);
        
		// 过滤无效的内容
		if (queryFilter!=null){
			fullTextQuery.setFilter(new CachingWrapperFilter(new QueryWrapperFilter(queryFilter)));
		}
        
        // 设置排序
		if (sort!=null){
			fullTextQuery.setSort(sort);
		}

		// 定义分页
		page.setTotalCount(fullTextQuery.getResultSize());
		fullTextQuery.setFirstResult(page.getFirst() - 1);
		fullTextQuery.setMaxResults(page.getPageSize()); 

		// 先从持久化上下文中查找对象，如果没有再从二级缓存中查找
        fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY); 
        
		// 返回结果
		page.setResult(fullTextQuery.list());
        
		return page;
	}
	
	*//**
	 * 获取全文查询对象
	 *//*
    public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses){
		BooleanQuery booleanQuery = new BooleanQuery();
		for (BooleanClause booleanClause : booleanClauses){
			booleanQuery.add(booleanClause);
		}
		return booleanQuery;
	}

	*//**
	 * 获取全文查询对象
	 * @param q 查询关键字
	 * @param fields 查询字段
	 * @return 全文查询对象
	 *//*
    public BooleanQuery getFullTextQuery(String q, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		BooleanQuery query = new BooleanQuery();
		try {
			if (StringUtils.isNotBlank(q)){
				for (String field : fields){
					QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);   
					query.add(parser.parse(q), Occur.SHOULD);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query;
	}
	
	*/

	/**
	 * 设置关键字高亮
	 *
	 * @param query     查询对象
	 * @param list      设置高亮的内容列表
	 * @param subLength 截取长度
	 * @param fields    字段名
	 * @return 结果集合
	 *//*
	public List<T> keywordsHighlight(BooleanQuery query, List<T> list, int subLength, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");   
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query)); 
		highlighter.setTextFragmenter(new SimpleFragmenter(subLength)); 
		for(T entity : list){ 
			try {
				for (String field : fields){
					String text = StringUtils.replaceHtml((String)Reflections.invokeGetter(entity, field));
					String description = highlighter.getBestFragment(analyzer,field, text);
					if(description!=null){
						Reflections.invokeSetter(entity, fields[0], description);
						break;
					}
					Reflections.invokeSetter(entity, fields[0], StringUtils.abbr(text, subLength*2));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}*/

	/**
	 * 字符串是否为空
	 *
	 * @return true 字符串为空；false 字符串不为空
	 */
	protected static boolean isNull(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 字符串是否不为空
	 *
	 * @return true 字符串不为空；false 字符串为空
	 */
	protected static boolean isNotNull(String str) {
		return !isNull(str);
	}

	/**
	 * 使用where...in语句时，为参数添加单引号
	 */
	protected static String formatWhereInParams(String param, String split) {

		String[] code = param.split(split);

		String tmp = "'" + code[0].trim() + "'";

		for (int i = 1; i < code.length; i++) {
			tmp += ",'" + code[i].trim() + "'";
		}

		return tmp;
	}
}
