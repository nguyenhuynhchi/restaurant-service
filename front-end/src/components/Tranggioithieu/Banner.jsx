import React from "react";
import 'typeface-oswald';
import banner_tren from "../../assets/bannerGioithieu_tren.png";
import banner_trai from "../../assets/bannerGioithieu_trai.png";

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

      {/* Phần giới thiệu */}
      <div className="flex flex-col md:flex-row items-center justify-center space-x-[100px]  py-5 px-5 gap-10 bg-gray-300">
        <div className="w-full h-full md:w-1/2">
          <img
            src={banner_trai}
            alt="Topping Beef Restaurant"
            className="w-full rounded-xl shadow-lg"
          />
        </div>

        {/* Nội dung giới thiệu */}
        <div className="w-full md:w-1/2 text-center md:text-left font-mono pr-20">
          <h2 className="text-4xl mb-6 font-oswald leading-tight-custom font-light300 whitespace-nowrap">
            THERE IS A MINI EUROPE IN THE HEART OF SAIGON
          </h2>
          <p className="mb-4 font-roboto text-m text-justify leading-7 font-light300">
            Không gian tại Steak Love, chúng tôi mang đến những miếng steak hảo hạng,
            được chế biến từ nguồn nguyên liệu chất lượng cao, nhưng vẫn giữ đúng giá trị
            của từng món ăn.
          </p>
          <p className="mb-4 font-roboto text-m text-justify leading-7 font-light300">
            Không gian nhà hàng được thiết kế sang trọng nhưng vô cùng ấm cúng, thân mật.
            Đây là nơi bạn có thể thoải mái tận hưởng hương vị tuyệt hảo, dù là bữa tiệc
            gia đình, buổi gặp gỡ đối tác hay một buổi tối lãng mạn cùng người thương.
          </p>
          <p className="mb-4 font-roboto text-m text-justify leading-7 font-light300">
            Với phương châm “True Flavor, True Value”, Steak Love cam kết phục vụ steak
            hảo hạng với mức giá xứng đáng. Chúng tôi tập trung vào việc mang đến trải
            nghiệm ẩm thực ấm cúng, gần gũi nhưng vẫn đẳng cấp, tinh tế.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Banner;
