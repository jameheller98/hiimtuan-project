import SlideTransition from "@/app/components/SlideTransition";
import Image from "next/image";
import FormLogin from "./components/FormLogin";
import Footer from "../../components/Footer";
import Link from "next/link";
import BackIcon from "@/app/components/BackIcon";

export default function Login() {
  return (
    <main className="overflow-hidden flex flex-col min-h-screen p-6 pb-0 gap-7 lg:p-8 lg:gap-8 lg:flex-row-reverse">
      <SlideTransition className="lg:flex-1" position="right">
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
      <div className="w-full flex-1 flex flex-col lg:w-auto lg:items-center lg:justify-center">
        <SlideTransition className="sm:hidden" position="left" delay={250}>
          <BackIcon />
        </SlideTransition>
        <SlideTransition
          className="relative mt-4 sm:mt-0 sm:mx-auto sm:w-2/3 2xl:w-1/2"
          position="left"
          delay={250}
        >
          <SlideTransition
            className="hidden sm:mb-4 sm:block lg:absolute lg:bottom-full"
            position="left"
            delay={250}
          >
            <BackIcon />
          </SlideTransition>
          <p className="font-semibold text-2xl">Welcome to my heart</p>
          <p className="text-base mt-4">
            Today is a new day. It&apos;s your day. Sign in to start new your
            stories.
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
            <Link href="/auth/register" className="text-(--color-blue)">
              Sign up
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
