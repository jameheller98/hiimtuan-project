import { create } from "zustand";
import { devtools, persist } from "zustand/middleware";
import { createTokenSlice, TokenSlice } from "./createTokenSlice";

interface AuthState extends TokenSlice {
  clearAll: () => void;
}

export const useAuthStore = create<AuthState>()(
  devtools(
    persist(
      (set, get, opts) => ({
        ...createTokenSlice(set, get, opts),
        clearAll: () => {
          get().clearAllToken();
        },
      }),
      { name: "authStore" }
    )
  )
);
