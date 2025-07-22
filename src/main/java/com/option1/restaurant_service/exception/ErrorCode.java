package com.option1.restaurant_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception (Ngoại lệ không xác định)", HttpStatus.INTERNAL_SERVER_ERROR), // Lỗi chưa đươc phân loại
  INVALID_KEY(-1, "Uncategorized exception (Ngoại lệ không xác định)", HttpStatus.BAD_REQUEST), // Lỗi do viết enumKey sai chổ nào đó (vẫn hiển thị "Uncategorized exception")
  USER_EXISTED(1001, "User existed (Tên người dùng đã tồn tại", HttpStatus.BAD_REQUEST),  // Người dùng đã tồn tại

  // Invalid
  USERNAME_LENGTH_INVALID(1002, "Username invalid (Tên sử dụng phải có tối thiểu {min} kí tự)", HttpStatus.BAD_REQUEST),  // {min} là giá trị min trong custom annotation invalid
  USERNAME_INVALID(1002, "Username invalid (Tên người dùng chỉ được chứa các kí tự: \"a-z, A-Z, 0-9, ._-\")", HttpStatus.BAD_REQUEST),
  USERPASSWORD_INVALID(1003, "Userpassword invalid (Mật khẩu phải có tối thiểu {min} kí tự)", HttpStatus.BAD_REQUEST),
  FULLNAME_INVALID(1004, "Fullname invalid (Tên chỉ chứa các kí tự alphabetic)", HttpStatus.BAD_REQUEST),
  EMAIL_INVALID(1005, "Email invalid (Email không hợp lệ)", HttpStatus.BAD_REQUEST),
  PHONE_INVALID(1006, "Phone invalid (Số điện thoại phải có đúng 10 kí tự và chỉ được chứa số 0-9)", HttpStatus.BAD_REQUEST),
  INVALID_DOB(1007, "Day of birth invalid (Tuổi của bạn phải ít nhất {min} tuổi)", HttpStatus.BAD_REQUEST),  // Tuoi phair lowns honw 18

  USER_NOT_EXISTED(1008, "User is not existed (Không tìm thấy người dùng này)", HttpStatus.NOT_FOUND),

  ROLE_NOT_FOUND(1009, "Role is not existed (Không có role này)", HttpStatus.NOT_FOUND),

  UNAUTHENTICATED(1010, "Unauthenticated (Chưa xác thựcccc)", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1011, "You do not have permision (Không có quyền)", HttpStatus.FORBIDDEN),

  ID_TABLE_EXISTED(1012, "ID cho bàn này đã tồn tại", HttpStatus.BAD_REQUEST),
  ID_TABLE_NOT_EXISTED(1013, "ID bàn này không tồn tại", HttpStatus.NOT_FOUND),

  NAME_RESTAURANT_EXISTED(1014, "Tên cho nhà hàng này đã tồn tại", HttpStatus.BAD_REQUEST),
  ID_RESTAURANT_NOT_EXISTED(1015, "Tên nhà hàng này không tồn tại", HttpStatus.NOT_FOUND),

  ID_RESERVATION_NOT_EXISTED(1016, "Id của đơn hàng này không tồn tại", HttpStatus.NOT_FOUND),
  NOT_HAVE_TABLE(1017, "Không thể nhận bàn khi chưa có bàn", HttpStatus.BAD_REQUEST),

  OUTTIME_CANCLE(1018, "Chỉ còn 1 tiếng nữa đến giờ nhận bàn, nếu có nhu cầu hủy bàn vui lòng liên hệ qua số điện thoại nhà hàng", HttpStatus.BAD_REQUEST),
  OUTTIME_RESERVATION(1019, "Bạn phải đặt bàn trước 5 tiếng, vui lòng liên hệ qua số điện thoại nhà hàng", HttpStatus.BAD_REQUEST)

  ;

  ErrorCode(int code, String message, HttpStatusCode statusCode) {
    this.code = code;
    this.message = message;
    this.statusCode = statusCode;
  }

  private int code;
  private String message;
  private HttpStatusCode statusCode;

}
