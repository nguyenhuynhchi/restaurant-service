import React, { useEffect, useState } from "react";
const background = "/background.png";

const Bandadat = () => {
   return (
      <div className="pt-[60px]">
         <div>
            <div className="absolute w-full h-full top-0 left-0 bg-current">
               <img src={background} alt="background right" className="fixed w-full h-[900px] " />
            </div>
         </div>
         <div className="relative z-10 flex flex-col space-y-7 justify-center items-center pt-[50px] pb-[100px] ">
            <div className="w-[80%] p-8 rounded-lg shadow-lg bg-white bg-opacity-80 z-10">
               <div className="absolute inset-0  rounded-lg z-0"></div>
               <div className="relative z-10 p-8 flex flex-col justify-start mt-[-50px] items-center h-full w-full">
                  <div className="flex space-x-1">
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                     <h2 className="text-2xl font-bold text-gray-800 mb-4">
                        Bàn đã đặt
                     </h2>
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                  </div>

               </div>
            </div>

            <div className="w-[80%] p-8 rounded-lg shadow-lg bg-white bg-opacity-80 z-10">
               <div className="absolute inset-0  rounded-lg z-0"></div>
               <div className="relative z-10 p-8 flex flex-col justify-start mt-[-50px] items-center h-full w-full">
                  <div className="flex space-x-1">
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                     <h2 className="text-2xl font-bold text-gray-800 mb-4">
                        Lịch sử đặt bàn
                     </h2>
                     <div className="w-40 h-[2px] bg-slate-700 mt-[20px]"></div>
                  </div>

               </div>
            </div>
         </div>
      </div>
   );
};

export default Bandadat;