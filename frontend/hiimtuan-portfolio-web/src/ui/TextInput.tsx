import classNames from "classnames";
import React, { DetailedHTMLProps, InputHTMLAttributes } from "react";

export default function TextInput(
  props: DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > & { label?: string; classNameContainer?: string; error?: string }
) {
  const { label, classNameContainer, error, ...rest } = props;

  return (
    <div className={classNameContainer}>
      {!!label && (
        <label htmlFor={props.id} className="block text-sm text-left">
          {label}
        </label>
      )}
      <input
        {...rest}
        className={classNames(
          "w-full text-sm mt-2 rounded-lg bg-(--color-background-input) border-[1px] border-(--color-stroke) p-3.5 placeholder-(--color-placeholder)",
          props.className
        )}
      />
      {!!error && <p>{error}</p>}
    </div>
  );
}
