import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Thongtinnguoidung from "./Thongtinnguoidung.jsx";

const background = "/background.png";

const restaurantChoice = [
   {
      resname: 'Steak Love Độc Lập',
      address: '106 Nguyễn Thị Minh Khai, Phường Võ Thị Sáu, Quận 3, Thành phố Hồ Chí Minh',
      tel: '0901.56.55.57',
      email: 'r1@gmail.com',
      resID: 'R1'
   },
   {
      resname: 'Steak Love The Villa',
      address: '290 Điện Biên Phủ, Phường Võ Thị Sáu, Quận 3, Thành phố Hồ Chí Minh',
      tel: '0944.56.55.57',
      email: 'r2@gmail.com',
      resID: 'R2'
   },
   {
      resname: 'Steak Love Diamond',
      address: 'Tầng 5 Diamond Plaza, 34 Lê Duẩn, Phường Bến Nghén, Quận 1, Thành phố Hồ Chí Minh',
      tel: '0963.56.55.57',
      email: 'r3@gmail.com',
      resID: 'R3'
   },

];


const Thongtindatban = () => {

   // Kiểm tra khi khách hàng chưa điền đầy đủ thông tin
   const [formError, setFormError] = useState("");

   const [reservationData, setReservationData] = useState({
      reservationTime: "",
      quantityPeople: "",
      restaurant: "",
      messenger: ""
   });

   const [isModalOpen, setIsModalOpen] = useState(false);

   const handleSubmit = async (e) => {
      e.preventDefault(); // Ngăn hành vi làm mới trang
      console.log("Đặt bàn được kích hoạt");

      if (
         !reservationData.restaurant ||
         !reservationData.reservationTime ||
         !reservationData.quantityPeople ||
         !reservationData.messenger
      ) {
         setFormError("Vui lòng nhập đầy đủ thông tin trước khi đặt bàn.");
         return;
      }

      // Nếu không có lỗi
      setFormError(""); // Xóa lỗi cũ

      try {
         const token = localStorage.getItem("token");
         if (!token) {
            // setIsLoggedIn(false);
            // return;
            navigate("/dangnhap");
            console.log("Chưa đăng nhập !!!!")
            return;
         }
         const requestUrl = `http://localhost:8386/restaurant/reservation`;
         const requestBody = JSON.stringify(reservationData);

         // 👉 In ra toàn bộ thông tin request
         console.log("=== Request revervation ===");
         console.log("URL:", requestUrl);
         console.log("Body:\n", reservationData);
         const response = await fetch(requestUrl, {
            method: "POST",
            headers: {
               "Content-Type": "application/json",
               "Authorization": `Bearer ${token}`
            },
            body: requestBody,
         });

         if (!response.ok) {
            throw new Error(`Lỗi khi đặt bàn:\n ${response.status}`);
         }

         const result = await response.json();
         console.log("Gửi request đặt bàn thành công:\n", result);

         setIsModalOpen(true);

      } catch (error) {
         console.error("Lỗi khi gửi request đặt bàn:", error);
         setFormError("Đã xảy ra lỗi khi gửi yêu cầu. Vui lòng thử lại.");
      }
   };

   return (
      <div className="pt-[60px]">
         <div>
            <div className="absolute w-full h-full top-0 left-0 bg-current">
               <img src={background} alt="background right" className="fixed w-full h-[900px] " />
            </div>
         </div>
         <div className="relative z-10 flex justify-center items-start pt-[50px] pb-[100px]">
            <div className="w-[80%] p-8 rounded-lg shadow-lg bg-slate-500 bg-opacity-90 z-10">
               <div className="absolute inset-0  rounded-lg z-0"></div>
               <div className="relative z-10 p-8 flex flex-col justify-start mt-[-50px] items-center h-full w-full">

                  <div className="flex space-x-1">
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                     <h2 className="text-2xl font-bold text-white text-center mb-8">
                        Thông tin của bạn
                     </h2>
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                  </div>
                  <Thongtinnguoidung />

                  <div className="flex space-x-1 mt-5">
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                     <h2 className="text-2xl font-bold text-white text-center mb-4">
                        Nhập thông tin đặt bàn
                     </h2>
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                  </div>

                  <div className="flex space-x-10 w-[80%] text-white text-xl">
                     <form onSubmit={handleSubmit}>
                        {/************** Chọn chi nhánh nhà hàng ***************/}
                        <div className="mb-4">
                           <label htmlFor="restaurant" className="text-white font-semibold rounded-[4px] text-2xl">
                              Chi nhánh
                              <span className="text-red-500 text-x font-bold"> *</span>
                           </label>

                           <div className="flex flex-col border-neutral-700 border-4  rounded-[10px]">
                              {restaurantChoice.map((res, idx) => (
                                 <label className="inline-flex items-center space-x-3 cursor-pointer mr-4"
                                 // key={idx}
                                 >
                                    <input
                                       type="radio"
                                       name="resID"
                                       value={res.resID}
                                       className="form-radio text-blue-500 h-4 w-8 focus:ring-blue-400"
                                       onChange={(e) => setReservationData({ ...reservationData, restaurant: e.target.value })}
                                    />
                                    <div className='flex-col space-y-3 text-[15px] mt-[20px] rounded-xl hover:bg-zinc-200  bg-zinc-200'>
                                       <p className="text-justify text-xl font-medium  text-black leading-relaxed px-2">
                                          {res.resname}
                                       </p>
                                       <div className="rounded-xl bg-zinc-600">
                                          <p className="text-justify leading-relaxed px-2">
                                             {res.address}
                                          </p>
                                          <div className="flex space-x-[50px]">
                                             <p className="text-justify leading-relaxed px-2">
                                                {res.tel}
                                             </p>
                                             <p className="text-justify leading-relaxed px-2">
                                                {res.email}
                                             </p>
                                          </div>
                                       </div>
                                    </div>
                                 </label>
                              ))}
                           </div>
                        </div>

                        {/************** Chọn thời gian ***************/}
                        <div className="mb-4 flex flex-col">
                           <label htmlFor="reservationTime" className="text-white font-semibold rounded-[4px] text-2xl">
                              Chọn thời gian
                              <span className="text-red-500 text-x font-bold"> *</span>
                           </label>
                           <input
                              type="datetime-local"
                              id="reservationTime"
                              className="w-[300px] mt-1 p-3 text-black border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                              value={reservationData.reservationTime}
                              onChange={(e) => setReservationData({ ...reservationData, reservationTime: e.target.value })}
                           />
                        </div>

                        {/************** Chọn số lượng người ***************/}
                        <div className="mb-4 flex flex-col">
                           <label htmlFor="quantityPeple" className="text-white font-semibold rounded-[4px] text-2xl">
                              Số lượng người
                              <span className="text-red-500 text-x font-bold"> *</span>
                           </label>
                           <input
                              type="number"
                              id="quantityPeople"
                              className="w-[300px] mt-1 p-3 text-black border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                              value={reservationData.quantityPeople}
                              onChange={(e) => setReservationData({ ...reservationData, quantityPeople: e.target.value })}
                           />
                        </div>

                        {/************** Nhập tin nhắn ***************/}
                        <div className="mb-4 flex flex-col">
                           <label htmlFor="messenger" className="text-white text-xl">
                              Tin nhắn ghi chú
                              <span className="text-red-500 text-x font-bold"> *</span>
                           </label>
                           <textarea
                              // type="textarea"
                              id="messenger"
                              className="w-[300px] mt-1 p-3 text-black border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                              value={reservationData.messenger}
                              onChange={(e) => setReservationData({ ...reservationData, messenger: e.target.value })}
                           />
                        </div>

                        <div>
                           {formError && (
                              <p className="text-red-600 mt-2 font-medium text-center">{formError}</p>
                           )}
                           <button
                              type="submit"
                              className="w-full bg-blue-500 text-white p-3 rounded-lg font-medium hover:bg-blue-600 transition"
                           >
                              Xác nhận đặt bàn
                           </button>

                        </div>
                     </form>
                  </div>
               </div>
            </div>
         </div>

         {isModalOpen && (
            <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
               <div className="bg-slate-700 rounded-lg shadow-lg w-[90%] sm:w-[600px] p-6 relative">
                  <h3 className="text-3xl font-bold text-white text-center mb-4">Đặt bàn thành công</h3>
                  <Link to="/bandadat">
                     <button
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-red-500 transition duration-300"
                        onClick={() => setIsModalOpen(false)}>
                        Xem
                     </button>
                  </Link>
               </div>
            </div>
         )};
      </div>
   );
};

export default Thongtindatban;