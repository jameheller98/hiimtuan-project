import useSWRMutation from "swr/mutation";
import { api } from "../..";
import { IApiResponse } from "@/api/api.interface";

async function resetPassword(url: string, { arg }: { arg: { email: string } }) {
  return api.post<IApiResponse<null>, { email: string }>(url, arg);
}

export function useResetPasswordMutation() {
  const { trigger } = useSWRMutation(`/auth/reset-password`, resetPassword);

  return {
    trigger,
  };
}
