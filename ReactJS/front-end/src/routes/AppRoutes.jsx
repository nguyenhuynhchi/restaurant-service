import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import TrangDangKy from "../components/Dangnhap_Dangky/Dangky.jsx";
import TrangDangNhap from "../components/Dangnhap_Dangky/Dangnhap.jsx";
import TrangThongTin from "../components/Trang_Thongtin.jsx";
import TrangChu from "../components/Trangchu/Trangchu.jsx";
import TrangGioiThieu from "../components/Tranggioithieu/Tranggioithieu.jsx";
import TrangDatBan from "../components/Trangdatban/Trangdatban.jsx";
import Trangxembandat from "../components/Trangxembandat/Trangxemban.jsx";


const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        {/* Điều hướng tất cả các URL khác về trang đăng nhập */}
        <Route path="/" element={<Navigate to="/trangchu" />} />

        {/* Route cho trang chủ */}
        <Route path="/trangchu" element={<TrangChu />} />

        {/* Route cho trang đăng ký */}
        <Route path="/dangky" element={<TrangDangKy />} />

        {/* Route cho trang đăng nhập */}
        <Route path="/dangnhap" element={<TrangDangNhap />} />

        {/* Route cho trang thông tin */}
        <Route path="/thongtin" element={<TrangThongTin />} />

        {/* Route cho trang giới thiệu */}
        <Route path="/gioithieu" element={<TrangGioiThieu />} />

        {/* Route cho trang đặt bàn */}
        <Route path="/datban" element={<TrangDatBan />} />

        {/* Route cho trang xem bàn đã đặt */}
        <Route path="/bandadat" element={<Trangxembandat />} />

        <Route path="*" element={<h1>404 - Không tìm thấy trang</h1>} />


      </Routes>
    </Router>
  );
};

export default AppRoutes;
