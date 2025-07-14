import React, { useEffect, useState } from "react";
import Locban from "./Locban";
import { getValidToken } from "../authService.js";

const restaurantName = [
   {
      resname: 'Steak Love Độc Lập',
      resID: 'R1',
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

const Banchuaxacnhan = () => {
   const [reservations, setReservations] = useState([]);

   const [showTableModal, setShowTableModal] = useState(false);
   const [selectedBooking, setSelectedBooking] = useState(null); // booking được chọn để gán bàn
   const [availableTables, setAvailableTables] = useState([]);
   const [selectedTable, setSelectedTable] = useState(null);

   const [isModalOpen, setIsModalOpen] = useState(false);
   const [modalMessage, setModalMessage] = useState("");

   useEffect(() => {
      const fetchUnconfirmedReservations = async () => {
         try {
            const token = await getValidToken();
            const response = await fetch("http://localhost:8386/restaurant/reservation/unconfirmed", {
               method: "GET",
               headers: {
                  "Content-Type": "application/json",
                  "Authorization": `Bearer ${token}`,
               },
            });

            if (!response.ok) throw new Error("Không thể lấy danh sách bàn chưa xác nhận");

            const data = await response.json();
            setReservations(data.result); // tùy theo format API trả về
         } catch (error) {
            console.error("Lỗi:", error);
         }
      };

      fetchUnconfirmedReservations();
   }, []);

   const handleOpenTableModal = async (booking) => {
      setSelectedBooking(booking);
      setShowTableModal(true);
      console.log("Hiển thị bàn để chọn");

      try {
         const token = localStorage.getItem("token");

         const res = await fetch("http://localhost:8386/restaurant/table", {
            method: "GET",
            headers: {
               "Content-Type": "application/json",
               "Authorization": `Bearer ${token}`,
            },
         });

         const data = await res.json();

         // 🟢 Lọc bàn theo chi nhánh (id bắt đầu bằng booking.restaurant, ví dụ "R1")
         const filtered = data.result.filter(table =>
            table.id.startsWith(booking.restaurant)   // booking.restaurant = "R1" | "R2" | "R3"
         );

         setAvailableTables(filtered);
      } catch (error) {
         console.error("Lỗi khi lấy danh sách bàn:", error);
      }
   };

   const handleConfirm = async (booking) => {
      if (!booking.table) {
         setIsModalOpen(true);
         setModalMessage("Hãy chọn bàn trước khi xác nhận!");
         return;
      }

      try {
         const token = localStorage.getItem("token");

         const requestUrl = `http://localhost:8386/restaurant/reservation/confirm/${booking.id}`;
         const requestBody = JSON.stringify({ table: booking.table });

         const res = await fetch(requestUrl,
            {
               method: "PUT",
               headers: {
                  "Content-Type": "application/json",
                  "Authorization": `Bearer ${token}`,
               },
               body: requestBody,
            }
         );

         console.log("URL:", requestUrl);
         console.log("Request body: \n", requestBody);
         console.log("Response: ", res);

         if (!res.ok) throw new Error("Xác nhận thất bại");

         // tuỳ ý: xoá khỏi danh sách chờ hoặc đổi status
         setReservations(prev =>
            prev.filter(r => r.id !== booking.id)
         );

         setIsModalOpen(true); // Bấm xác nhận chọn bàn thành công thì thông báo "đã xác nhận"
         setModalMessage("Đã xác nhận bàn !");
      } catch (err) {
         console.error(err);
         alert("Có lỗi khi xác nhận.");
      }
   };


   const handlePickTable = () => {
      if (!selectedTable) return;

      // 1. Cập‑nhật tạm lên UI
      setReservations(prev =>
         prev.map(r =>
            r.id === selectedBooking.id ? { ...r, table: selectedTable.id } : r
         )
      );

      // 2. Đóng modal & reset
      setShowTableModal(false);
      setSelectedTable(null);
      setSelectedBooking(null);
   };

   const InfoItem = ({ label, value }) => {
      const isTableLabel = label === "Bàn";
      const isTableValue = value === "Chưa có";

      return (
         <div className="flex flex-col space-y-1 bg-zinc-300 rounded-xl p-2 items-center">
            <p className="font-semibold">{label}</p>
            <div className="bg-zinc-600 w-full text-center rounded-xl px-2 py-1 text-slate-100 min-h-[40px] flex items-center justify-center">
               {typeof value === "string" || typeof value === "number" ? <p>{value}</p> : value}
            </div>
         </div>
      );
   };

   return (
      <div className="pt-[90px] px-8 h-full w-full">
         <div className="flex flex-col md:flex-row gap-6 max-w-7xl mx-auto">
            {/* Bên trái: bàn chưa xác nhận */}
            <div className="w-full md:w-1/2 space-y-6">
               <h2 className="text-3xl font-bold text-center mb-10 text-slate-800">
                  Danh sách đặt bàn chưa xác nhận
               </h2>
               {reservations.length === 0 ? (
                  <p className="text-center text-gray-600">Không có bàn nào đang chờ xác nhận.</p>
               ) : (
                  reservations.map((res, idx) => (
                     <div key={idx} className="relative bg-gray-500 shadow-md rounded-lg p-6 border border-gray-300">
                        <div className="absolute top-1 left-1 text-[13px] font-semibold bg-gray-400 px-2 py-1 rounded">
                           ID: {res.id.slice(-4)}
                        </div>
                        <div className="grid grid-cols-2 gap-y-4 gap-x-8 text-gray-800">
                           <InfoItem label="⏰ Thời gian nhận bàn:" value={new Date(res.reservationTime).toLocaleString("vi-VN")} />
                           <InfoItem label="🏠 Chi nhánh:" value={restaurantName.find(item => item.resID === res.restaurant)?.resname || "Không rõ chi nhánh"} />
                           <InfoItem label="👥 Số lượng người:" value={res.quantityPeople} />
                           <InfoItem label="💬 Tin nhắn ghi chú:" value={res.messenger || "Không có"} />
                           <InfoItem label="🍽️ Bàn" value={res.table || "Chưa có"} />
                           <div className="space-x-4">
                              {<button
                                 onClick={() => handleOpenTableModal(res)}
                                 className="text-red-600 py-1 px-2 rounded-lg bg-zinc-300 cursor-pointer hover:bg-blue-800"
                              >
                                 Chọn bàn
                              </button>}
                              {<button
                                 onClick={() => handleConfirm(res)}
                                 className="text-red-600 py-1 px-2 rounded-lg bg-zinc-300 cursor-pointer hover:bg-blue-800"
                              >
                                 xác nhận
                              </button>}
                           </div>
                        </div>
                     </div>
                  ))
               )}
            </div>

            <Locban />
            {showTableModal && (
               <div className="fixed inset-0 ml-[-120px] pt-16 bg-black bg-opacity-40 flex justify-center items-center z-100">
                  <div className="bg-zinc-400 bg-opacity-80 p-6 w-[500px] max-h-[90vh] overflow-y-auto">
                     <h3 className="text-xl font-bold mb-4">Chọn bàn cho đơn đặt bàn {selectedBooking?.id.slice(-4)}</h3>
                     <div className="grid grid-cols-2 gap-4">
                        {availableTables
                           .filter(table =>
                              table.capacity >= selectedBooking.quantityPeople &&
                              table.capacity <= selectedBooking.quantityPeople + 1
                           )
                           .map((table, index) => (
                              <div
                                 key={index}
                                 className={`border p-4 rounded-lg cursor-pointer ${selectedTable?.id === table.id ? 'bg-blue-200 border-blue-500' : 'bg-gray-100'
                                    }`}
                                 onClick={() => setSelectedTable(table)}
                              >
                                 <p className="font-semibold">🪑 {table.id}</p>
                                 <p>Sức chứa: {table.capacity}</p>
                                 <p className="text-sm italic text-gray-600">{table.description}</p>
                              </div>
                           ))}
                     </div>

                     <div className="flex justify-end mt-6 space-x-4">
                        <button
                           onClick={() => {
                              setShowTableModal(false);
                              setSelectedTable(null);
                              setSelectedBooking(null);
                           }}
                           className="px-4 py-2 bg-gray-300 rounded"
                        >
                           Hủy
                        </button>
                        <button
                           disabled={!selectedTable}
                           className={`px-4 py-2 rounded ${selectedTable ? 'bg-blue-600 text-white' : 'bg-gray-400 text-gray-700 cursor-not-allowed'
                              }`}
                           onClick={handlePickTable}
                        >
                           Chọn
                        </button>
                     </div>
                  </div>
               </div>
            )}
         </div >
         {isModalOpen && (
            <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
               <div className="bg-slate-700 rounded-lg shadow-lg w-[90%] sm:w-[600px] p-6 relative">
                  <h3 className="text-2xl font-bold text-red-600 text-center mb-4">{modalMessage}</h3>
                  <div className="flex space-x-4 justify-center">
                     <button
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-red-500 transition duration-300"
                        onClick={() => setIsModalOpen(false)}>
                        OK
                     </button>
                  </div>
               </div>
            </div>
         )};
      </div>
   );

};

export default Banchuaxacnhan;
