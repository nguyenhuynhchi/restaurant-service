import Navbar from '../Navbar-v2.jsx'
import Bandadat from './Bandadat_v2.jsx';
import Lichsudatban from './Lichsudatban.jsx';
const background = "/background_2.png";

const Trangxembandat = () => {
   return (
      <div>
         <Navbar />
         <div className="pt-[60px]">
            <div>
               <div className="absolute w-full h-full top-0 left-0 bg-current">
                  <img src={background} alt="background right" className="fixed w-full h-[900px] " />
               </div>
            </div>
            <div className="relative z-10 flex flex-col space-y-7 justify-center items-center pt-[50px] pb-[100px] ">
               <Bandadat />
               <Lichsudatban />
            </div>
         </div>
      </div>
   );
};

export default Trangxembandat;