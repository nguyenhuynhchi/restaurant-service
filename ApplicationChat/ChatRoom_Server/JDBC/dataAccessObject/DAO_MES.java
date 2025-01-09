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
				String sql = "INSERT INTO tinnhan (userID_Gui, userID_Nhan, groupID_Nhan, NoiDungTinNhan, TG_Gui) VALUES (?, ?, ?, ?, ?)";

				// Tạo đối tượng PreparedStatement
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// Thiết lập các giá trị cho PreparedStatement
				pstmt.setString(1, t.getSenderID());
				pstmt.setString(2, t.getReceiverGroupID());
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
				String sql = "SELECT messageID, userID_Gui, userID_Nhan, u.userName, groupID_Nhan, grs.groupName, NoiDungTinNhan, TG_Gui\r\n"
						+ "FROM tinnhan m\r\n" + "LEFT JOIN thanhvientrongnhom grmb ON m.groupID_Nhan = grmb.groupID\r\n"
						+ "LEFT JOIN users u ON m.userID_Nhan = u.userID\r\n"
						+ "LEFT JOIN nhom grs ON m.groupID_Nhan = grs.groupID\r\n" + "WHERE m.userID_Gui = '"
						+ condition + "'\r\n" + "   OR m.userID_Nhan = '" + condition + "'\r\n"
						+ "   OR m.groupID_Nhan IN (\r\n" + "       SELECT grmb.groupID\r\n"
						+ "       FROM thanhvientrongnhom grmb\r\n       WHERE grmb.userID = '" + condition + "'\r\n"
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
						String userID_Gui = result.getString("userID_Gui");
						String ten_NguoiGui = DAO_USERS.getInstance().findName(userID_Gui);
						String userID_Nhan = result.getString("userID_Nhan");
						String receiverName = DAO_USERS.getInstance().findName(userID_Nhan);
						String groupID_Nhan = result.getString("groupID_Nhan");
						String receiverGroupName = DAO_GROUPS.getInstance().findGroupName(groupID_Nhan);
						String message = result.getString("NoiDungTinNhan");
						String TG_Gui_date = result.getString("TG_Gui").substring(0, 10);
						String TG_Gui_time = result.getString("TG_Gui").substring(11);

						timesRs++;
						rs += "rs" + timesRs + "#" + userID_Gui + "#" + ten_NguoiGui + "#" + userID_Nhan + "#" + receiverName
								+ "#" + groupID_Nhan + "#" + receiverGroupName + "#" + message + "#"
								+ TG_Gui_date + "#" + TG_Gui_time + "$";

						System.out.println("\n*** ***\n\tngười gửi: " + ten_NguoiGui + "(" + userID_Gui
								+ ")\n\tngười nhận: " + receiverName + "(" + userID_Nhan + ")\t nhóm nhận: "
								+ receiverGroupName + "#" + groupID_Nhan);

						System.out.println("\tnội dung tin: " + message);
						System.out.println("\tngày: " + TG_Gui_date + "\tgiờ: " + TG_Gui_time);
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
