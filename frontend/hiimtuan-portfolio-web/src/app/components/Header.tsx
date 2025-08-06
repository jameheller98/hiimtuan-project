"use client";

import { useLogoutMutation } from "@/api/mutations/auth/useLogoutMutation";
import { useAuthStore } from "@/stores";
import classNames from "classnames";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import React, { useState } from "react";

export default function Header() {
  const { token, clearAll } = useAuthStore();
  const { trigger } = useLogoutMutation();
  const pathname = usePathname();
  const [pathShouldHidden] = useState([
    "/auth/login",
    "/auth/register",
    "/auth/forgot-password",
    "/auth/change-password",
  ]);

  const handleLogout = async () => {
    try {
      await trigger();
    } catch (e) {
      console.log(e);
    } finally {
      clearAll();
    }
  };

  return (
    <header
      className={classNames("py-5 px-4 text-center sm:px-5 md:px-6", {
        hidden: pathShouldHidden.some((item) => pathname.includes(item)),
      })}
    >
      <div className="flex gap-4 items-center">
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
          <Image
            onClick={handleLogout}
            src="/svgs/user.svg"
            alt="user"
            width={24}
            height={24}
            className="w-10 h-10"
          />
        )}
      </div>
    </header>
  );
}
