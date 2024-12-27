package dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.JDBC_Util;
import model.MES_model;

public class DAO_MES implements interface_DAO<MES_model> {
	public static DAO_MES getInstance() {
		return new DAO_MES();
	}

	@Override
	public int insert(MES_model t) {
		int rs = 0;
		try {
			// Tạo kết nối đến csdl
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {
				// Thực thi lệnh sql
				String sql = "INSERT INTO MES (senderID, receiverID, receiverGroupID, contentMessage, timeReceive) VALUES (?, ?, ?, ?, ?)";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
				pstmt.setString(1, t.getSenderID());
				pstmt.setString(2, t.getReceiverID());
				pstmt.setString(3, t.getReceiverGroupID());
				pstmt.setString(4, t.getContentMessage());
				pstmt.setTimestamp(5, t.getTimeReceive());

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
	public int update(MES_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(MES_model t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<MES_model> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MES_model> selectByCondition(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInfoMessage(String condition) {
		String rs = "";

		// Tạo kết nối đến csdl
		try {
			Connection conn = JDBC_Util.getConnection();

			if (conn != null) {

				// Thực thi lệnh sql
				String sql = "SELECT messageID, senderID, receiverID, u.userName, receiverGroupID, grs.groupName, contentMessage, timeReceive\r\n"
						+ "FROM mes m\r\n" + "LEFT JOIN groupmembers grmb ON m.receiverGroupID = grmb.groupID\r\n"
						+ "LEFT JOIN users u ON m.receiverID = u.userID\r\n"
						+ "LEFT JOIN groups grs ON m.receiverGroupID = grs.groupID\r\n" + "WHERE m.senderID = '"
						+ condition + "'\r\n" + "   OR m.receiverID = '" + condition + "'\r\n"
						+ "   OR m.receiverGroupID IN (\r\n" + "       SELECT grmb.groupID\r\n"
						+ "       FROM groupmembers grmb\r\n" + "       WHERE grmb.userID = '" + condition + "'\r\n"
						+ "   )\n" + "GROUP BY m.messageID  LIMIT 50;";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
//				pstmt.setString(1, condition);

				// thực thi lệnh SQL
				ResultSet result = pstmt.executeQuery();

				// Kiểm tra xem có có kết quả truy vấn không
				try {
					System.out.println("\n- Bạn đã thực thi câu lệnh: " + sql);
					int timesRs = 0;
					while (result.next()) {
						String senderID = result.getString("senderID");
						String senderName = DAO_USERS.getInstance().findName(senderID);
						String receiverID = result.getString("receiverID");
						String receiverName = DAO_USERS.getInstance().findName(receiverID);
						String receiverGroupID = result.getString("receiverGroupID");
						String receiverGroupName = DAO_GROUPS.getInstance().findGroupName(receiverGroupID);
						String message = result.getString("contentMessage");
						String timeReceive_date = result.getString("timeReceive").substring(0, 10);
						String timeReceive_time = result.getString("timeReceive").substring(11);

						timesRs++;
						rs += "rs" + timesRs + "#" + senderID + "#" + senderName + "#" + receiverID + "#" + receiverName
								+ "#" + receiverGroupID + "#" + receiverGroupName + "#" + message + "#"
								+ timeReceive_date + "#" + timeReceive_time + "$";

						System.out.println("\n*** ***\n\tngười gửi: " + senderName + "(" + senderID
								+ ")\n\tngười nhận: " + receiverName + "(" + receiverID + ")\t nhóm nhận: "
								+ receiverGroupName + "#" + receiverGroupID);

						System.out.println("\tnội dung tin: " + message);
						System.out.println("\tngày: " + timeReceive_date + "\tgiờ: " + timeReceive_time);
					}
					System.out.println("\n->\n" + rs);

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
	public MES_model selectByInfo(MES_model t, String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findByCondition(String condition) {

		return null;
	}

}
