package dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.JDBC_Util;
import model.GROUPS_model;

public class DAO_GROUPS implements interface_DAO<GROUPS_model> {

	public static DAO_GROUPS getInstance() {
		return new DAO_GROUPS();
	}

	@Override
	public int insert(GROUPS_model t) {
		int rs = 0;
		try {
			// Tạo kết nối đến csdl
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {
				// Thực thi lệnh sql
				String sql = "INSERT INTO GROUPS (groupName, quantityMember, createBy, createTime)"
						+ "VALUES (?, ?, ?, ?)";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
//				pstmt.setString(1, t.getGroupID());
				pstmt.setString(1, t.getGroupName());
				pstmt.setInt(2, t.getQuantityMember());
				pstmt.setString(3, t.getCreateBy());
				pstmt.setTimestamp(4, t.getCreateTime());

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
	public int update(GROUPS_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(GROUPS_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<GROUPS_model> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GROUPS_model> selectByCondition(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findByCondition(String condition) {
		String rs = null;
		String groupName = condition;
		// Tạo kết nối đến csdl
		try {
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {

				// Thực thi lệnh sql
				String sql = "SELECT groupID from GROUPS WHERE groupName = ? LIMIT 1";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
				pstmt.setString(1, groupName);
				
				// thực thi lệnh SQL
				ResultSet result = pstmt.executeQuery();

				// Kiểm tra xem có có kết quả truy vấn không
				try {
					if (result.next()) {
						System.out.println("\n- Bạn đã thực thi câu lệnh: " + sql);
						rs = result.getString("groupID");
						System.out.println("ID của nhóm đó là: " + rs);
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

	@Override
	public GROUPS_model selectByInfo(GROUPS_model t, String condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
