package utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.DBRecordBean;
import javafx.concurrent.Task;

public class DBUtil {
	private static final String DB_VERSION = "v1";

	private static final String SQL_CreateTable_History = "create table if not exists table_history("
			+ "project_new_id varchar(50),project_name varchar(20) , run_time VARCHAR(30) , create_time VARCHAR(30),"
			+ "img_path varchar(150), location_path varchar(150), "
			+ "setting_name  varchar(20), is_save_middle TINYINT(1), net_width VARCHAR(30),net_height VARCHAR(30),"
			+ "is_pre_check TINYINT(1),pre_check_way TINYINT(1), fly_height VARCHAR(30), camera_size VARCHAR(30),"
			+ "gsd VARCHAR(30), result_file_path VARCHAR(150),project_errorDetail VARCHAR(150),start_run_time VARCHAR(30),"
			+ "project_id VARCHAR(30),"
			+ "extra VARCHAR(150) " + ");";
	private static Connection conn;
	public static void initDB() {
		try {
			Task<Boolean> task = new Task<Boolean>()
			{
				@Override
				protected Boolean call() throws Exception
				{
					FileUtil.deleteNullDir(new File(System.getProperty("user.dir") + "\\logs"));
					return null;
				}
			};
			Thread clear = new Thread(task);
			clear.start();
			if (conn != null) {
				if (!conn.isClosed()) {
					System.out.println("db启动前，先断开链接");
					conn.close();
				}
			}
			File folder = new File(System.getProperty("user.dir") + "/datas");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			conn = getConn();
			Statement stat = conn.createStatement();
			stat.executeUpdate(SQL_CreateTable_History);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空数据库数据
	 * 
	 * @return
	 */
	public static int clearAll() {
		String sqlDeleteAll = "delete from table_history;";
		int num = 0;
		try {
			PreparedStatement prepareStatement = conn.prepareStatement(sqlDeleteAll);
			num = prepareStatement.executeUpdate();
			conn.commit();
			System.out.println("数据库清空数据" + num);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * 数据批量删除
	 * 
	 * @param list
	 * @return
	 */
	public static int clear(ArrayList<DBRecordBean> list) {
		int num = 0;
		try {
			String deleteSql = "delete  from table_history where project_new_id =?";
			PreparedStatement stmt = conn.prepareStatement(deleteSql);
			for (DBRecordBean item : list) {
				// 1是占位符的位置，i是取代占位符的值
				stmt.setString(1, item.getProject().getId() +""+ item.getRunTime() + "");
				System.out.println("clear db 的id"+item.getProject().getId() +""+ item.getRunTime() + "");
				// 添加到批量
				stmt.addBatch();
			}
			// 返回批量执行的条数
			int[] result = stmt.executeBatch();
			conn.commit();
			num = result.length;
			System.out.println("批量删除行数：" + result.length);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * 读取数据
	 * 
	 * @return
	 */
	public static ArrayList<DBRecordBean> selectAll() {
		ArrayList<DBRecordBean> datas = new ArrayList<DBRecordBean>();
		try {
			ResultSet rs = conn.createStatement().executeQuery("select * from table_history;");
			while (rs.next()) { // 将查询到的数据打印出来
				DBRecordBean bean = new DBRecordBean();
				bean.resToBean(rs);
				datas.add(bean);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datas;
	}

	/**
	 * 数据插入
	 * 
	 * @param record
	 * @return
	 */
	public static int insert(DBRecordBean record) {
		int executeUpdate = 0;
		String insertSQL = record.toInsertSQL();

		System.out.println("insert sql:" + insertSQL);
		try {
			executeUpdate = conn.createStatement().executeUpdate(insertSQL);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return executeUpdate;
	}

	private static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:datas\\db_" + DB_VERSION);
			conn.setAutoCommit(false);
			System.out.println("DB 链接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 */
	public static void close() {
		System.out.println("db关闭");
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} // 结束数据库的连接
	}
}
