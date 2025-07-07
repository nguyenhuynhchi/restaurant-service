import Icon_chinhanh from "../../assets/icon_chinhanh.png";
import Icon_nhahang from "../../assets/icon_nhahang.png";
import { Link } from "react-router-dom";

const Middle = () => {
    return (
        <div className="w-1/2 mx-auto bg-white py-4 px-4 rounded-2xl shadow-lg shadow-gray-500/100 flex justify-center relative -mt-[40px] z-30">
            <ul className="flex w-full items-center justify-evenly">

                <Link to="/">
                    <li className="text-black font-semibold cursor-pointer">
                        <div className="flex flex-col items-center">
                            <img src={Icon_chinhanh} alt="icon_chinhanh"
                                className="w-[55px] h-[55px]" />
                            CHI NHÁNH
                        </div>
                    </li>
                </Link>

                <Link to="/gioithieu">
                    <li className="text-black font-semibold cursor-pointer">
                        <div className="flex flex-col items-center">
                            <img src={Icon_nhahang} alt="icon_nhahang"
                                className="w-[55px] h-[55px]" />
                            VỀ NHÀ HÀNG
                        </div>
                    </li>
                </Link>
            </ul>
        </div>
    );
};

export default Middle;