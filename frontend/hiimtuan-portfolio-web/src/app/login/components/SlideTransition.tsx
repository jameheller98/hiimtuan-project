"use client";

import { animated, easings, useSpring } from "@react-spring/web";
import { ReactNode } from "react";

export default function SlideTransition({
  children,
  position,
  delay = 0,
  className,
}: {
  children?: ReactNode;
  position?: "left" | "right" | "down";
  delay?: number;
  className?: string;
}) {
  const springs = useSpring({
    from: {
      x: position === "right" ? 100 : position === "left" ? -100 : 0,
      y: position === "down" ? 100 : 0,
      opacity: 0,
    },
    to: { x: 0, y: 0, opacity: 1 },
    delay,
    config: { easing: easings.easeOutCubic, duration: 750 },
  });

  return (
    <animated.div className={className} style={{ ...springs }}>
      {children}
    </animated.div>
  );
}
