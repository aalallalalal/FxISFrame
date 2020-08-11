package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.DBRecordBean;

public class DBUtil {
	private static final String DB_VERSION = "v1";

	private static final String SQL_CreateTable_History = "create table if not exists table_history("
			+ "project_new_id varchar(30),project_name varchar(20) , run_time VARCHAR(30) , create_time VARCHAR(30),"
			+ "img_path varchar(150), location_path varchar(150), "
			+ "setting_name  varchar(20), is_save_middle TINYINT(1), net_width VARCHAR(30),net_height VARCHAR(30),"
			+ "is_pre_check TINYINT(1),pre_check_way TINYINT(1), fly_height VARCHAR(30), camera_size VARCHAR(30),"
			+ "gsd VARCHAR(30), result_file_path VARCHAR(150)," + "extra VARCHAR(150) " + ");";

	private static Connection conn;

	public static void initDB() {
		try {
			conn = getConn();
			Statement stat = conn.createStatement();
			stat.executeUpdate(SQL_CreateTable_History);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
			conn = DriverManager.getConnection("jdbc:sqlite:Datas\\db_" + DB_VERSION);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

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
