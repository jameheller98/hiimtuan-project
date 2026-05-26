import SlideTransition from "@/app/components/SlideTransition";
import BackIcon from "@/app/components/BackIcon";
import Footer from "../../components/Footer";
import FormForgotPassword from "./components/FormForgotPassword";

export default function ForgotPassword() {
  return (
    <main className="overflow-hidden flex min-h-screen p-6 pb-0 gap-7 lg:p-8 lg:gap-8">
      <div className="relative flex-1 flex flex-col max-w-[700px] mx-auto lg:items-center lg:justify-center">
        <SlideTransition className="sm:hidden" position="right" delay={250}>
          <BackIcon />
        </SlideTransition>
        <SlideTransition
          className="relative mt-4 sm:mt-0 sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="right"
          delay={250}
        >
          <SlideTransition
            className="hidden sm:mb-4 sm:block lg:absolute lg:bottom-full"
            position="right"
            delay={250}
          >
            <BackIcon />
          </SlideTransition>
          <p className="font-semibold text-2xl">
            Sometime you can forget something
          </p>
          <p className="text-base mt-4 whitespace-break-spaces">
            Alright let&apos;s take back the password to you.{"\n"}Input and
            send to email for retrieve password.
          </p>
        </SlideTransition>
        <SlideTransition
          className="sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="down"
          delay={500}
        >
          <FormForgotPassword />
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
