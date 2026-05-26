import useSWRMutation from "swr/mutation";
import { api } from "../..";
import { IApiResponse } from "@/api/api.interface";

async function login(
  url: string,
  { arg }: { arg: { email: string; password: string } }
) {
  return api.post<
    IApiResponse<{
      token: string;
      refreshToken: string;
    }>,
    { email: string; password: string }
  >(url, arg);
}

export function useLoginMutation() {
  const { trigger } = useSWRMutation(`/auth/login`, login);

  return {
    trigger,
  };
}
