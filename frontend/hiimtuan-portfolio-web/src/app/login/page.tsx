import SlideTransition from "@/app/login/components/SlideTransition";
import Image from "next/image";
import FormLogin from "./components/FormLogin";
import Footer from "../components/Footer";

export default function Login() {
  return (
    <main className="overflow-hidden flex flex-col min-h-screen p-6 pb-0 gap-7 lg:p-8 lg:gap-8 lg:flex-row-reverse">
      <SlideTransition className="lg:flex-1" position="right">
        <Image
          src="/metro-2.jpg"
          alt="metro-2"
          className="w-full aspect-[342/180] object-cover object-bottom-left rounded-[20px] lg:hidden"
          width={5472}
          height={3648}
          priority
        />
        <Image
          src="/metro-1.jpg"
          alt="metro-1"
          className="hidden h-[calc(100vh-32px*2)] object-cover object-bottom-left rounded-[20px] lg:block"
          width={3748}
          height={5472}
          priority
        />
      </SlideTransition>
      <div className="w-full flex-1 flex flex-col lg:w-auto lg:items-center lg:justify-center">
        <SlideTransition
          className="sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="left"
          delay={250}
        >
          <p className="font-semibold text-2xl">Welcome Back 👋</p>
          <p className="text-base mt-4">
            Today is a new day. It&apos;s your day. You shape it. Sign in to
            start managing your projects.
          </p>
        </SlideTransition>
        <SlideTransition
          className="sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="down"
          delay={500}
        >
          <FormLogin />
          <p className="text-center mt-6">
            Don&apos;t you have an account?{" "}
            <span className="text-(--color-blue)">Sign up</span>
          </p>
        </SlideTransition>
        <SlideTransition
          className="mt-auto lg:absolute lg:bottom-8"
          position="right"
          delay={750}
        >
          <Footer isLoginPage className="pt-12 lg:py-0" />
        </SlideTransition>
      </div>
    </main>
  );
}
