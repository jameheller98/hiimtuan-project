import SlideTransition from "@/app/components/SlideTransition";
import Image from "next/image";

import BackIcon from "@/app/components/BackIcon";
import Footer from "../../components/Footer";
import FormRegister from "./components/FormRegister";
import Link from "next/link";

export default function Register() {
  return (
    <main className="overflow-hidden flex flex-col min-h-screen p-6 pb-0 gap-7 lg:p-8 lg:gap-8 lg:flex-row">
      <SlideTransition className="lg:flex-1" position="left">
        <Image
          src="/images/auth/metro-2.jpg"
          alt="metro-2"
          className="w-full aspect-[342/180] object-cover object-bottom-left rounded-[20px] lg:hidden"
          width={5472}
          height={3648}
          priority
        />
        <Image
          src="/images/auth/metro-1.jpg"
          alt="metro-1"
          className="hidden h-[calc(100vh-32px*2)] object-cover object-bottom-left rounded-[20px] lg:block"
          width={3748}
          height={5472}
          priority
        />
      </SlideTransition>
      <div className="relative w-full flex-1 flex flex-col lg:w-auto lg:items-center lg:justify-center">
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
          <p className="font-semibold text-2xl">Bring your mind to me</p>
          <p className="text-base mt-4">
            Connect to my world. Ready share your experience, knowledge, hobbies
            and more...
          </p>
        </SlideTransition>
        <SlideTransition
          className="sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="down"
          delay={500}
        >
          <FormRegister />
          <p className="text-center mt-6">
            Already have an account?{" "}
            <Link href="/auth/login" className="text-(--color-blue)">
              Sign in
            </Link>
          </p>
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
