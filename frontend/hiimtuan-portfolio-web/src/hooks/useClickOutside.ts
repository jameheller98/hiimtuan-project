import { RefObject, useEffect } from "react";

/**
 * Hook: Runs the callback when a click happens outside the given ref element
 * @param {object} ref - React ref object (e.g., from useRef)
 * @param {function} callback - Function to run on outside click
 */
export default function useClickOutside<T extends HTMLElement>(
  ref: RefObject<T | null>,
  callback: (event: MouseEvent | TouchEvent) => void,
  excludeRefs: RefObject<HTMLElement | null>[] = []
) {
  useEffect(() => {
    function handleClick(event: MouseEvent | TouchEvent) {
      const target = event.target as Node;

      const clickedInside = ref.current && ref.current?.contains(target);

      const clickedExcluded = excludeRefs.some(
        (excludeRef) =>
          excludeRef.current && excludeRef.current?.contains(target)
      );

      if (!clickedInside && !clickedExcluded) {
        callback(event);
      }
    }

    document.addEventListener("mousedown", handleClick);
    document.addEventListener("touchstart", handleClick);
    return () => {
      document.removeEventListener("mousedown", handleClick);
      document.removeEventListener("touchstart", handleClick);
    };
  }, [ref, excludeRefs, callback]);
}
