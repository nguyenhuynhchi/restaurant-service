import Navbar from '../Navbar-v2.jsx'
import Banner from './Banner.jsx'
import Middle from './Middle.jsx'
import ContentLeft from './Content_left.jsx'
import ContentRight from './Content_right.jsx'
import Background from './Background.jsx'
import Footer from '../Footer.jsx';

const Trangchu = () => {
    return (
        <div className='bg-gray-300'>
            <Navbar />
            <div className="pt-[60px]">
                <Banner />
                <Middle />
                <Background />
                <div className="flex h-[900px] mt-[-900px] z-20">
                    <div className="w-1/2 h-full flex justify-center items-start">
                        <ContentLeft />
                    </div>

                    <div className="w-1/2 h-full flex justify-center items-start">
                        <ContentRight />
                    </div>
                </div>
            </div>
            <Footer/>
        </div>
    );
};

export default Trangchu