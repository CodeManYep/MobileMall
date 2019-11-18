package cn.snnu.mm.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * 阿里druid数据库连接池
 * 要实现单例模式，保证全局只有一个数据库连接池
 * 
 * @author zhangwz
 * 2018年4月14日 下午2:11:04
 */
public class DbPoolConnection {
	
	private static DbPoolConnection databasePool=null;  
    private static DruidDataSource dds = null;
    private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
    
	static {
		Properties properties = loadPropertyFile("config/db.properties");
		try {
			dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DbPoolConnection() {}

	/**     
     * 得到一个数据库连接池实例     
     * @return DbPoolConnection 实例     
     */
	public static synchronized DbPoolConnection getInstance() {
		if (null == databasePool) {
			databasePool = new DbPoolConnection();
		}
		return databasePool;
	}

	/**     
     * 得到一个连接     
     * @return DruidPooledConnection 一个连接     
     * @throws SQLException SQL异常     
     */
	public static DruidPooledConnection getConnection() throws SQLException {
		return dds.getConnection();
	}

	public static Properties loadPropertyFile(String fullFile) {

		Properties p = new Properties();
		if (fullFile == "" || fullFile.equals("")) {
			System.out.println("属性文件为空!~");
		} else {
			// 加载属性文件
			InputStream inStream = DbPoolConnection.class.getClassLoader().getResourceAsStream(fullFile);
			try {
				p.load(inStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}
	
	/**
	 * 以下弃置不用，因为会造成连接
	 * 无法关闭
	 * 当用到一下方法时全部重新编写代码
	 * 
	 * 2018年4月21日
	 * @author zhangwz
	 * @throws SQLException
	 */
	//开启事务
	public static void startTransaction() throws SQLException {
		Connection con = getConnection();
		if (con != null) {
			con.setAutoCommit(false);
		}
	}

	//事务回滚
	public static void rollback() throws SQLException {
		Connection con = getConnection();
		if (con != null) {
			con.rollback();
		}
	}

	//提交并且 关闭资源及从ThreadLocall中释放
	public static void commitAndRelease() throws SQLException {
		Connection con = getConnection();
		if (con != null) {
			con.commit(); // 事务提交
			con.close();// 关闭资源
			tl.remove();// 从线程绑定中移除
		}
	}

	//关闭资源方法
	public static void closeConnection() throws SQLException {
		Connection con = getConnection();
		if (con != null) {
			con.close();
			con = null;
		}
	}

	public static void closePreparedStatement(PreparedStatement pst) throws SQLException {
		if (pst != null) {
			pst.close();
			pst = null;
		}
	}
	
	public static void closeStatement(Statement st) throws SQLException {
		if (st != null) {
			st.close();
			st = null;
		}
	}

	public static void closeResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
			rs = null;
		}
	}

}
