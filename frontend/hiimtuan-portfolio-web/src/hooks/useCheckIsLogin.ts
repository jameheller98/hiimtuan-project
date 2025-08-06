import { useAuthStore } from "@/stores";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function useCheckIsLogin() {
  const router = useRouter();
  const { token } = useAuthStore();

  useEffect(() => {
    if (token) {
      router.replace("/");
    }
  }, [token, router]);
}
