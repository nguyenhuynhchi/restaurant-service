import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getValidToken } from "../authService.js";

const Thongtinnguoidung = () => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);


    const [userData, setUserData] = useState({
        username: "",
        fullname: "",
        email: "",
        phone: "",
        dob: "",
        sex: ""
    });

    useEffect(() => {
        // Gọi API lấy dữ liệu người dùng
        const fetchUserData = async () => {
            try {
                const token = await getValidToken()
                console.log("Token: \n", token);

                if (!token) {
                    // navigate("/dangnhap");
                    setIsLoggedIn(false);
                    console.log("Chưa đăng nhập !!!!")
                    return;
                }

                const response = await fetch("http://localhost:8386/restaurant/users/myInfo", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`,
                    },
                });

                console.log("Api xem thông tin được gọi");

                if (!response.ok) {
                    throw new Error("Lỗi khi gọi API: " + response.status);
                }

                const data = await response.json();
                console.log(data);

                setUserData(data.result);
                setIsLoggedIn(true);
            } catch (error) {
                console.error("Lỗi khi lấy dữ liệu người dùng:", error);
                setIsLoggedIn(false);
                // navigate("/dangnhap");
                return;
            }
        };

        fetchUserData();
    }, []);

    return (
        <div>
            {isLoggedIn ? (
                <div className="flex space-x-16 ml-[100px] w-[85%] text-white text-xl">
                    <div className="flex text-white text-xl space-x-5 w-full">
                        <div className="flex flex-col space-y-5 items-center whitespace-nowrap">
                            <p><strong>Họ tên:</strong></p>
                            <p><strong>Email:</strong></p>
                            <p><strong>Ngày sinh:</strong></p>
                        </div>
                        <div className="flex flex-col space-y-5">
                            <p>{userData.fullname}</p>
                            <p>{userData.email}</p>
                            <p>{userData.dob}</p>
                        </div>
                    </div>
                    <div className="flex text-white text-xl space-x-5 w-full">
                        <div className="flex flex-col space-y-5 items-center">
                            <p><strong>Số điện thoại:</strong></p>
                            <p className="opacity-0"><strong> xxxxxxxxxxxx</strong></p>
                            <p><strong>Giới tính:</strong></p>
                        </div>
                        <div className="flex flex-col space-y-5">
                            <p>{userData.phone}</p>
                            <p className="opacity-0">{userData.email}</p>
                            <p>{userData.sex}</p>
                        </div>
                    </div>
                </div>
            ) : (
                <div className="mt-6 flex flex-col space-y-4 items-center">
                    <p className="text-white font-roboto text-lg">Bạn chưa đăng nhập !</p>
                    <button
                        className="bg-blue-600 text-white w-[80%] px-4 py-2 rounded-lg hover:bg-blue-400 transition duration-300"
                        onClick={() => navigate("/dangnhap")}>
                        Đăng nhập
                    </button>
                </div>
            )}
        </div>
    );
};

export default Thongtinnguoidung;