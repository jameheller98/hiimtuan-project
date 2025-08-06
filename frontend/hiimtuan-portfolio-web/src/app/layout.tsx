import type { Metadata } from "next";
import { Montserrat } from "next/font/google";
import "./globals.css";
import Footer from "./components/Footer";
import { ToastContainer } from "react-toastify";
import Header from "./components/Header";
import { SWRConfig } from "swr";

const montserrat = Montserrat({
  variable: "--font-montserrat",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Hiimtuan portfolio",
  description: "Website design by Tuan Nguyen",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={montserrat.className}>
      <body className="flex flex-col min-h-screen">
        <SWRConfig
          value={{
            shouldRetryOnError: false,
          }}
        >
          <Header />
          {children}
          <Footer />
          <ToastContainer />
        </SWRConfig>
      </body>
    </html>
  );
}
