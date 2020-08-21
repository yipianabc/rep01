package com.ajax;

import java.sql.SQLException;

/**
 * 把结果集中的一条记录转化为实体类对象，具体的转化代码到Dao中完成
 * @param <G>
 */
public interface GetEntity<G> {

	public G getEntity(java.sql.ResultSet rs) throws SQLException;
	
}
