"use client";
import classNames from "classnames";
import { usePathname } from "next/navigation";
import React, { useState } from "react";

export default function Footer({
  className,
  isAuthPage = false,
}: {
  className?: string;
  isAuthPage?: boolean;
}) {
  const pathname = usePathname();
  const [pathShouldHidden] = useState([
    "/auth/login",
    "/auth/register",
    "/auth/forgot-password",
    "/auth/change-password",
  ]);

  return (
    <footer
      className={classNames("py-6 text-center", className, {
        hidden:
          !isAuthPage &&
          pathShouldHidden.some((item) => pathname.includes(item)),
      })}
    >
      <small className="text-(--color-copyright) text-sm">
        &copy; 2025 NGUYEN HOANG ANH TUAN
      </small>
    </footer>
  );
}
