"use client";

import classNames from "classnames";
import Image from "next/image";
import React, { DetailedHTMLProps, InputHTMLAttributes, useState } from "react";

export default function TextInput(
  props: DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > & { label?: string; classNameContainer?: string; error?: string }
) {
  const { label, classNameContainer, error, type, placeholder, ...rest } =
    props;
  const [passwordHidden, setPasswordHidden] = useState(true);

  return (
    <div className={classNameContainer}>
      {!!label && (
        <label htmlFor={props.id} className="block text-sm text-left">
          {label}
        </label>
      )}
      <div
        className={classNames(
          "flex items-center w-full text-sm mt-2 rounded-lg bg-(--color-background-input) border-[1px] border-(--color-stroke) p-3.5 placeholder-(--color-placeholder) gap-2",
          !!error && "border-red-300",
          props.className
        )}
      >
        <input
          {...rest}
          type={
            type === "password" ? (passwordHidden ? "password" : "text") : type
          }
          placeholder={
            type === "password" && passwordHidden
              ? placeholder?.replaceAll("e", "□")
              : placeholder
          }
          className="w-full focus:outline-none"
        />
        {type === "password" && (
          <Image
            src={passwordHidden ? "/svgs/eye-slash.svg" : "/svgs/eye.svg"}
            alt={passwordHidden ? "eye-slash" : "eye"}
            width={24}
            height={24}
            onClick={() => {
              setPasswordHidden((hidden) => {
                return !hidden;
              });
            }}
          />
        )}
      </div>
      {!!error && <p className="text-xs text-red-500 mt-1">{error}</p>}
    </div>
  );
}
