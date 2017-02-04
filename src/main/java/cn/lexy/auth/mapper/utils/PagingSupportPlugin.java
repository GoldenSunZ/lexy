package cn.lexy.auth.mapper.utils;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 
 * @author fall_guy
 *
 */
@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class PagingSupportPlugin implements Interceptor {

	private String databaseType;// 数据库类型

	private String pattern;// 分页拦截表达式

	private Integer pageSize;// 默认每页显示记录条数

	private String identifier;// map方式传值 存放PaginInfo的key值,携带分页信息 如果是java
								// bean,属性,该值表示fieldName

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	protected static Logger log = org.slf4j.LoggerFactory
			.getLogger(PagingSupportPlugin.class);

	public Object intercept(Invocation invocation) throws Throwable {

		Object handler = invocation.getTarget();
		if (handler instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) handler;
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper
					.getValueByFieldName(statementHandler, "delegate");
			MappedStatement msmt = (MappedStatement) ReflectHelper
					.getValueByFieldName(delegate, "mappedStatement");

			if (msmt.getId().matches(pattern)) {
				BoundSql boundSql = delegate.getBoundSql();
				Object params = boundSql.getParameterObject();
				if (null == params) {
					return invocation.proceed();
				}
				Connection connection = (Connection) invocation.getArgs()[0];
				String excuteSql = boundSql.getSql();
				String countSql = "select count(0) from (" + excuteSql
						+ ") _auto_count";

				PreparedStatement psmt = connection.prepareStatement(countSql);
//				BoundSql _countSql = new BoundSql(msmt.getConfiguration(),
//						countSql, boundSql.getParameterMappings(), params);
				setParameters(psmt, msmt, boundSql, params);
				ResultSet rs = psmt.executeQuery();
				long count = 0;
				if (rs.next()) {
					count = rs.getInt(1);
				}
				rs.close();
				psmt.close();
				// process pageInfo
				if(0 == count){
					return invocation.proceed();
				}
				PageInfo page = null;
				if (params instanceof PageInfo) {
					page = (PageInfo) params;
					page.setTotal(count);
				} else if (params instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) params;
					if(map.containsKey(identifier)){
						page = (PageInfo) map.get(identifier);
					}else{
						Collection<Object> value = map.values();
						Iterator<Object> iter = value.iterator();
						while(iter.hasNext()){
							Object item = iter.next();
							if(item instanceof PageInfo){
								page = (PageInfo) item;
								break;
							}
						}
					}
					//找不到参数，不做分页处理
					if(null == page){
						return invocation.proceed();
					}
					page.setTotal(count);
				} else {
					Field _page = ReflectionUtils.findField(params.getClass(),
							identifier);
					if (null != _page) {
						page = (PageInfo) ReflectionUtils.getField(_page,
								params);
						page.setTotal(count);
						ReflectionUtils.setField(_page, params, page);
					} else {
						//传入对象中找不到分页参数，不做分页处理
						return invocation.proceed();
					}
				}
				// calc start offset
				if(null == page.getPageSize()){
					if (null != pageSize) {
						page.setPageSize(pageSize);
					}else{
						page.setPageSize(15);
					}
				}
				// calc other paging info
				page.setMaxPage((int) Math.ceil((double)page.getTotal()
						/ (double)page.getPageSize()));
				if (null == page.getCurrent() || page.getCurrent() < 1) {
					page.setCurrent(1);
				}
				if(null == page.getCurrent()){
					page.setCurrent(1);
				}else{
					if (page.getCurrent() > page.getMaxPage()) {
						page.setCurrent(page.getMaxPage());
					}
				}
				int start = (page.getCurrent() - 1) * page.getPageSize() + 1;
				int offset = page.getCurrent() * page.getPageSize();
				if (null == Dialect.valueOf(databaseType)) {
					return invocation.proceed();
				}

				String generateSql = null;
				if (Dialect.mysql.toString().equals(databaseType)) {
					generateSql = mysqlPageingSql(excuteSql, start,
							page.getPageSize());
				} else if (Dialect.sqlserver.toString().equals(databaseType)) {
					generateSql = sqlserverPagingSql(excuteSql, start, offset);
				} else if (Dialect.oracle.toString().equals(databaseType)) {
					generateSql = oraclePagingSql(excuteSql, start, offset);
				}
				if (null != generateSql) {
					ReflectHelper.setValueByFieldName(boundSql, "sql",
							generateSql);
				}
			}
		}
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		// TODO Auto-generated method stub
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		String dialect = properties.getProperty("page.dialect");
		if (!StringUtils.isEmpty(dialect)) {
			databaseType = dialect;
		}
		String _pattern = properties.getProperty("page.pattern");
		if (!StringUtils.isEmpty(_pattern)) {
			pattern = _pattern;
		}
		String size = properties.getProperty("page.size");
		if (!StringUtils.isEmpty(size)) {
			pageSize = Integer.valueOf(size);
		}
		String _identifier = properties.getProperty("page.identifier");
		if (!StringUtils.isEmpty(_identifier)) {
			identifier = _identifier;
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void setParameters(PreparedStatement ps,
			MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters")
				.object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration
					.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null
					: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry
							.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName
							.startsWith(ForEachSqlNode.ITEM_PREFIX)) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(
											propertyName.substring(prop
													.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject
								.getValue(propertyName);
					}
					@SuppressWarnings("rawtypes")
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException(
								"There was no TypeHandler found for parameter "
										+ propertyName + " of statement "
										+ mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value,
							parameterMapping.getJdbcType());
				}
			}
		}
	}

	public enum Dialect {
		mysql, oracle, sqlserver
	}

	private String mysqlPageingSql(String orginSql, Integer start,
			Integer offset) {

		StringBuilder sb = new StringBuilder("select _paging_result.* from (" + orginSql
				+ ") _paging_result limit ");
		if (null != start) {
			sb.append((start-1) + ",");
		}
		sb.append(offset);
		//log.info("MySQL Excuted:"+sb.toString());
		return sb.toString();
	}

	private String oraclePagingSql(String orginSql, Integer start,
			Integer offset) {
		return "";
	}

	private String sqlserverPagingSql(String orginSql, Integer start,
			Integer offset) {
		return "";
	}
}