import useSWRMutation from "swr/mutation";
import { api } from "../..";
import { IApiResponse } from "@/api/api.interface";

async function logout(url: string, { arg }: { arg: undefined }) {
  return api.get<IApiResponse<string>, undefined>(url, arg);
}

export function useLogoutMutation() {
  const { trigger } = useSWRMutation(`/auth/logout`, logout);

  return {
    trigger,
  };
}
