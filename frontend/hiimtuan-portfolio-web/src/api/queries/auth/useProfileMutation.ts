import { IApiResponse } from "@/api/api.interface";
import useSWR from "swr";
import { api } from "../..";

export type TRole = "USER" | "ADMIN" | "SUPER_ADMIN";

export interface IProfile {
  id: number;
  email: string;
  fullName: string;
  roles: Array<TRole>;
}

async function profile(url: string) {
  return api.get<IApiResponse<IProfile>, unknown>(url);
}

export function useProfileQuery() {
  const { data, error, isLoading } = useSWR("/user/me", profile);

  return {
    profile: data,
    error,
    isLoading,
  };
}
