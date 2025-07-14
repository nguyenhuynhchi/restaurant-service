import React, { useEffect, useState } from "react";
import Navbar from '../Navbar-v2.jsx'
import Thongtindatban from "./Thongtindatban.jsx";

const Trangdatban = () => {

   return (
      <div className="overflow-hidden">
         <Navbar />
         <Thongtindatban />
      </div>
   );
};

export default Trangdatban;