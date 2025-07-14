import Navbar from '../Navbar-v2.jsx'
import Banner from './Banner.jsx'
import Bestseller from './Bestseller.jsx';
import Middle from './Middle.jsx';
import Footer from '../Footer.jsx';
const Tranggioithieu = () => {
    return (
        <div>
            <Navbar />
            <div className="pt-[60px]">
                <Banner />
                <Middle />
                <Bestseller />
            </div>
            <Footer />
        </div>
    );
};

export default Tranggioithieu