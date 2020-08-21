package com.ajax;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC������
 * @author Administrator
 *
 */
public class JDBCUtil {
	
	/**
	 * ���ݿ�URL
	 */
	public static final String URL = "jdbc:mysql://localhost:3306/hotel?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "990327yp";
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
	/**
	 * ���췽��˽�л�
	 */
	/**
	 * ��ȡ����
	 * @return Connection
	 */
	public static Connection getConnection(){
		
		try {
			//1.��������
			Class.forName(DRIVER);
			//2.��ȡ����
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * �ر���Դ
	 * @param con
	 * @param st
	 * @param rs
	 */
	public static void close(Connection con ,Statement st ,ResultSet rs){
		try{
			try{
				if(rs != null){
					rs.close();
				}
			}finally{
				try{
					if(st != null){
						st.close();
					}
				}finally{
					if(con != null){
						con.close();
					}
				}
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * �������ݣ��õ��Զ��������
	 */
	public static int addGetAutoId(String sql,Object...args) {
		Connection con = null; 
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			//���ò���
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			//4.ִ��SQL���
			ps.executeUpdate();
			//�õ����������
			rs = ps.getGeneratedKeys();
			if(rs.next())return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(con, ps, null);
		}
		
		return 0;
	}
	/**
	 * ctrl+o ���ٲ��ҷ���
	 * ctrl+l ���ٶ�λ��
	 * ctrl+shift+r ���ٲ����ļ�
	 * �������޸ģ�ɾ������
	 * @param sql
	 * @param args
	 * @return
	 */
	public static int update(String sql,Object ... args){
		//1.��������
		//2.��ȡ����
		Connection con = null; 
		PreparedStatement ps = null;
		//3.����Statement����
		try {
			con = getConnection();
			ps = con.prepareStatement(sql);
			//���ò���
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			//4.ִ��SQL���
			int i = ps.executeUpdate();
			//�����ɾ�ĳɹ���������Ӱ��ĺ���
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(con, ps, null);
		}
		
		return 0;
	}
	/**
	 * ��ѯ�õ��ܼ�¼����
	 */
	public static int queryTotal(String sql,Object...params){
		int total = 0;
		java.sql.Connection conn = null;
		java.sql.PreparedStatement pst = null;
		java.sql.ResultSet rs = null;
		try{
			conn = getConnection();
			pst = conn.prepareStatement(sql);
			if(params != null && params.length != 0){
				for(int i = 0;i<params.length;i++){
					pst.setObject(i+1, params[i]);
				}
			}
			rs = pst.executeQuery();
			if(rs.next()){
				total = rs.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(conn, pst, rs);
		}
		return total;
	}
	
	/**
	 * ��ѯ������һ�����ϣ��������ֶ�����ʵ�������������ֲ�һ��ʱʹ��
	 * @param sql
	 * @param GetEntity �ӽ������ȡ��һ����¼��ת��Ϊʵ�������ӿ�
	 * @param args sql����
	 */
	public static <T> List<T> query(String sql,GetEntity<T> getEntity,Object...args){
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			
			rs = ps.executeQuery();
			List<T> list = new ArrayList<T>();
			while(rs.next()){
				//�ӽ������ȡ��һ����¼ת��Ϊʵ�������
				T obj = getEntity.getEntity(rs);
				list.add(obj);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(connection, ps, rs);
		}
		
		return null;
	}
	/**
	 * ��ѯ������һ�����󣬵������ֶ�����ʵ�������������ֲ�һ��ʱʹ��
	 * @param sql
	 * @param GetEntity �ӽ������ȡ��һ����¼��ת��Ϊʵ�������ӿ�
	 * @param args sql����
	 */
	public static <T> T queryOne(String sql,GetEntity<T> getEntity,Object...args){	
		List<T> list = query(sql,getEntity,args);
		if(list != null && list.size() == 1)return list.get(0);
		return null;
	}
	//��ѯ�Ƿ����
	public static boolean query1(String sql){
		Connection connection =null;
		Statement st=null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			st=connection.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()){
				String name=rs.getString("managerName");
				System.out.println(name);
				return true;
			}else {
				return false;
			}
			//return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(connection, st, rs);
		}
		
		return false;
	}
	/**
	 * ��ѯ������һ�����ϣ�ʵ�����е��������ֱ���ͱ����ֶ���һ��
	 * @param sql
	 * @param clz ���ؽ���ķ���
	 * @param args sql����
	 * @return
	 */
	public static <T> List<T> query(String sql,Class<T> clz ,Object ... args){
		
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			
			ps = connection.prepareStatement(sql);
			
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			
			rs = ps.executeQuery();
			
			List<T> list = new ArrayList<T>();
			ResultSetMetaData rsmd = rs.getMetaData();//�����Ԫ����
			int count = rsmd.getColumnCount();
			while(rs.next()){
				//�����ഴ������
				T obj = clz.newInstance();
				//��ʵ���������Ը�ֵ���磺id,name,password
				for (int i = 0; i < count; i++) {
					//��ȡ���ݿ�����ֶ���
					String columnName = rsmd.getColumnLabel(i+1);//
					//��ȡ�������Ѷ��������
					Field [] fields = clz.getDeclaredFields();
					for (int j = 0; j < fields.length; j++) {
						//��ȡ�����ֶζ���
						Field field = fields[j];
						//��ȡ��������
						String fieldName =  field.getName();
					
						if(fieldName.equalsIgnoreCase(columnName)){
							//��ȡset������ name--������: setName
							String methodName = "set"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1, fieldName.length());
							
							Method method = clz.getMethod(methodName, field.getType());
							
							Object value = rs.getObject(columnName);
							//oralce��Ҫ�ж�value����
							if(value != null){
								method.invoke(obj, value);
							}
							
						}
					}
				}
				
				list.add(obj);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(connection, ps, rs);
		}
		
		return null;
	}
	
	/**
	 * 
	 * ��ѯ��һ������
	 * @param sql
	 * @param clz ���ؽ���ķ���
	 * @param args sql����
	 * @return
	 */
	public static <T> T queryForObject(String sql,Class<T> clz,Object ...args){
		List<T> list = query(sql, clz, args);
		
		if(list != null && list.size()>0){
			return list.get(0);
		}
		
		return null;
		
	}
	/**
	 * ��ѯ�����ؼ��ϣ�ʹ��map��ʾ����
	 * 
	 * map.put("name","ˮ��");
	 * map.put("sex","Ů");
	 * ��MAP��ʾ����key���Ƕ������������value�Ƕ�������ֵ
	 * @param sql
	 * @param args
	 * @return
	 */
	public static List<Map<String, Object>> queryForList(String sql,Object ... args){
		Connection connection = null;
		
		PreparedStatement ps = null;
		
		ResultSet rs = null;
		try{
			connection = getConnection();		
		
			ps = connection.prepareStatement(sql);
		
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			
			rs = ps.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
			//��ȡ�����Ԫ��Ϣ���ֶ����֣��ֶ����ͣ�һ���ж����ֶΣ�
			ResultSetMetaData rsmd =  rs.getMetaData();
			
			//ͳ�ƽ�����е��ֶθ���
			int count = rsmd.getColumnCount();
			
			while(rs.next()){
				//һ��map��ʾһ�����ݻ�һ������
				Map<String, Object> map = new HashMap<String, Object>();
				
				
				for (int i = 0; i < count; i++) {
					String columnName = rsmd.getColumnLabel(i+1);
					Object columnValue = rs.getObject(columnName);
					map.put(columnName, columnValue);
				}
				
				list.add(map);
				
			}
			
			return list;
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(connection, ps, rs);
		}
		
		return null;
	}
	
	/**
	 * ��ѯ������һ��map
	 * @param sql
	 * @param args
	 * @return
	 */
	public static Map<String, Object> queryForMap(String sql,Object ... args){
		
		List<Map<String, Object>> list = queryForList(sql, args);
		
		if(list != null && list.size()>0){
			return list.get(0);
		}
		
		return null;
	}
}
