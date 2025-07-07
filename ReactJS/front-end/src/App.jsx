import AppRoutes from './routes/AppRoutes';
import Navbar from './components/Navbar-v2.jsx'
import Banner from './components/Trangchu/Banner.jsx'
import Middle from './components/Trangchu/Middle.jsx'
import ContentLeft from './components/Trangchu/Content_left.jsx'
import ContentRight from './components/Trangchu/Content_right.jsx'
import Background from './components/Trangchu/Background.jsx'
import TrangDangNhap from "./components/Dangnhap_Dangky/Dangnhap.jsx";
import TrangDangKy from "./components/Dangnhap_Dangky/Dangky.jsx";
import TrangThongTin from "./components/Trang_Thongtin.jsx";
import { BrowserRouter, Routes, Route, Navigate, Router } from "react-router-dom";


function App() {
  return <AppRoutes />;
}

export default App;
