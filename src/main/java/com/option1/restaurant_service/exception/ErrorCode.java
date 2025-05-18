package com.option1.restaurant_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR), // Lỗi chưa đươc phân loại
  INVALID_KEY(-1, "Uncategorized exception", HttpStatus.BAD_REQUEST), // Lỗi do viết enumKey sai chổ nào đó (vẫn hiển thị "Uncategorized exception")
  USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),  // Người dùng đã tồn tại

  // Invalid
  USERNAME_LENGTH_INVALID(1002, "Username invalid (Tên sử dụng phải có tối thiểu {min} kí tự)", HttpStatus.BAD_REQUEST),  // {min} là giá trị min trong custom annotation invalid
  USERNAME_INVALID(1002, "Username invalid (Tên người dùng chỉ được chứa các kí tự: \"a-z, A-Z, 0-9, ._-\")", HttpStatus.BAD_REQUEST),
  USERPASSWORD_INVALID(1003, "Userpassword invalid (Mật khẩu phải có tối thiểu {min} kí tự)", HttpStatus.BAD_REQUEST),
  FULLNAME_INVALID(1005, "Fullname invalid (Tên chỉ chứa các kí tự alphabetic)", HttpStatus.BAD_REQUEST),
  EMAIL_INVALID(1006, "Email invalid (Email không hợp lệ)", HttpStatus.BAD_REQUEST),
  PHONE_INVALID(1007, "Phone invalid (Số điện thoại phải có đúng 10 kí tự và chỉ được chứa số 0-9)", HttpStatus.BAD_REQUEST),
  INVALID_DOB(1008, "Day of birth invalid (Tuổi của bạn phải ít nhất {min} tuổi)", HttpStatus.BAD_REQUEST),  // Tuoi phair lowns honw 18

  USER_NOT_EXISTED(1004, "User is not existed (Không tìm thấy người dùng này)", HttpStatus.NOT_FOUND),

  UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1007, "You do not have permision", HttpStatus.FORBIDDEN),

  ID_TABLE_EXISTED(1008, "ID cho bàn này đã tồn tại", HttpStatus.BAD_REQUEST),
  ID_TABLE_NOT_EXISTED(1009, "ID bàn này không tồn tại", HttpStatus.NOT_FOUND),

  NAME_RESTAURANT_EXISTED(1010, "Tên cho nhà hàng này đã tồn tại", HttpStatus.BAD_REQUEST),
  ID_RESTAURANT_NOT_EXISTED(1011, "Tên nhà hàng này không tồn tại", HttpStatus.NOT_FOUND)

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
