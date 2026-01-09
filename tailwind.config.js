/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#7d3bb3',
          100: '#7b44ab',
          200: '#624983',
          300: '#c199e4',
          400: '#e6c9ff',
          500: '#d6a8ff',
          600: '#b76bff',
          700: '#8f2bff',
          800: '#6c00e6',
          900: '#4b0073',
          950: '#2b0033',
        },
      },
    },
  },
  plugins: [],
};
