import backgorundLeft from "../../assets/background_left.png";
import backgroundRight from "../../assets/background_right.png";

const Background = () => {
    return (
        <div className="flex mt-[-30px]">

            <div className="w-1/2 relative">
                <div className="absolute w-full h-full top-0 left-0 bg-slate-800 opacity-70 rounded-t-[30px]" />
                <img src={backgroundRight} alt="background left" className="w-full h-[900px] rounded-t-[30px]" />
            </div>
            <div className="w-1/2 relative">
                <div className="absolute w-full h-full top-0 left-0 bg-current opacity-60 rounded-t-[30px]" />
                <img src={backgorundLeft} alt="background right" className="w-full h-[900px] rounded-t-[30px]" />
            </div>
        </div>
    );
};

export default Background