import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Chinhanh = () => {
   const [message, setMessage] = useState("");
   const navigate = useNavigate();

   const [userData, setUserData] = useState({
      username: "",
      fullname: "",
      email: "",
      phone: "",
      dob: "",
      sex: ""
   });

   const handleSubmit = async (e) => {
      e.preventDefault();
      try {
         const token = localStorage.getItem("token");

         if (!token) {
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

         if (!response.ok) {
            throw new Error("Lỗi khi gọi API: " + response.status);
         }

         const data = await response.json();
         console.log("Response: \n", data);
         setUserData(data.result);
         const email = userData.email;
         console.log(`Email người dùng: ${email}`);
      } catch (error) {
         console.error("Lỗi khi lấy dữ liệu người dùng:", error);
         navigate("/dangnhap");
         return;
      }
   }

   return (
      <div className="z-10 p-4 py-10 flex flex-col space-y-6 justify-start items-center h-full w-full">
         <h2 className="text-4xl font-roboto leading-tight-custom font-light300 whitespace-nowrap">Gửi mọi thắc mắc và ý kiến đến chúng tôi</h2>

         <textarea
            className="w-[70%] h-48 border-2 border-black p-3 focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Nội dung"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
         />

         <button
            onClick={handleSubmit}
            className="px-6 py-3 text-white font-medium bg-red-600 transition"
         >
            Gửi đến chúng tôi
         </button>
      </div>
   );
};

export default Chinhanh;