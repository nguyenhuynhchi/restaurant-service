package dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.JDBC_Util;
import model.GROUPMEMBERS_model;
import model.GROUPS_model;

public class DAO_GROUPMEMBERS implements interface_DAO<GROUPMEMBERS_model> {

	public static DAO_GROUPMEMBERS getInstance() {
		return new DAO_GROUPMEMBERS();
	}

	@Override
	public int insert(GROUPMEMBERS_model t) {
		int rs = 0;
		try {
			// Tạo kết nối đến csdl
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {
				// Thực thi lệnh sql
				String sql = "INSERT INTO GROUPMEMBERS (groupMemberID, groupID, userID) VALUE (?, ?, ?) ";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
				pstmt.setString(1, t.getGroupMemberID());
				pstmt.setString(2, t.getGroupID());
				pstmt.setString(3, t.getUserID());

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
	public int update(GROUPMEMBERS_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(GROUPMEMBERS_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<GROUPMEMBERS_model> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public GROUPMEMBERS_model selectById(GROUPMEMBERS_model t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GROUPMEMBERS_model> selectByCondition(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findByCondition(String condition) {
		String rs = "";
		// Tạo kết nối đến csdl
		try {
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {

				// Thực thi lệnh sql
				String sql = "SELECT grmb.userID, users.fullName\r\n" + "FROM groupmembers grmb, users\r\n"
						+ "WHERE grmb.userID = users.userID AND grmb.groupID = '" + condition + "'";

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
						rs += userID + "|" + fullName + "@";
					}
					System.out.println("ID và họ tên của client trong group '" + condition + "': " + rs);
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

	@Override
	public GROUPMEMBERS_model selectByInfo(GROUPMEMBERS_model t, String condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
