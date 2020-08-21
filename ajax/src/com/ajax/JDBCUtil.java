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
 * JDBC工具类
 * @author Administrator
 *
 */
public class JDBCUtil {
	
	/**
	 * 数据库URL
	 */
	public static final String URL = "jdbc:mysql://localhost:3306/hotel?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "990327yp";
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
	/**
	 * 构造方法私有化
	 */
	/**
	 * 获取链接
	 * @return Connection
	 */
	public static Connection getConnection(){
		
		try {
			//1.加载驱动
			Class.forName(DRIVER);
			//2.获取连接
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 关闭资源
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
	 * 插入数据，得到自动增长编号
	 */
	public static int addGetAutoId(String sql,Object...args) {
		Connection con = null; 
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			//设置参数
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			//4.执行SQL语句
			ps.executeUpdate();
			//得到结果集对象
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
	 * ctrl+o 快速查找方法
	 * ctrl+l 快速定位行
	 * ctrl+shift+r 快速查找文件
	 * 新增，修改，删除方法
	 * @param sql
	 * @param args
	 * @return
	 */
	public static int update(String sql,Object ... args){
		//1.加载驱动
		//2.获取链接
		Connection con = null; 
		PreparedStatement ps = null;
		//3.创建Statement对象
		try {
			con = getConnection();
			ps = con.prepareStatement(sql);
			//设置参数
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i+1, args[i]);
				}
			}
			//4.执行SQL语句
			int i = ps.executeUpdate();
			//如果增删改成功，返回受影响的函数
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(con, ps, null);
		}
		
		return 0;
	}
	/**
	 * 查询得到总记录条数
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
	 * 查询，返回一个集合，当表中字段名与实体类中属性名字不一样时使用
	 * @param sql
	 * @param GetEntity 从结果集中取出一条记录，转化为实体类对象接口
	 * @param args sql参数
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
				//从结果集中取出一条记录转化为实体类对象
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
	 * 查询，返回一个对象，当表中字段名与实体类中属性名字不一样时使用
	 * @param sql
	 * @param GetEntity 从结果集中取出一条记录，转化为实体类对象接口
	 * @param args sql参数
	 */
	public static <T> T queryOne(String sql,GetEntity<T> getEntity,Object...args){	
		List<T> list = query(sql,getEntity,args);
		if(list != null && list.size() == 1)return list.get(0);
		return null;
	}
	//查询是否存在
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
	 * 查询，返回一个集合，实体类中的属性名字必须和表中字段名一样
	 * @param sql
	 * @param clz 返回结果的泛型
	 * @param args sql参数
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
			ResultSetMetaData rsmd = rs.getMetaData();//结果集元数据
			int count = rsmd.getColumnCount();
			while(rs.next()){
				//根据类创建对象
				T obj = clz.newInstance();
				//对实体类中属性赋值，如：id,name,password
				for (int i = 0; i < count; i++) {
					//获取数据库表中字段名
					String columnName = rsmd.getColumnLabel(i+1);//
					//获取类所有已定义的属性
					Field [] fields = clz.getDeclaredFields();
					for (int j = 0; j < fields.length; j++) {
						//获取单个字段对象
						Field field = fields[j];
						//获取属性名字
						String fieldName =  field.getName();
					
						if(fieldName.equalsIgnoreCase(columnName)){
							//获取set方法名 name--方法名: setName
							String methodName = "set"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1, fieldName.length());
							
							Method method = clz.getMethod(methodName, field.getType());
							
							Object value = rs.getObject(columnName);
							//oralce需要判断value类型
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
	 * 查询，一个对象
	 * @param sql
	 * @param clz 返回结果的泛型
	 * @param args sql参数
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
	 * 查询，返回集合，使用map表示对象
	 * 
	 * map.put("name","水淋");
	 * map.put("sex","女");
	 * 用MAP表示对象，key就是对象的属性名，value是对象属性值
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
			
			//获取结果集元信息（字段名字，字段类型，一共有多少字段）
			ResultSetMetaData rsmd =  rs.getMetaData();
			
			//统计结果集中的字段个数
			int count = rsmd.getColumnCount();
			
			while(rs.next()){
				//一个map表示一行数据或一个对象
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
	 * 查询，返回一个map
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
