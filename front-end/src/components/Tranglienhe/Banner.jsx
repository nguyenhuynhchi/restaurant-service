import React from "react";
import 'typeface-oswald';
import banner_tren from "../../assets/bannerGioithieu_tren.png";

const Banner = () => {
  return (
    <div className="bg-white text-gray-800">
      {/* Banner full width */}
      <div className="w-full h-[400px]">
        <img
          src={banner_tren}
          alt="Banner Steak Love"
          className="w-full h-full object-cover"
        />
      </div>
    </div>
  );
};

export default Banner;
