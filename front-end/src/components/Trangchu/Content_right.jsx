
import Icon_nhahang from "../../assets/icon_nhahang.png";

const Content = () => {
    return (
        <div className="flex flex-col items-center p-12 py-[80px] z-30 space-y-7">
            <div className="text-[40px] text-white font-extralight">
                Mở cửa 7 ngày trong tuần
            </div>

            <div className="text-[30px] text-red-600 font-semibold">
                Đặt bàn bất cứ lúc nào
            </div>

            <div className="text-white text-x leading-7 font-roboto font-light300 space-y-4 px-10 text-justify">
                <p>Steak Love mang đến cho khách hàng một thực đơn đa dạng, phù hợp với nhiều khẩu vị khác nhau, từ những tín đồ steak yêu thích sự đậm đà đến những thực khách tìm kiếm sự mới mẻ và sáng tạo.</p>
                <p>Bạn sẽ được thưởng thức vô vàn món ngon với những món steak được chế biến bằng nguồn nguyên liệu tươi ngon đến từ Úc, Mỹ, Nhật và đặc biệt tự hào giới thiệu món steak hảo hạng từ nguồn thịt bò chất lượng của Việt Nam trong thời gian tới. Mì Ý chuẩn vị béo thơm cay nồng hay một đĩa salad với nước sốt đặc biệt. Tất cả sẽ hòa quyện cùng 1 ly vang đỏ sóng sánh hay 1 ly bia tươi mát lạnh.</p>
                <p>Hãy đến với Steak Love để thưởng thức và cảm nhận, sự tài hoa của chính đầu bếp trưởng nơi đây, cùng đội ngũ nhân viên phục vụ chuyên nghiệp đem đến cho thực khách một buổi thưởng thức món ăn thật lý tưởng.</p>
            </div>

            <div className="flex space-x-2 items-center">
                <div className="bg-white rounded-full p-3 flex items-center justify-center">
                    <img src={Icon_nhahang} alt="Icon nha hang" className="w-[60px] h-[60px]" />
                </div>
                <div className="flex flex-col space-y-2">
                    <div>
                        <p className="text-red-600 text-xl font-bold">Thời gian nhà hàng mở cửa</p>
                    </div>
                    <div className="text-white">
                        <p>Thứ 2 đến Chủ nhật</p>
                        <p>11 AM - 22 PM</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Content;