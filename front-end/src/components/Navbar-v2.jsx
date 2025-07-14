import React, { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import { Button } from './Trangchu/Button';
import { useNavigate } from "react-router-dom";
import { getValidToken } from "./authService.js";

const Navbar = () => {
    const [menuOpen, setMenuOpen] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();

    const toggleMenu = () => {
        console.log("Menu toggled!");
        const newState = !menuOpen;
        setMenuOpen(newState);
        handleCheckLoginStatus();
    };

    const handleCheckLoginStatus = async () => {
        try {
            const token = await getValidToken();
            if (!token) {
                console.log("Không có token hợp lệ.");
                setIsLoggedIn(false);
                return;
            }

            const response = await fetch("http://localhost:8386/restaurant/users/myInfo", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                setIsLoggedIn(false);
                return;
            }

            const data = await response.json();

            if (data.code === 1000) {
                setIsLoggedIn(true);

                const userRoles = data.result.roles || [];
                const hasAdminRole = userRoles.some(role => role.name === "ADMIN");
                setIsAdmin(hasAdminRole);
            } else {
                setIsLoggedIn(false);
            }
        } catch (error) {
            console.error("Lỗi kiểm tra đăng nhập:", error);
            setIsLoggedIn(false);
        }
    };

    const handleLogout = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            console.log("Không có token --> trở về trang chủ");
            navigate("/trangchu");
            return;
        }

        try {
            await fetch("http://localhost:8386/restaurant/auth/logout", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({ token }),
            });

            localStorage.removeItem("token");
            console.log("Đã xóa token !!!");
            window.location.reload();
            console.log("Đã đăng xuất trở về trang chủ");
            navigate("/trangchu");
        } catch (err) {
            console.error("Logout API error:", err);
            // tuỳ ý hiển thị toast/thông báo lỗi
        }
    };

    return (
        <nav className="fixed top-0 left-0 w-full z-50 flex items-center space-x-5 justify-between py-3 px-5 bg-white shadow">
            {/* Logo */}
            <div className="flex items-center space-x-2">
                <img src="/logo.png" alt="Steak Love Logo" className="w-[60px] h-[60px]" />
                <span className="text-2xl font-medium font-oswald">Bíp STéch</span>
            </div>

            {/* Navigation Links (Desktop Only) */}
            <ul className="hidden md:flex items-center space-x-9">
                <Link to="/trangchu">
                    <li className="text-black text-[20px] font-normal rounded-xl px-5 py-4 hover:bg-gray-300 cursor-pointer">
                        TRANG CHỦ
                    </li>
                </Link>
                <Link to="/gioithieu">
                    <li className="text-black text-[20px] font-normal rounded-xl px-5 py-4 hover:bg-gray-300 cursor-pointer">
                        GIỚI THIỆU
                    </li>
                </Link>
                <Link to="/lienhe">
                    <li className="text-black text-[20px] font-normal rounded-xl px-5 py-4 hover:bg-gray-300 cursor-pointer">
                        LIÊN HỆ
                    </li>
                </Link>
            </ul>

            <Link to="/datban" className='ml-[90px]'>
                <Button>
                    ĐẶT BÀN
                </Button>
            </Link>

            {/* Mobile Menu Toggle (Dấu ba gạch luôn hiển thị) */}
            <button
                className="menu-toggle block text-3xl"
                onClick={toggleMenu}>
                ☰
            </button>

            {/* Dropdown Menu */}
            {
                menuOpen && (
                    <div className="dropdown-menu absolute top-full right-0 mt-2 bg-white shadow-lg rounded-lg w-64 z-50">
                        <ul>
                            {isLoggedIn ? (
                                <>
                                    <Link to="/thongtin">
                                        <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                            Xem Thông Tin
                                        </li>
                                    </Link>
                                    <Link to="/bandadat">
                                        <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                            Xem Bàn Đã Đặt
                                        </li>
                                    </Link>
                                    {isAdmin && (
                                        <Link to="/duyetban">
                                            <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                                Duyệt bàn cho khách
                                            </li>
                                        </Link>
                                    )}
                                    <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer" onClick={handleLogout}>
                                        Đăng xuất
                                    </li>
                                </>
                            ) : (
                                <Link to="/dangnhap">
                                    <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                        Đăng nhập
                                    </li>
                                </Link>
                            )}

                            {/* Các mục chỉ hiển thị trên màn hình nhỏ */}
                            <div className="md:hidden">
                                <Link to="/trangchu">
                                    <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                        TRANG CHỦ
                                    </li>
                                </Link>
                                <Link to="/gioithieu">
                                    <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                        GIỚI THIỆU
                                    </li>
                                </Link>
                                <Link to="/lienhe">
                                    <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                        LIÊN HỆ
                                    </li>
                                </Link>
                            </div>
                        </ul>
                    </div>
                )
            }
        </nav >
    );
};

export default Navbar;
