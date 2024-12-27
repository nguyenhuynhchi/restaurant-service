package dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.JDBC_Util;
import model.USERS_model;

public class DAO_USERS implements interface_DAO<USERS_model> {

	public static DAO_USERS getInstance() {
		return new DAO_USERS();
	}

	@Override
	public int insert(USERS_model t) {
		int rs = 0;
		try {
			// Tạo kết nối đến csdl
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {
				// Thực thi lệnh sql
				String sql = "INSERT INTO USERS (userID, userName, fullName, password, createTime)"
						+ "VALUES (?, ?, ?, ?, ?)";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
				pstmt.setString(1, t.getUserID());
				pstmt.setString(2, t.getUserName());
				pstmt.setString(3, t.getFullName());
				pstmt.setString(4, t.getPassword());
				pstmt.setTimestamp(5, t.getCreateTime());

				// thực thi lệnh SQL
				int affectedRows = pstmt.executeUpdate();

				// Kiểm tra xem có dòng nào bị ảnh hưởng không (nghĩa là dữ liệu đã được chèn)
				if (affectedRows > 0) {
					System.out.println("\n- Bạn đã thực thi câu lệnh: " + sql);
					System.out.println("- Có " + affectedRows + " dòng bị thay đổi");
					rs = 1;
				}

				// Ngắt kết nối
				JDBC_Util.closeConnection(conn);
			} else {
				System.out.println("Kết nối không thành công.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error: " + e.getMessage());
		}
		return rs;
	}

	@Override
	public int update(USERS_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(USERS_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<USERS_model> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public USERS_model selectByInfo(USERS_model t, String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<USERS_model> selectByCondition(String condition) {

		return null;
	}

	@Override
	public String findByCondition(String condition) {
		String rs = null;
		String[] parts = condition.split("#");
		String userName = parts[0];
		String password = parts[1];
		// Tạo kết nối đến csdl
		try {
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {

				// Thực thi lệnh sql
				String sql = "SELECT userID, fullName from USERS  WHERE userName = ?  AND  password = ? LIMIT 1";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
				pstmt.setString(1, userName);
				pstmt.setString(2, password);

				// thực thi lệnh SQL
				ResultSet result = pstmt.executeQuery();

				// Kiểm tra xem có có kết quả truy vấn không
				try {
					if (result.next()) {
						System.out.println("\n- Bạn đã thực thi câu lệnh: " + sql);
						String ID = result.getString("userID");
						String fullName = result.getString("fullName");
						rs = ID + "#" + fullName;
						System.out.println("ID của client đó: " + rs);
					} else {
						System.out.println("Không có kết quả truy vấn");
					}

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Lỗi khi truy vấn");
				}

				// Ngắt kết nối
				JDBC_Util.closeConnection(conn);
			} else {
				System.out.println("Kết nối không thành công.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error: " + e.getMessage());
		}
		return rs;
	}

	public String findName(String condition) {
		String rs = null;
		// Tạo kết nối đến csdl
		try {
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {

				// Thực thi lệnh sql
				String sql = "SELECT userName from USERS  WHERE userID = ? LIMIT 1";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
				pstmt.setString(1, condition);

				// thực thi lệnh SQL
				ResultSet result = pstmt.executeQuery();

				// Kiểm tra xem có có kết quả truy vấn không
				try {
					if (result.next()) {
						System.out.println("\n- Bạn đã thực thi câu lệnh: " + sql);
						rs = result.getString("userName");
						System.out.println("Tên của '" + condition + "' đó: " + rs);
					} else {
						System.out.println("Không có kết quả truy vấn");
					}

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Lỗi khi truy vấn");
				}

				// Ngắt kết nối
				JDBC_Util.closeConnection(conn);
			} else {
				System.out.println("Kết nối không thành công.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error: " + e.getMessage());
		}
		return rs;
	}

	public boolean checkUserName(String condition) {
		boolean rs = true;
		// Tạo kết nối đến csdl
		try {
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {

				// Thực thi lệnh sql
				String sql = "SELECT * from USERS  WHERE userName = '" + condition + "' LIMIT 1";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// thực thi lệnh SQL
				ResultSet result = pstmt.executeQuery();

				// Kiểm tra xem có có kết quả truy vấn không
				try {
					System.out.println("\n- Bạn đã thực thi câu lệnh: " + sql);
					if (result.next()) {
						String userName = result.getString("userName");
						System.out.println("Tên người dùng bị trùng: " + userName);
					} else {
						System.out.println("Không có kết quả truy vấn");
						rs = false;
					}

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Lỗi khi truy vấn");
				}

				// Ngắt kết nối
				JDBC_Util.closeConnection(conn);
			} else {
				System.out.println("Kết nối không thành công.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error: " + e.getMessage());
		}
		return rs;
	}

	public String getUsers() {
		String rs = "";
		// Tạo kết nối đến csdl
		try {
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {

				// Thực thi lệnh sql
				String sql = "SELECT * from USERS";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// thực thi lệnh SQL
				ResultSet result = pstmt.executeQuery();

				// Kiểm tra xem có có kết quả truy vấn không
				try {
					System.out.println("\n- Bạn đã thực thi câu lệnh: " + sql);
					while (result.next()) {
						String userID = result.getString("userID");
						String fullName = result.getString("fullName");
//						System.out.println("\t -- " + userID + "|" + fullName);
						rs += userID + "|" + fullName+"#";
					}
					System.out.println("**"+rs);

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Lỗi khi truy vấn");
				}

				// Ngắt kết nối
				JDBC_Util.closeConnection(conn);
			} else {
				System.out.println("Kết nối không thành công.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Error: " + e.getMessage());
		}
		return rs;
	}
}
