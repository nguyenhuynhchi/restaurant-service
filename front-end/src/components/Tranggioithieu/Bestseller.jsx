import React from 'react';
import bestseller_1 from "../../assets/bestSeller_1.png";
import bestseller_2 from "../../assets/bestSeller_2.png";
import bestseller_3 from "../../assets/bestSeller_3.png";
import bestseller_4 from "../../assets/bestSeller_4.png";
import Icon_bestSeller from "../../assets/bestSeller_icon.png";

const images = [
    {
        src: bestseller_1,
        position: 'left',
        title: 'Steak Love',
        desc: [
            'Một món chính đậm vị với steak tươi ngon và rau củ. Phần Steak được cắt hình trái tim. Tùy chọn loại sốt như:',
            'Sốt nấm',
            'Sốt tiêu',
            'Sốt vang đỏ',
            'Sốt kem',
            'Sốt BBQ'
        ],
    },
    {
        src: bestseller_3,
        position: 'left',
        title: 'Beef Wellington AUS Tenderloin',
        desc: [
            'Bò được cuộn trong bột bánh ngàn lớp cùng với nấm & thịt xông khói; ăn kèm khoai tây nghiền, khoai tây đút lò, nấm & bó xôi xào tỏi',
            'Dòng bò ăn cỏ, vân mỡ nhiều, đậm vị bò, thơm vị bơ, không chứa kháng sinh mang lại sản phẩm với hương vị xuất sắc, ổn định.',
            'Độ chín ngon nhất: medium'
        ],
    },
    {
        src: bestseller_4,
        position: 'right',
        title: 'Cold Cuts',
        desc: [
            'Món ăn là sự kết hợp tinh tế giữa đùi heo muối đậm vị, phô mai béo ngậy và pate nhum độc đáo, tạo nên hương vị biển cả đầy quyến rũ.',
            'Trái olive chua nhẹ và bánh mì đen mềm thơm giúp cân bằng vị giác, mang lại trải nghiệm ẩm thực hài hòa, sang trọng và lạ miệng.'
        ],
    },
    {
        src: bestseller_2,
        position: 'right',
        title: 'Combo USDA CHOICE',
        desc: [
            'Hấp dẫn với hai miếng Steak áp chảo, xúc xích Đức nướng đậm đà cùng khoai tây chiên giòn rụm. Ba loại nước sốt kèm theo tăng thêm hương vị đậm đà, phù hợp với nhiều khẩu vị.',
            '300g Top Blade',
            '400g USDA Ribeye',
            'Xúc xích Đức',
            'Khoai tây chiên & 03 loại sốt (sốt kem nấm, sốt tiêu & sốt BBQ)'
        ],
    },
];

const Bestseller = () => {
    return (
        <div className="bg-gray-300 py-10">
            <div className="grid grid-cols-2 gap-6 justify-center items-start w-full max-w-5xl mx-auto">
                {images.map((img, idx) => (
                    <div
                        key={idx}
                        // className={`flex flex-col items-center relative`}>
                        className={`flex flex-col items-center relative ${idx === 3 ? '-mt-[230px]' : ''}`}>
                        {idx === 0 && (
                            <div className="flex">
                                <img src={Icon_bestSeller} alt="icon_chinhanh"
                                    className="w-[60px] h-[60px]" />
                                <span className="text-4xl mb-6 py-[5px] font-oswald leading-tight-custom font-light300 whitespace-nowrap">
                                    BEST SELLER
                                </span>
                            </div>
                        )}

                        {/* Hình ảnh + hover effect */}
                        <div className="relative group overflow-hidden rounded-xl w-full">
                            <img
                                src={img.src}
                                alt={`img-${idx}`}
                                className="w-full h-auto object-cover rounded-xl transition-all duration-500 ease-in-out transform group-hover:scale-105" />
                            <div
                                className="absolute inset-0 bg-black/70 text-white opacity-0 group-hover:opacity-100
                                flex flex-col items-center justify-center text-sm transition-opacity duration-500
                                pointer-events-none px-4 pb-6">

                                <h3 className="text-2xl font-bold text-center font-oswald leading-tight-custom whitespace-nowrap mb-4">{img.title}</h3>

                                {/* description */}
                                <div className='flex-col space-y-3 mt-[20px]'>
                                    {img.desc.map((paragraph, pIdx) => (
                                        <p key={pIdx} className="text-justify font-medium leading-relaxed px-2">
                                            {paragraph}
                                        </p>
                                    ))}
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};


export default Bestseller
