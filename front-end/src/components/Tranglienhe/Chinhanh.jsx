
import chinhanh_1 from "../../assets/chinhanh_1.png";
import chinhanh_2 from "../../assets/chinhanh_2.png";
import chinhanh_3 from "../../assets/chinhanh_3.png";

import icon_location from "../../assets/icon_location.png";
import icon_email from "../../assets/icon_email.png";
import icon_phone from "../../assets/icon_phone.png";

const restaurant = [
   {
      resname: 'Steak Love Độc Lập',
      address: '106 Nguyễn Thị Minh Khai, Phường Võ Thị Sáu, Quận 3, Thành phố Hồ Chí Minh',
      tel: '19007901',
      email: 'SteakLoveDocLap@gmail.com',
      img: chinhanh_1,
   },
   {
      resname: 'Steak Love The Villa',
      address: '290 Điện Biên Phủ, Phường Võ Thị Sáu, Quận 3, Thành phố Hồ Chí Minh',
      tel: '19007902',
      email: 'SteakLoveTheVilla@gmail.com',
      img: chinhanh_2,
   },
   {
      resname: 'Steak Love Diamond',
      address: 'Tầng 5 Diamond Plaza, 34 Lê Duẩn, Phường Bến Nghén, Quận 1, Thành phố Hồ Chí Minh',
      tel: '19007903',
      email: 'SteakLoveDiamond@gmail.com',
      img: chinhanh_3,
   },
];

const Chinhanh = () => {
   return (
      <div className="z-10 pt-4 flex flex-col space-y-4 justify-start items-center h-full w-full">
         <h2 className="text-4xl font-oswald leading-tight-custom font-light300 whitespace-nowrap">Các chi nhánh</h2>
         <div className="w-32 h-[2px] bg-zinc-500"></div>

         {restaurant.map((res, index) => (
            <div
               key={index}
               className={`flex flex-col md:flex-row ${index % 2 === 0 ? 'md:flex-row' : 'md:flex-row-reverse'
                  } items-center justify-center space-x-0 md:space-x-5 py-3 pl-2 gap-10`}>

               <div className=" h-full md:w-1/2">
                  <img
                     src={res.img}
                     alt="Topping Beef Restaurant"
                     className="w-full rounded-xl shadow-lg "
                  />
               </div>

               {/* Thông tin chi nhánh nhà hàng */}
               <div className=" md:w-1/2 text-center md:text-left pr-20">
                  <div className='flex-col space-y-3'>
                     {/* <h2 className="text-4xl mb-6 font-oswald leading-tight-custom font-light300 whitespace-nowrap"> */}
                        <h2 className="text-[40px] font-normal">
                        {res.resname}
                     </h2>
                     <p className="mb-4 font-roboto text-m text-justify leading-7 font-light300 whitespace-nowrap flex items-center gap-2">
                        <img src={icon_location} alt="Địa chỉ nhà hàng" className="w-[15px] h-[15px]" />
                        {res.address}
                     </p>
                     <p className="mb-4 font-roboto text-m text-justify leading-7 font-light300 whitespace-nowrap flex items-center gap-2">
                        <span>
                           <img src={icon_phone} alt="số điện thoại nhà hàng" className="w-[15px] h-[15px]" />
                        </span>
                        {res.tel}
                     </p>
                     <p className="mb-4 font-roboto text-m text-justify leading-7 font-light300 whitespace-nowrap flex items-center gap-2">
                        <span>
                           <img src={icon_email} alt="Email nhà hàng" className="w-[15px] h-[15px]" />
                        </span>
                        {res.email}
                     </p>
                  </div>
               </div>
            </div>
         ))}

      </div >
   );
};

export default Chinhanh; 