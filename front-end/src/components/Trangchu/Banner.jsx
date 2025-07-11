import { Button } from './Button';
import { Link } from "react-router-dom";

const Banner = () => {
    return (
        <div className="w-full h-[400px] bg-banner bg-center bg-no-repeat bg-cover relative rounded-b-[30px]">
            <div className="absolute bottom-16 left-1/2 transform -translate-x-1/2">
                <Link to="/datban">
                    <Button>
                        ĐẶT BÀN
                    </Button>
                </Link>
            </div>
        </div>
    );
};

export default Banner;