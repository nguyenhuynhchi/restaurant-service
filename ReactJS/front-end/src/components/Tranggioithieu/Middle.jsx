import React from "react";
import icon_1 from "../../assets/icon_1_gioithieu.png";
import icon_2 from "../../assets/icon_2_gioithieu.png";
import icon_3 from "../../assets/icon_3_gioithieu.png";

const Middle = () => {
  const features = [
    {
      title: "KHÔNG GIAN ĐẲNG CẤP",
      description:
        "Steak Love mang phong cách sang trọng, đẳng cấp với những món ngon xứng tầm là lựa chọn hoàn hảo cho các Steak Lovers trải nghiệm nền ẩm thực đa dạng và sáng tạo từ Á đến Âu. Hãy để chúng tôi mang đến cho bạn món ăn trọn vẹn cả hương vị và thỏa mãn mọi giác quan!",
      icon: icon_1,
    },
    {
      title: "PHỤC VỤ CHUYÊN NGHIỆP",
      description:
        "Với mong muốn mang đến trải nghiệm ẩm thực hoàn hảo, chúng tôi luôn mang đến phong thái phục vụ chuyên nghiệp nhất thỏa mãn những thượng khách của Steak Love.",
      icon: icon_3,
    },
    {
      title: "MENU ĐỘC ĐÁO",
      description:
        "Các phần steak chất lượng được tuyển chọn khắt khe bởi đầu bếp nhiều năm kinh nghiệm, trải nghiệm ẩm thực cao cấp giúp khách hàng không chỉ cảm nhận món ăn qua vị giác, khứu giác, thị giác mà còn đi vào tâm trí với những câu chuyện đặc sắc chờ đợi các Steak Lovers khám phá.",
      icon: icon_2,
    },
  ];

  return (
    <div className="bg-gray-100 py-10 px-4">
      <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-20 text-center">
        {features.map((feature, index) => (
          <div key={index} className="flex flex-col items-center">
            {/* Icon bo tròn */}
            <div className="bg-white rounded-full w-24 h-24 flex items-center justify-center shadow-md mb-8">
              <img src={feature.icon} alt={feature.title} className="w-24 h-24" />
            </div>
            {/* Tiêu đề */}
            <h3 className="text-xl font-bold uppercase mb-2 leading-tight-custom">{feature.title}</h3>
            {/* Gạch ngang dưới tiêu đề */}
            <div className="w-12 h-[2px] bg-gray-400 mb-4"></div>
            {/* Mô tả */}
            <p className="text-gray-700 text-justify leading-relaxed px-2">{feature.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Middle;
