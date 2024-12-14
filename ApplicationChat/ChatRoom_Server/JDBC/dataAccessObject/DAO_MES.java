package dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import data.JDBC_Util;
import model.MES_model;

public class DAO_MES implements interface_DAO<MES_model>{
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
					rs=1;
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

	@Override
	public String findByCondition(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MES_model selectByInfo(MES_model t, String condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
