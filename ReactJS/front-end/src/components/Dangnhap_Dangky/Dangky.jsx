import React, { useState } from "react";
import { Link } from "react-router-dom"; // Thêm import Link
import { useNavigate } from "react-router-dom";
import { format } from "date-fns";

const TrangDangKy = () => { // Đổi tên thành TrangDangKy
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [fullname, setFullname] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [dob, setDob] = useState("");
  const [sex, setSex] = useState("");

  const [error, setError] = useState(""); // Trạng thái lỗi
  const [success, setSuccess] = useState(""); // Trạng thái thành công
  const [fieldErrors, setFieldErrors] = useState({});
  const [isSubmitted, setIsSubmitted] = useState(false);



  const handleSubmit = async (e) => {
    e.preventDefault(); // Ngăn hành vi làm mới trang
    setIsSubmitted(true); // Bật kiểm tra lỗi trống
    console.log("Đăng ký được kích hoạt"); // Xác nhận sự kiện được kích hoạt

    // Format ngày sinh tại thời điểm submit
    const formattedDob = dob ? format(new Date(dob), "yyyy-MM-dd") : "";

    const requestData = {
      username,
      fullname,
      password,
      email,
      phone,
      dob: formattedDob,
      sex,
    };

    // In request JSON ra console
    console.log("Request JSON gửi đi:\n", JSON.stringify(requestData, null, 2));

    try {
      const response = await fetch("http://localhost:8386/restaurant/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(requestData),
      });

      const data = await response.json();
      console.log(data);

      if (!response.ok) {
        // Gán lỗi theo từng trường
        const errorMsg = data.message || "Đăng ký thất bại";
        let fieldErrs = {};

        if (errorMsg.toLowerCase().includes("username")) {
          fieldErrs.username = errorMsg;
        } else if (errorMsg.toLowerCase().includes("fullname")) {
          fieldErrs.fullname = errorMsg;
        } else if (errorMsg.toLowerCase().includes("password")) {
          fieldErrs.password = errorMsg;
        } else if (errorMsg.toLowerCase().includes("email")) {
          fieldErrs.email = errorMsg;
        } else if (errorMsg.toLowerCase().includes("phone")) {
          fieldErrs.phone = errorMsg;
        } else if (errorMsg.toLowerCase().includes("dob")) {
          fieldErrs.dob = errorMsg;
        }


        setFieldErrors(fieldErrs);
        return; // Ngưng xử lý tiếp
      }

      setSuccess("Đăng ký thành công!");
      setFieldErrors({});
      navigate("/dangnhap"); // Điều hướng giao diện về trang chủ khi đăng ký thành công
      setError("");
    } catch (error) {
      console.error("Lỗi kết nối:", error.message);
      setError("Đăng ký thất bại, vui lòng thử lại!");
      setSuccess("");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-lg p-6 bg-white rounded-2xl shadow-xl">
        <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">
          Đăng Ký
        </h2>
        <form onSubmit={handleSubmit}>
          {/************** Tên đăng nhập ***************/}
          <div className="mb-4">
            <label htmlFor="username" className="block text-sm font-medium text-gray-700">
              Tên đăng nhập
              {username.trim() === "" && (
                <span className="text-red-500 text-sm font-bold mt-1"> *</span>
              )}
            </label>
            <input
              type="text"
              id="username"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="Nhập tên đăng nhập"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            {isSubmitted && username.trim() === "" && (
              <p className="text-red-500 text-sm mt-1">Bạn không được bỏ trống tên đăng nhập</p>
            )}
            {fieldErrors.username && username.trim() != "" && (
              <p className="text-red-500 text-sm mt-1">{fieldErrors.username}</p>
            )}
          </div>

          {/************** Họ tên ***************/}
          <div className="mb-4">
            <label htmlFor="fullname" className="block text-sm font-medium text-gray-700">
              Họ tên
              {fullname.trim() === "" && (
                <span className="text-red-500 text-sm font-bold mt-1"> *</span>
              )}
            </label>
            <input
              type="text"
              id="fullname"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="Nhập họ tên đầy đủ"
              value={fullname}
              onChange={(e) => setFullname(e.target.value)}
            />
            {isSubmitted && fullname.trim() === "" && (
              <p className="text-red-500 text-sm mt-1">Bạn không được bỏ trống họ tên</p>
            )}
            {fieldErrors.fullname && fullname.trim() != "" && (
              <p className="text-red-500 text-sm mt-1">{fieldErrors.fullname}</p>
            )}
          </div>

          {/************** Mật khẩu ***************/}
          <div className="mb-4">
            <label htmlFor="password" className="block text-sm font-medium text-gray-700">
              Mật khẩu
              {password.trim() === "" && (
                <span className="text-red-500 text-sm font-bold mt-1"> *</span>
              )}
            </label>
            <input
              type="password"
              id="password"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="Nhập mật khẩu"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {isSubmitted && password.trim() === "" && (
              <p className="text-red-500 text-sm mt-1">Bạn không được bỏ trống mật khẩu</p>
            )}
            {fieldErrors.password && password.trim() != "" && (
              <p className="text-red-500 text-sm mt-1">{fieldErrors.password}</p>
            )}
          </div>

          {/************** Email ***************/}
          <div className="mb-4">
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
              Email
              {email.trim() === "" && (
                <span className="text-red-500 text-sm font-bold mt-1"> *</span>
              )}
            </label>
            <input
              type="email"
              id="email"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="Nhập email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            {isSubmitted && email.trim() === "" && (
              <p className="text-red-500 text-sm mt-1">Bạn không được bỏ trống email</p>
            )}
            {fieldErrors.email && email.trim() != "" && (
              <p className="text-red-500 text-sm mt-1">{fieldErrors.email}</p>
            )}
          </div>

          {/************** Phone ***************/}
          <div className="mb-4">
            <label htmlFor="phone" className="block text-sm font-medium text-gray-700">
              Số điện thoại
              {phone.trim() === "" && (
                <span className="text-red-500 text-sm font-bold mt-1"> *</span>
              )}
            </label>
            <input
              type="text"
              id="phone"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="Nhập số điện thoại"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />
            {isSubmitted && phone.trim() === "" && (
              <p className="text-red-500 text-sm mt-1">Bạn không được bỏ trống số điện thoại</p>
            )}
            {fieldErrors.phone && phone.trim() != "" && (
              <p className="text-red-500 text-sm mt-1">{fieldErrors.phone}</p>
            )}
          </div>

          {/************** Ngày sinh ***************/}
          <div className="mb-4">
            <label htmlFor="dob" className="block text-sm font-medium text-gray-700">
              Ngày sinh
              {dob.trim() === "" && (
                <span className="text-red-500 text-sm font-bold mt-1"> *</span>
              )}
            </label>
            <input
              type="date"
              id="dob"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
              value={dob}
              onChange={(e) => setDob(e.target.value)}
            />
            {isSubmitted && dob.trim() === "" && (
              <p className="text-red-500 text-sm mt-1">Bạn không được bỏ trống ngày sinh</p>
            )}
            {fieldErrors.dob && (
              <p className="text-red-500 text-sm mt-1">{fieldErrors.dob}</p>
            )}
          </div>

          {/************** Giới tính ***************/}
          <div className="mb-4">
            <label htmlFor="sex" className="block text-sm font-medium text-gray-700">
              Giới tính
            </label>
            <div className="mt-2 flex items-center">
              <label className="inline-flex items-center mr-4">
                <input
                  type="radio"
                  name="gender"
                  value="Nam"
                  className="form-radio text-blue-500 h-4 w-4 focus:ring-blue-400"
                  onChange={(e) => setSex(e.target.value)}
                />
                <span className="ml-2 text-gray-700">Nam</span>
              </label>
              <label className="inline-flex items-center mr-4">
                <input
                  type="radio"
                  name="gender"
                  value="Nữ"
                  className="form-radio text-blue-500 h-4 w-4 focus:ring-blue-400"
                  onChange={(e) => setSex(e.target.value)}
                />
                <span className="ml-2 text-gray-700">Nữ</span>
              </label>
              <label className="inline-flex items-center">
                <input
                  type="radio"
                  name="gender"
                  value="Khác"
                  className="form-radio text-blue-500 h-4 w-4 focus:ring-blue-400"
                  onChange={(e) => setSex(e.target.value)}
                />
                <span className="ml-2 text-gray-700">Khác</span>
              </label>
            </div>
          </div>

          {/* Nút submit */}
          <div>
            <button
              type="submit"
              className="w-full bg-blue-500 text-white p-3 rounded-lg font-medium hover:bg-blue-600 transition"
            >
              Đăng Ký
            </button>
          </div>
        </form>
        <div className="mt-6 text-center text-sm text-gray-500">
          Đã có tài khoản?{" "}
          <Link to="/dangnhap" className="text-blue-500 hover:underline">
            Đăng nhập ngay
          </Link>
        </div>
      </div>
    </div>
  );
};

export default TrangDangKy; // Đảm bảo tên component đúng
