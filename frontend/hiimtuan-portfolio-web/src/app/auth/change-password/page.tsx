import SlideTransition from "@/app/components/SlideTransition";
import Footer from "../../components/Footer";
import FormChangePassword from "./components/FormChangePassword";

export default function ChangePassword() {
  return (
    <main className="overflow-hidden flex min-h-screen p-6 pb-0 gap-7 lg:p-8 lg:gap-8">
      <div className="relative flex-1 flex flex-col max-w-[700px] mx-auto lg:items-center lg:justify-center">
        <SlideTransition
          className="relative mt-4 sm:mt-0 sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="right"
          delay={250}
        >
          <p className="font-semibold text-2xl">
            Let&apos;s retrieve account for you
          </p>
          <p className="text-base mt-4 whitespace-break-spaces">
            Enter a new password to retrieve account.
          </p>
        </SlideTransition>
        <SlideTransition
          className="sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="down"
          delay={500}
        >
          <FormChangePassword />
        </SlideTransition>
        <SlideTransition
          className="mt-auto lg:absolute lg:bottom-0"
          position="right"
          delay={750}
        >
          <Footer isAuthPage className="pt-12 lg:py-0" />
        </SlideTransition>
      </div>
    </main>
  );
}
