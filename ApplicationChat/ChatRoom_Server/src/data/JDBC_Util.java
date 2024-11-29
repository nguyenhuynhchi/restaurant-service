//package data;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import com.mysql.cj.jdbc.Driver;
//import com.mysql.cj.jdbc.exceptions.CommunicationsException;
//
//public class JDBC_Util {
//
//	public static int flat = 0;
//
//	public static Connection getConnection() {
//		Connection c = null;
//
//		try {
//			// Này là đăng ký driver với DriverManager
//			DriverManager.registerDriver(new Driver());
//
//			// thông số
//			String url = "jdbc:mysql://localhost:3306/     ten dtb "|; // chua có tên
//			String username = "root";
//			String password = "";
//
//			// tạo kết nối
//			c = DriverManager.getConnection(url, username, password);
//		} catch (CommunicationsException e) {
//			// Xử lý khi xảy ra lỗi liên kết
//			System.out.println("Lỗi kết nối đến MySQL: " + e.getMessage());
//			flat = 1;
//		} catch (SQLException e) {
//			// Xử lý các lỗi SQL khác
//			System.out.println("Lỗi SQL: " + e.getMessage());
//			flat = 1;
//		}
//
//		return c;
//	}
//
//	public static void closeConnection(Connection c) {
//		try {
//			if (c != null) {
//				c.close();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// In tên + version
//	public static void printInfo(Connection c) {
//		try {
//			if (c != null) {
//				java.sql.DatabaseMetaData mtdt = c.getMetaData();
//				System.out.println(mtdt.getDatabaseProductName());
//				System.out.println(mtdt.getDatabaseProductVersion());
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//}
