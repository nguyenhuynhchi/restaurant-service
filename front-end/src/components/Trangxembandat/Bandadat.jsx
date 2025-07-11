import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
const background = "/background_2.png";

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






   return (
      <div className="pt-[60px]">
         <div>
            <div className="absolute w-full h-full top-0 left-0 bg-current">
               <img src={background} alt="background right" className="fixed w-full h-[900px] " />
            </div>
         </div>
         <div className="relative z-10 flex flex-col space-y-7 justify-center items-center pt-[50px] pb-[100px] ">
            <div className="w-[80%] p-7 rounded-lg shadow-lg bg-white bg-opacity-80 z-10">
               <div className="absolute inset-0  rounded-lg z-0"></div>
               <div className="relative z-10 p-6 flex flex-col justify-start mt-[-50px] items-center h-full w-full">
                  <div className="flex space-x-1">
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                     <h2 className="text-2xl font-bold text-gray-800 mb-4">
                        Bàn đã đặt
                     </h2>
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                  </div>
                  <div className="flex space-x-16 w-[100%] p-1 text-gray-800 text-x rounded-lg border-green-400 border-2">
                     <div className="flex space-x-5 w-full">
                        <div className="flex flex-col space-y-5 items-center whitespace-nowrap">
                           <p><strong>Chi nhánh:</strong></p>
                           <p><strong>Thời gian nhận bàn:</strong></p>
                           <p><strong>Trạng thái đặt bàn:</strong></p>
                        </div>
                        <div className="flex flex-col space-y-5">
                           <p>restaurant</p>
                           <p>reservationTime</p>              {/* Thử */}
                           <p>status</p>
                        </div>
                     </div>
                     <div className="flex space-x-5 w-full">
                        <div className="flex flex-col space-y-5 items-center whitespace-nowrap">
                           <p><strong>Số lượng người:</strong></p>
                           <p><strong>Tin nhắn ghi chú:</strong></p>
                           <p><strong>Bàn:</strong></p>
                        </div>
                        <div className="flex flex-col space-y-5">
                           <p>quantityPeople</p>
                           <p>messenger</p>
                           <p>table</p>
                        </div>
                     </div>
                  </div>
               </div>
            </div>

            {/* <Lichsudatban /> */}
         </div>
      </div>
   );
};

export default Bandadat;