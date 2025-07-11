import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const restaurantName = [
   {
      resname: 'Bíp téch Điện Biên Phủ',
      resID: 'R1'
   },
   {
      resname: 'Bíp téch Điện Biên Phủ',
      resID: 'R2'
   },
];



const Lichsudatban = () => {

   const navigate = useNavigate();
   const [reservationList, setReservationList] = useState([]);

   useEffect(() => {
      // Gọi API lấy dữ liệu người dùng
      const fetchReservationData = async () => {
         try {
            const token = localStorage.getItem("token");

            if (!token) {
               // setIsLoggedIn(false);
               // return;
               navigate("/dangnhap");
               console.log("Chưa đăng nhập !!!!")
               return;
            }

            const response = await fetch("http://localhost:8386/restaurant/reservation/history", {
               method: "GET",
               headers: {
                  "Content-Type": "application/json",
                  "Authorization": `Bearer ${token}`, // <-- đính kèm token tại đây
               },
            });

            console.log("Api xem thông tin đặt bàn sắp tới được gọi");

            if (!response.ok) {
               throw new Error("Lỗi khi gọi API: " + response.status);
            }

            const data = await response.json();
            console.log(data);

            setReservationList(data.result);


         } catch (error) {
            console.error("Lỗi khi lấy dữ liệu:", error);
            // setIsLoggedIn(false);
            navigate("/dangnhap");
            return;
         }
      };

      fetchReservationData();
   }, []);

   const InfoItem = ({ label, value }) => (
      <div className="flex flex-col space-y-1 bg-zinc-200 rounded-xl p-2 w-[100%] items-center">
         <p className="whitespace-nowrap"><strong>{label}</strong></p>
         <div className="bg-zinc-600 w-full text-center text-sm rounded-xl p-2 text-slate-100">
            {typeof value === "string" || typeof value === "number" ? <p>{value}</p> : value}
         </div>
      </div>
   );

   return (
      <div
         // key={index}
         className="w-[80%] p-7 rounded-lg shadow-lg bg-slate-600 bg-opacity-80 z-10"
      >
         <div className="relative z-10 p-6 flex flex-col space-y-6 justify-start items-center h-full w-full">
            <div className="flex space-x-1 mt-[-35px]">
               <div className="w-40 h-[2px] bg-zinc-200 mt-[25px]"></div>
               <h2 className="text-[35px] text-white mb-1">
                  Lịch sử đặt bàn
               </h2>
               <div className="w-40 h-[2px] bg-zinc-200 mt-[25px]"></div>
            </div>

            {reservationList.map((res, index) => (
               <div className="flex space-x-16 items-center w-[80%] p-2 text-gray-800 text-x rounded-2xl border-green-400 bg-emerald-700 border-b-[10px] border-l-[10px] border-t-[2px] border-r-[2px] justify-center">
                  {/* <div className="flex flex-col space-y-3 items-center w-full"> */}
                  <div className="grid grid-cols-2 gap-9 text-gray-800 w-[90%] text-sm">
                     <div className="space-y-3">

                        <InfoItem label="Chi nhánh" value={restaurantName.find(item => item.resID === res.restaurant)?.resname || "Không rõ chi nhánh"} />

                        <InfoItem label="Thời gian nhận bàn" value={new Date(res.reservationTime).toLocaleString()} />

                        <InfoItem label="Trạng thái đặt bàn" value={res.status} />
                     </div>

                     <div className="space-y-3">
                        <InfoItem label="Số lượng người" value={res.quantityPeople} />

                        <InfoItem label="Tin nhắn ghi chú" value={res.messenger} />

                        <InfoItem label="Bàn" value={res.table || "Chưa có"} />
                     </div>
                  </div>
               </div>
            ))}
         </div>
      </div>
   );
};

export default Lichsudatban;