package com.ajax;

import java.sql.SQLException;

/**
 * �ѽ�����е�һ����¼ת��Ϊʵ������󣬾����ת�����뵽Dao�����
 * @param <G>
 */
public interface GetEntity<G> {

	public G getEntity(java.sql.ResultSet rs) throws SQLException;
	
}
