/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'banner' : 'url(/background.png)'
      },
      fontFamily: {
        oswald: ['Oswald', 'sans-serif'], // Định nghĩa riêng nếu cần
        roboto: ['Roboto', 'sans-serif']
      },
      fontSize: {
        'custom-44': '44px'
      },
      lineHeight: {
        'tight-custom': '1.2'
      },
      fontWeight: {
        light300: '300'
      }
    },
  },
  plugins: [],
}

