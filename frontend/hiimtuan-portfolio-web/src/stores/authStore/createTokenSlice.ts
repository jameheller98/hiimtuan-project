import { StateCreator } from "zustand";

export interface TokenSlice {
  token: string;
  refreshToken: string;
  setAllToken: (token: string, refreshToken: string) => void;
  clearAllToken: () => void;
}

export const createTokenSlice: StateCreator<TokenSlice, [], [], TokenSlice> = (
  set
) => ({
  token: "",
  refreshToken: "",
  setAllToken: (token: string, refreshToken: string) =>
    set({ token, refreshToken }),
  clearAllToken: () => set({ token: "", refreshToken: "" }),
});
