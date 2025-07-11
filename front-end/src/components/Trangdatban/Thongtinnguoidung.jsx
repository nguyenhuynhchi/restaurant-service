import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

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
                const token = localStorage.getItem("token");

                if (!token) {
                    // setIsLoggedIn(false);
                    // return;
                    navigate("/dangnhap");
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
                // setIsLoggedIn(true);
            } catch (error) {
                console.error("Lỗi khi lấy dữ liệu người dùng:", error);
                // setIsLoggedIn(false);
                navigate("/dangnhap");
                return;
            }
        };

        fetchUserData();
    }, []);

    return (
        <div className="flex space-x-16 ml-[100px] w-[85%] text-white text-xl">
            <div className="flex text-white text-xl space-x-5 w-full">
                <div className="flex flex-col space-y-5 items-center">
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
    );
};

export default Thongtinnguoidung;