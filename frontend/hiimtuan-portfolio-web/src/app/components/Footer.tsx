"use client";
import classNames from "classnames";
import { usePathname } from "next/navigation";
import React from "react";

export default function Footer({
  className,
  isLoginPage = false,
}: {
  className?: string;
  isLoginPage?: boolean;
}) {
  const pathname = usePathname();

  return (
    <footer
      className={classNames("py-6 text-center", className, {
        hidden: !isLoginPage && pathname.includes("/login"),
      })}
    >
      <small className="text-(--color-copyright) text-sm">
        &copy; 2025 NGUYEN HOANG ANH TUAN
      </small>
    </footer>
  );
}
