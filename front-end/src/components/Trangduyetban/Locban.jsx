import React, { useEffect, useState } from "react";
import { getValidToken } from "../authService.js";

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

const quantityOptions = [
   { label: "2", value: "2" },
   { label: "3-4", value: "3" },
   { label: "5-6", value: "5" },
];

const Locban = () => {

   const [filteredReservations, setFilteredReservations] = useState([]);

   const [selectedBranch, setSelectedBranch] = useState('');
   const [selectedQuantity, setSelectedQuantity] = useState('');

   const handleFilter = async () => {
      try {
         const token = await getValidToken();

         const requestBody = JSON.stringify({
               restaurant: selectedBranch,
               quantityPeople: selectedQuantity,
            }); 
         const response = await fetch("http://localhost:8386/restaurant/reservation/filter", {
            method: "POST",
            headers: {
               "Content-Type": "application/json",
               "Authorization": `Bearer ${token}`,
            },
            body: requestBody,
         });

         console.log("Gửi request lọc:\n", requestBody)

         if (!response.ok) throw new Error("Không thể lọc dữ liệu");

         const data = await response.json();
         setFilteredReservations(data.result);
      } catch (error) {
         console.error("Lỗi khi lọc:", error);
      }
   };

   const InfoItem = ({ label, value }) => {
      const isTableLabel = label === "🍽️ Bàn:";
      return (
         <div className="flex flex-col space-y-1 bg-zinc-300 rounded-xl p-2 items-center">
            <p className="font-semibold">{label}</p>
            <div className="bg-zinc-600 w-full text-center rounded-xl px-2 py-1 text-slate-100 min-h-[40px] flex items-center justify-center">
               {typeof value === "string" || typeof value === "number" ? (
                  <p className={`${(isTableLabel) ? "font-bold text-green-500 text-[20px]" : ""}`}>{value}</p>
               ) : (
                  value
               )}
            </div>
         </div>
      );
   };

   return (
      <div className="w-full md:w-1/2 space-y-6">
         {/* Khung lọc */}
         <div className="bg-blue-300 p-5 rounded-lg shadow-md border border-gray-300">
            <h3 className="text-xl font-semibold text-gray-700 mb-4">Lọc bàn đặt</h3>

            <div className="flex space-x-2 mb-4">
               <div className="w-1/2">
                  <label className="block text-sm font-medium mb-1">Chi nhánh:</label>
                  <select
                     value={selectedBranch}
                     onChange={(e) => setSelectedBranch(e.target.value)}
                     className="w-full p-2 border rounded-md"
                  >
                     <option value="">-- Tất cả --</option>
                     {restaurantName.map(res => (
                        <option key={res.resID} value={res.resID}>{res.resname}</option>
                     ))}
                  </select>
               </div>

               <div className="w-1/2">
                  <label className="block text-sm font-medium mb-1">Số người:</label>
                  <select
                     value={selectedQuantity}
                     onChange={(e) => setSelectedQuantity(e.target.value)}
                     className="w-full p-2 border rounded-md"
                  >
                     <option value="">-- Tất cả --</option>
                     {quantityOptions.map((opt, idx) => (
                        <option key={idx} value={opt.value}>{opt.label}</option>
                     ))}
                  </select>
               </div>
               <button
                  onClick={handleFilter}
                  className="bg-blue-600 text-white px-6 rounded-md hover:bg-blue-700"
               >
                  Lọc
               </button>
            </div>
         </div>

         {/* Kết quả lọc (giống danh sách chính) */}
         {filteredReservations.length > 0 && (
            <div className="space-y-6">
               {filteredReservations.map((res, idx) => (
                  <div key={idx} className="bg-green-900 shadow-md rounded-lg p-6 border border-gray-300">
                     <div className="grid grid-cols-2 gap-y-4 gap-x-6 text-gray-800">
                        <InfoItem label="⏰ Thời gian nhận bàn:" value={new Date(res.reservationTime).toLocaleString("vi-VN")} />
                        <InfoItem label="🏠 Chi nhánh:" value={restaurantName.find(item => item.resID === res.restaurant)?.resname || "Không rõ"} />
                        <InfoItem label="👥 Số lượng người:" value={res.quantityPeople} />
                        <InfoItem label="💬 Tin nhắn ghi chú:" value={res.messenger || "Không có"} />
                        <InfoItem label="🍽️ Bàn:" value={res.table} />
                     </div>
                  </div>
               ))}
            </div>
         )}
      </div>
   );
};

export default Locban;