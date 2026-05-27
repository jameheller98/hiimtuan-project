"use client";

import { useLogoutMutation } from "@/api/mutations/auth/useLogoutMutation";
import { useProfileQuery } from "@/api/queries/auth/useProfileQuery";
import useClickOutside from "@/hooks/useClickOutside";
import { useIsMobile } from "@/hooks/useIsMobile";
import { useAuthStore } from "@/stores";
import { animated, useTransition, config } from "@react-spring/web";
import classNames from "classnames";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import React, {
  Ref,
  RefObject,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from "react";

export default function Header() {
  const { profile } = useProfileQuery();
  const { token } = useAuthStore();
  const pathname = usePathname();
  const [pathShouldHidden] = useState([
    "/auth/login",
    "/auth/register",
    "/auth/forgot-password",
    "/auth/change-password",
  ]);
  const refDropDown = useRef<RDropDown>(null);
  const refButtonUser = useRef<HTMLDivElement>(null);

  return (
    <header
      className={classNames(
        "relative py-5 px-4 flex gap-4 items-center sm:px-5 md:px-6",
        {
          hidden: pathShouldHidden.some((item) => pathname.includes(item)),
        }
      )}
    >
      <div className="flex-1 text-left">
        <Link href="/" className="font-semibold text-xl ">
          Hiimtuan Blog
        </Link>
      </div>
      {!token ? (
        <div>
          <Link href="/auth/login" className="text-(--color-blue)">
            Sign in
          </Link>{" "}
          /{" "}
          <Link href="/auth/register" className="text-(--color-blue)">
            Sign up
          </Link>
        </div>
      ) : (
        <div
          ref={refButtonUser}
          onClick={() => refDropDown.current?.trigger()}
          className="flex items-center gap-1 px-4 py-1 rounded-lg lg:gap-2 cursor-pointer bg-white/10 border border-white/20 transition duration-300 hover:backdrop-blur-md hover:shadow-lg hover:bg-white/20"
        >
          <span className="text-sm hidden sm:block">
            {profile?.data?.fullName}
          </span>
          <Image
            src="/svgs/user.svg"
            alt="user"
            width={24}
            height={24}
            className="w-10 h-10"
          />
        </div>
      )}
      <DropDown ref={refDropDown} refButtonUser={refButtonUser} />
    </header>
  );
}

interface RDropDown {
  trigger: () => void;
}

const DropDown = ({
  ref,
  refButtonUser,
}: {
  ref?: Ref<RDropDown>;
  refButtonUser: RefObject<HTMLDivElement | null>;
}) => {
  const { profile } = useProfileQuery();
  const { clearAll } = useAuthStore();
  const { trigger } = useLogoutMutation();
  const refDropDownInner = useRef<HTMLUListElement>(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [refState] = useState([refButtonUser]);
  const isMobile = useIsMobile();

  useImperativeHandle(
    ref,
    () => {
      return {
        trigger: () => {
          setShowDropdown((open) => !open);
        },
      };
    },
    []
  );

  useClickOutside(
    refDropDownInner,
    () => {
      setShowDropdown(false);
    },
    refState
  );

  useEffect(() => {
    setShowDropdown(false);
  }, [isMobile]);

  const transitions = useTransition(showDropdown, {
    from: { height: 0, opacity: 0, transform: "translateY(-6px) scale(0.98)" },
    enter: {
      height: isMobile ? 110 : 50,
      opacity: 1,
      transform: "translateY(0px) scale(1)",
    },
    leave: { height: 0, opacity: 0, transform: "translateY(-6px) scale(0.98)" },
    config: { ...config.stiff, tension: 300, friction: 26 },
  });

  const handleLogout = async () => {
    try {
      setShowDropdown(false);
      await trigger();
    } catch (e) {
      console.log(e);
    } finally {
      clearAll();
    }
  };

  return transitions(
    (styles, item) =>
      item && (
        <animated.div
          style={styles}
          className="absolute min-w-[15rem] w-full top-[calc(100%-1.25rem)] right-0 bg-white/10 shadow-xl px-5 py-3 border border-white backdrop-blur-md sm:top-[calc(100%-0.5rem)] sm:w-auto sm:rounded-2xl sm:right-5 md:right-6"
        >
          <ul
            ref={refDropDownInner}
            className="flex flex-col items-end sm:items-start gap-4"
          >
            <div className="border-b text-right border-b-gray-300 pb-3 w-full sm:hidden">
              {profile?.data?.fullName}
            </div>
            <DropDownItem
              icon="/svgs/logout.svg"
              title="Logout"
              handleClick={handleLogout}
            />
          </ul>
        </animated.div>
      )
  );
};

const DropDownItem = ({
  icon,
  title,
  handleClick,
}: {
  icon: string;
  title: string;
  handleClick?: () => void;
}) => {
  return (
    <li className="flex cursor-pointer" onClick={handleClick}>
      <Image src={icon} alt={title} width={24} height={24} />
      <span>{title}</span>
    </li>
  );
};
