"use client";
import classNames from "classnames";
import Image from "next/image";
import { useRouter } from "next/navigation";
import React from "react";

export default function BackIcon({ className }: { className?: string }) {
  const router = useRouter();

  return (
    <Image
      src="/svgs/arrow-left.svg"
      alt="arrow left"
      width={32}
      height={32}
      onClick={() => router.back()}
      className={classNames("cursor-pointer", className)}
    />
  );
}
