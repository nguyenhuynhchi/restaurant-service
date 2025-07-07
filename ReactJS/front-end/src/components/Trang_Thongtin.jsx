import React, { useEffect, useState } from "react";
import Navbar from './Navbar-v2.jsx'

const background = "/background.png";

const TrangTT = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const [userData, setUserData] = useState({
        username: "",
        fullname: "",
        email: "",
        phone: "",
        dob: "",
        sex: ""
    });

    const [userDataUpdate, setUserDataUpdate] = useState({
        username: "",
        fullname: "",
        email: "",
        phone: "",
        dob: "",
        sex: ""
    });

    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const userId = userData.id;
            const requestUrl = `http://localhost:8386/restaurant/users/${userId}`;
            const requestBody = JSON.stringify(userDataUpdate);

            // 👉 In ra toàn bộ thông tin request
            console.log("=== Request Update User ===");
            console.log("URL:", requestUrl);
            console.log("Body:", userDataUpdate);
            const response = await fetch(requestUrl, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: requestBody,
            });

            if (!response.ok) {
                throw new Error(`Lỗi cập nhật: ${response.status}`);
            }

            const result = await response.json();
            console.log("Cập nhật thành công:", result);

            setUserData(userDataUpdate); // cập nhật lại view chính
            setIsModalOpen(false); // đóng modal
        } catch (error) {
            console.error("Lỗi khi cập nhật người dùng:", error);
        }
    };


    useEffect(() => {
        // Gọi API lấy dữ liệu người dùng
        const fetchUserData = async () => {
            try {
                const token = localStorage.getItem("token");

                if (!token) {
                    setIsLoggedIn(false);
                    return;
                }

                const response = await fetch("http://localhost:8386/restaurant/users/myInfo", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`, // <-- đính kèm token tại đây
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
            }
        };

        fetchUserData();
    }, []);

    return (
        <div className="overflow-hidden">
            <div>
                <Navbar />
            </div>
            <div className="pt-[60px]">
                <div className='relative w-screen h-[1000px]'>
                    <div className="absolute w-full h-full top-0 left-0 bg-current opacity-60" />
                    <img src={background} alt="background right" className="w-full h-[900px] " />
                </div>
                <div className="flex h-[500px] mt-[-900px] justify-center items-center">
                    <div className="relative h-[500px] w-[80%] p-8 rounded-lg shadow-lg flex flex-col justify-start items-center mt-[-80px] z-30">
                        {/* Nền mờ nằm dưới */}
                        <div className="absolute inset-0 bg-slate-500 opacity-80 rounded-lg z-0"></div>
                        {/* Nội dung nằm trên */}
                        <div className="relative z-10 p-8 flex flex-col justify-start mt-[-50px] items-center h-full">
                            {/* Tiêu đề căn giữa */}
                            <h2 className="text-3xl font-bold mb-8 text-white text-center">
                                {isLoggedIn ? "Thông tin người dùng" : "Bạn chưa đăng nhập"}
                            </h2>
                            {isLoggedIn ? (
                                <div className="flex text-white ml-[-400px] text-xl space-x-9 w-full">
                                    <div className="flex flex-col items-center space-y-8 whitespace-nowrap">
                                        <p><strong>Tên người dùng:</strong></p>
                                        <p><strong>Họ tên:</strong></p>
                                        <p><strong>Email:</strong></p>
                                        <p><strong>Số điện thoại:</strong></p>
                                        <p><strong>Ngày sinh:</strong></p>
                                        <p><strong>Giới tính:</strong></p>
                                    </div>
                                    <div className="space-y-8">
                                        <p className="mb-4">{userData.username}</p>
                                        <p className="mb-4">{userData.fullname}</p>
                                        <p className="mb-4">{userData.email}</p>
                                        <p className="mb-4">{userData.phone}</p>
                                        <p className="mb-4">{userData.dob}</p>
                                        <p className="mb-4">{userData.sex}</p>
                                    </div>
                                </div>
                            ) : (
                                <div className="mt-6">
                                    <button
                                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-400 transition duration-300"
                                        onClick={() => window.location.href = "/dangnhap"}>
                                        Đăng nhập
                                    </button>
                                </div>
                            )}

                            {isLoggedIn && (
                                <button
                                    className="absolute bottom-[-50px] right-4 mr-[-300px] bg-blue-700 text-orange-200 px-4 py-2 rounded-lg hover:bg-blue-500 transition duration-300"
                                    onClick={() => {
                                        setUserDataUpdate(userData);
                                        setIsModalOpen(true);
                                    }}>
                                    Chỉnh sửa
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {/* Modal chỉnh sửa */}
            {isModalOpen && (
                <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-slate-700 rounded-lg shadow-lg w-[90%] sm:w-[600px] p-6 relative">
                        <h3 className="text-3xl font-bold text-white text-center mb-4">Chỉnh sửa thông tin</h3>
                        <form onSubmit={handleSubmit}>
                            {/* Tên đăng nhập */}
                            <div className="mb-4">
                                <label htmlFor="username" className="block text-x font-medium text-white">
                                    Tên đăng nhập
                                </label>
                                <input
                                    type="text"
                                    id="username"
                                    className="w-full mt-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                                    value={userDataUpdate.username}
                                    onChange={(e) => setUserDataUpdate({ ...userDataUpdate, username: e.target.value })}
                                />
                            </div>
                            {/* Họ tên */}
                            <div className="mb-4">
                                <label htmlFor="fullname" className="block text-x font-medium text-white">
                                    Họ tên
                                </label>
                                <input
                                    type="text"
                                    id="fullname"
                                    className="w-full mt-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                                    value={userDataUpdate.fullname}
                                    onChange={(e) => setUserDataUpdate({ ...userDataUpdate, fullname: e.target.value })}
                                />
                            </div>
                            {/* Email */}
                            <div className="mb-4">
                                <label htmlFor="email" className="block text-x font-medium text-white">
                                    Email
                                </label>
                                <input
                                    type="email"
                                    id="email"
                                    className="w-full mt-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                                    value={userDataUpdate.email}
                                    onChange={(e) => setUserDataUpdate({ ...userDataUpdate, email: e.target.value })}
                                />
                            </div>
                            {/* Số điện thoại */}
                            <div className="mb-4">
                                <label htmlFor="phone" className="block text-x font-medium text-white">
                                    Số điện thoại
                                </label>
                                <input
                                    type="text"
                                    id="phone"
                                    className="w-full mt-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                                    value={userDataUpdate.phone}
                                    onChange={(e) => setUserDataUpdate({ ...userDataUpdate, phone: e.target.value })}
                                />
                            </div>
                            {/* Ngày sinh */}
                            <div className="mb-4">
                                <label htmlFor="dob" className="block text-x font-medium text-white">
                                    Ngày sinh
                                </label>
                                <input
                                    type="date"
                                    id="dob"
                                    className="w-full mt-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                                    value={userDataUpdate.dob}
                                    onChange={(e) => setUserDataUpdate({ ...userDataUpdate, dob: e.target.value })}
                                />
                            </div>
                            {/* Giới tính */}
                            <div className="mb-4">
                                <label className="block text-x font-medium text-white">
                                    Giới tính
                                </label>
                                <div className="flex items-center mt-2">
                                    <label className="inline-flex items-center mr-4">
                                        <input
                                            type="radio"
                                            name="sex"
                                            value="Nam"
                                            checked={userDataUpdate.sex === "Nam"}
                                            onChange={(e) => setUserDataUpdate({ ...userDataUpdate, sex: e.target.value })}
                                            className="form-radio"
                                        />
                                        <span className="ml-2 text-x font-medium text-white">Nam</span>
                                    </label>
                                    <label className="inline-flex items-center mr-4">
                                        <input
                                            type="radio"
                                            name="sex"
                                            value="Nữ"
                                            checked={userDataUpdate.sex === "Nữ"}
                                            onChange={(e) => setUserDataUpdate({ ...userDataUpdate, sex: e.target.value })}
                                            className="form-radio"
                                        />
                                        <span className="ml-2 text-x font-medium text-white">Nữ</span>
                                    </label>
                                    <label className="inline-flex items-center">
                                        <input
                                            type="radio"
                                            name="sex"
                                            value="Khác"
                                            checked={userDataUpdate.sex === "Khác"}
                                            onChange={(e) => setUserDataUpdate({ ...userDataUpdate, sex: e.target.value })}
                                            className="form-radio"
                                        />
                                        <span className="ml-2 text-x font-medium text-white">Khác</span>
                                    </label>
                                </div>
                            </div>
                            <div className="flex justify-end mt-4 space-x-10">

                                {/* Nút đóng */}
                                <button
                                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-red-500 transition duration-300"
                                    onClick={() => setIsModalOpen(false)}>
                                    Đóng
                                </button>
                                {/* Nút lưu */}
                                <button
                                    type="submit"
                                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition duration-300">
                                    Lưu
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default TrangTT;