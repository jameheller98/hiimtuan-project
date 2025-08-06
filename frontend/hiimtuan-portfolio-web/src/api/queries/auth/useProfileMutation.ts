import { IApiResponse } from "@/api/api.interface";
import useSWR from "swr";
import { api } from "../..";

async function profile(url: string) {
  return api.get<IApiResponse<unknown>, unknown>(url);
}

export function useProfileQuery() {
  const { data, error, isLoading } = useSWR("/user/me", profile);

  return {
    profile: data,
    error,
    isLoading,
  };
}
