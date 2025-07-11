import React, { useState } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";

import showPasswordIcon from "../../assets/showPassword.png";
import hidePasswordIcon from "../../assets/hidePassword.png";

const TrangDangNhap = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(""); // Trạng thái lỗi
  const [success, setSuccess] = useState(""); // Trạng thái thành công

  const [showPassword, setShowPassword] = useState(false);  // Ẩn hiện password

  const handleSubmit = async (e) => {
    e.preventDefault(); // Ngăn hành vi làm mới trang
    console.log("Đăng nhập được kích hoạt"); // Xác nhận sự kiện được kích hoạt
    try {
      const response = await fetch("http://localhost:8386/restaurant/auth/token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          username,
          password,
        }),
      });

      if (!response.ok) {
        throw new Error("Lỗi khi kết nối đến server.");
      }

      const data = await response.json();
      console.log(data);
      setSuccess("Đăng nhập thành công!");

      localStorage.setItem("token", data.result.token);
      const token = localStorage.getItem("token");
      console.log("Token trong localStorage: \n" + token);

      navigate("/trangchu"); // Điều hướng giao diện về trang chủ khi đăng nhập thành công
      setError("");
    } catch (error) {
      console.error("Lỗi kết nối:", error.message);
      setError("Đăng nhập thất bại, vui lòng thử lại!");
      setSuccess("");
    }
  };


  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md p-6 bg-white rounded-2xl shadow-xl">
        {/* Logo */}
        <div className="flex justify-center mb-4">
          <img src="./public/logo.png" alt="Logo" className="w-24 h-24" />
        </div>
        {/* Title */}
        <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">Đăng Nhập</h2>
        {/* Error Message */}
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}
        {/* Success Message */}
        {success && <p className="text-green-500 text-center mb-4">{success}</p>}
        {/* Form */}
        <form onSubmit={handleSubmit}>
          {/* Username Input */}
          <div className="mb-4">
            <label htmlFor="username" className="block text-sm font-medium text-gray-700">
              Tên đăng nhập
            </label>
            <input
              type="text"
              id="username"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="Nhập tên đăng nhập"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          {/* Password Input */}
          <div className="mb-6 relative">
            <label htmlFor="password" className="block text-sm font-medium text-gray-700">
              Mật khẩu
            </label>

            <input
              type={showPassword ? "text" : "password"}
              id="password"
              className="w-full mt-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 pr-10"
              placeholder="Nhập mật khẩu"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            {/* Icon con mắt */}
            <img
              src={showPassword ? hidePasswordIcon : showPasswordIcon}
              alt="toggle password"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-[42px] w-6 h-6 cursor-pointer"
            />
          </div>
          {/* Submit Button */}
          <div>
            <button
              type="submit"
              className="w-full bg-blue-500 text-white p-3 rounded-lg font-medium hover:bg-blue-600 transition"
            >
              Đăng Nhập
            </button>
          </div>
        </form>
        {/* Footer */}
        <div className="mt-6 text-center text-sm text-gray-500">
          Bạn chưa có tài khoản?{" "}
          <Link to="/dangky" className="text-blue-500 hover:underline">
            Đăng ký ngay
          </Link>
        </div>
      </div>
    </div>
  );
};

export default TrangDangNhap;
