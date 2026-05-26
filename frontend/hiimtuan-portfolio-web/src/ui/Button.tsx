"use client";
import { animated, useSpring } from "@react-spring/web";
import classNames from "classnames";
import {
  ButtonHTMLAttributes,
  DetailedHTMLProps,
  MouseEventHandler,
} from "react";

export default function Button(
  props: DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > & { variant?: "fill" | "text" }
) {
  const { variant = "fill", onClick, className, ...rest } = props;
  const [springs, api] = useSpring(() => ({
    from: { y: 0, x: 0, scale: 1 },
    config: {
      duration: 150,
    },
  }));

  const handleClick: MouseEventHandler<HTMLButtonElement> = (e) => {
    onClick?.(e);
    api.start({
      to: [
        {
          y: -3,
          x: -3,
          scale: 0.99,
        },
        {
          y: 0,
          x: 0,
          scale: 1,
        },
      ],

      reset: true,
      reverse: true,
    });
  };

  return (
    <animated.button
      {...rest}
      className={classNames(
        {
          "bg-(--color-background-button) text-white w-full py-3.5 rounded-xl":
            variant === "fill",
        },
        className
      )}
      onClick={handleClick}
      style={{
        ...springs,
      }}
    >
      {props.children}
    </animated.button>
  );
}
