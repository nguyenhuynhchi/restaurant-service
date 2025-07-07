import Navbar from '../Navbar-v2.jsx'
import Banner from './Banner.jsx'
import Bestseller from './Bestseller.jsx';
import Middle from './Middle.jsx'

const Tranggioithieu = () => {
    return (
        <div>
            <Navbar />
            <div className="pt-[60px]">
                <Banner />
                <Middle />
                <Bestseller />
            </div>
        </div>
    );
};

export default Tranggioithieu