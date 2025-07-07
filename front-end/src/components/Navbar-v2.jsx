import React, { useState } from 'react';
import { Link } from "react-router-dom";
import { Button } from './Trangchu/Button';

const Navbar = () => {
    const [menuOpen, setMenuOpen] = useState(false);

    const toggleMenu = () => {
        console.log("Menu toggled!");
        setMenuOpen(!menuOpen);
    };

    return (
        <nav className="fixed top-0 left-0 w-full z-50 flex items-center space-x-5 justify-between px-2 bg-white shadow">
            {/* Logo */}
            <div className="flex items-center space-x-2">
                <img src="/logo.png" alt="Steak Love Logo" className="w-[60px] h-[60px]" />
                <span className="text-2xl font-medium font-oswald">Bíp STéch</span>
            </div>

            {/* Navigation Links (Desktop Only) */}
            <ul className="hidden md:flex items-center space-x-9">
                <Link to="/trangchu">
                    <li className="text-black text-[18px] font-semibold rounded-xl px-5 py-4 hover:bg-gray-300 cursor-pointer">
                        TRANG CHỦ
                    </li>
                </Link>
                <Link to="/gioithieu">
                    <li className="text-black text-[18px] font-semibold rounded-xl px-5 py-4 hover:bg-gray-300 cursor-pointer">
                        GIỚI THIỆU
                    </li>
                </Link>
                <Link to="/trangchu">
                    <li className="text-black text-[18px] font-semibold rounded-xl px-5 py-4 hover:bg-gray-300 cursor-pointer">
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
                className="menu-toggle block text-2xl"
                onClick={toggleMenu}>
                ☰
            </button>

            {/* Dropdown Menu */}
            {
                menuOpen && (
                    <div className="dropdown-menu absolute top-full right-0 mt-2 bg-white shadow-lg rounded-lg w-64 z-50">
                        <ul>
                            {/* Các mục luôn hiển thị */}
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

                            {/* Các mục chỉ hiển thị trên màn hình nhỏ */}
                            <div className="md:hidden">
                                <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                    TRANG CHỦ
                                </li>
                                <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                    GIỚI THIỆU
                                </li>
                                <li className="dropdown-item px-4 py-2 text-black hover:bg-gray-100 cursor-pointer">
                                    LIÊN HỆ
                                </li>
                            </div>
                        </ul>
                    </div>
                )
            }
        </nav >
    );
};

export default Navbar;
