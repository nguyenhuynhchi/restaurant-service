import React from "react";

export function Button({ children, ...props }) {
  return (
    <button {...props} className="px-10 py-4 bg-red-600 text-white font-semibold rounded-xl">
      {children}
    </button>
  );
}
