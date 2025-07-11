import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Lichsudatban from "./Lichsudatban";
const background = "/background_2.png";

const restaurantName = [
   {
      resname: 'Steak Love Độc Lập',
      resID: 'R1'
   },
   {
      resname: 'Steak Love The Villa',
      resID: 'R2'
   },
   {
      resname: 'Steak Love Diamond',
      resID: 'R3'
   },
];

const Bandadat = () => {

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

            const response = await fetch("http://localhost:8386/restaurant/reservation/coming", {
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

   const handleAction = async (res) => {
      try {
         const token = localStorage.getItem("token");

         const response = await fetch(`http://localhost:8386/restaurant/reservation/cancle/${res.id}`, {
            method: "PUT", // hoặc "POST" nếu backend yêu cầu
            headers: {
               "Content-Type": "application/json",
               "Authorization": `Bearer ${token}`,
            },
         });

         const result = await response.json();
         console.log("Gửi request hủy bàn thành công:\n", result);

         if (!response.ok) {
            throw new Error("Huỷ bàn thất bại:\n", response);
         }

         // Cập nhật trạng thái chỉ bàn đó trong danh sách
         setReservationList(prevList =>
            prevList.map(item =>
               item.id === res.id ? { ...item, status: "❌ Khách đã hủy bàn" } : item
            )
         );
         // navigate(0);

         console.log(`Đã huỷ bàn có id: ${res.id}`);
      } catch (error) {
         console.error("Lỗi khi huỷ bàn:", error);
      }
   };


   const InfoItem = ({ label, value }) => {
      const isTableLabel = label === "🍽️ Bàn";
      const isTableValue = value === "Chưa có";

      return (
         <div className="flex flex-col space-y-1 bg-zinc-100 rounded-xl p-2 items-center">
            <p className="font-semibold">{label}</p>
            <div className="bg-zinc-600 w-full text-center rounded-xl px-2 py-1 text-slate-100 min-h-[40px] flex items-center justify-center">
               {typeof value === "string" || typeof value === "number" ? (
                  <p className={`${(isTableLabel && !isTableValue) ? "font-bold text-green-500 text-[20px]" : ""}`}>{value}</p>
               ) : (
                  value
               )}
            </div>
         </div>
      );
   };


   return (
      // <div className="pt-[60px]">
      //    <div>
      //       <div className="absolute w-full h-full top-0 left-0 bg-current">
      //          <img src={background} alt="background right" className="fixed w-full h-[900px] " />
      //       </div>
      //    </div>
      //    <div className="relative z-10 flex flex-col space-y-7 justify-center items-center pt-[50px] pb-[100px] ">
      <div
         // key={index}
         className="w-[80%] p-7 rounded-lg shadow-lg bg-slate-600 bg-opacity-80 z-10"
      >
         <div className="relative z-10 p-6 flex flex-col space-y-6 justify-start items-center h-full w-full">
            <div className="flex space-x-1 mt-[-35px]">
               <div className="w-40 h-[2px] bg-zinc-200 mt-[25px]"></div>
               <h2 className="text-[35px] text-white mb-1 whitespace-nowrap">
                  Bàn đã đặt
               </h2>
               <div className="w-40 h-[2px] bg-zinc-200 mt-[25px]"></div>
            </div>

            {reservationList.map((res, index) => (
               <div key={index} className="flex justify-end w-[100%] my-4">

                  <div className="flex space-x-16 items-center w-[80%] p-2 text-gray-800 text-x rounded-2xl border-blue-100 bg-blue-400 border-b-[10px] border-l-[10px] border-t-[2px] border-r-[2px] justify-center">
                     {/* <div className="flex flex-col space-y-3 items-center w-full"> */}
                     <div className="grid grid-cols-2 gap-9 text-gray-800 w-[95%] text-sm">
                        <div className="space-y-3">

                           <InfoItem label="🏠 Chi nhánh" value={restaurantName.find(item => item.resID === res.restaurant)?.resname || "Không rõ chi nhánh"} />

                           <InfoItem label="⏰ Thời gian nhận bàn" value={new Date(res.reservationTime).toLocaleString()} />

                           <InfoItem label="✅❌⏳Trạng thái đặt bàn" value={res.status} />
                        </div>

                        <div className="space-y-3">
                           <InfoItem label="👥 Số lượng người" value={res.quantityPeople} />

                           <InfoItem label="💬 Tin nhắn ghi chú" value={res.messenger} />

                           <InfoItem label="🍽️ Bàn" value={res.table || "Chưa có"} />
                        </div>
                     </div>
                  </div>
                  <div className="ml-4 mt-5">
                     <button
                        onClick={() => handleAction(res)}
                        className="px-6 py-3 rounded-xl bg-red-500 text-white text-xl hover:bg-red-600"
                     >
                        Huỷ bàn
                     </button>
                  </div>

               </div>

            ))}
         </div>
      </div>
   );
};

export default Bandadat;