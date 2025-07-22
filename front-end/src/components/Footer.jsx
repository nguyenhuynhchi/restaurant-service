import React from "react";
import work_icon from "../assets/work_icon.png";
import contact_mail from "../assets/contact-mail.png";
import { Link } from "react-router-dom";

const Footer = () => {
   return (
      <footer className="bg-[#0a0624] text-gray-300 pt-2 pb-8">
         <div className="mx-auto flex flex-col items-center">

            <h3 className="text-s font-roboto pb-5 text-center text-white">
               Đồ án kết thúc môn<br />
               Công nghệ phần mềm</h3>

            <div className="flex w-full justify-around">
               <div className="text-center">
                  <div className="flex items-center space-x-3 justify-center text-lg font-roboto">
                     <p className="w-[25px] h-[25px]">☰</p>
                     <p>Danh mục chính:</p>
                  </div>
                  <ul className="space-y-[5px] text-left ml-10 mt-1 underline">
                     <Link to="/trangchu" className="hover:font-semibold"><li>Trang chủ</li></Link>
                     <Link to="/trangchu" className="hover:font-semibold"><li>Giới thiệu</li></Link>
                     <Link to="/lienhe" className="hover:font-semibold"><li>Liên hệ</li></Link>
                  </ul>
               </div>

               <div className="text-center">
                  <div className="flex items-center space-x-3 justify-center text-lg font-roboto">
                     <img src={work_icon} alt="" className="w-[25px] h-[25px]" />
                     <p>Make by:</p>
                  </div>
                  <div className="flex space-x-5">
                     <ul className="space-y-[5px] text-center mt-1">
                        <li>Nguyễn Huỳnh Chí</li>
                        <li>Nguyễn Trọng Đạt</li>
                        <li>Lê Khánh Duy</li>
                     </ul>
                     <ul className="space-y-[5px] mt-1">
                        <li>110122001</li>
                        <li>110122217</li>
                        <li>110122002</li>
                     </ul>
                  </div>
               </div>

               <div className="text-center">
                  <div className="flex items-center space-x-3 justify-center text-lg font-roboto">
                     <img src={contact_mail} alt="" className="w-[25px] h-[25px]" />
                     <p>Contact us:</p>
                  </div>
                  <ul className="space-y-[5px]">
                     <li>huynhchi0904@gmail.com</li>
                     <li>nguyentrongdat10244@gmail.com</li>
                     <li>lekhanhduyc3tieucan22@gmail.com</li>
                  </ul>
               </div>
            </div>
         </div>
      </footer>
   );
};

export default Footer;
