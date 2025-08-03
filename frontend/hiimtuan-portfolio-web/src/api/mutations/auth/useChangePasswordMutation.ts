import useSWRMutation from "swr/mutation";
import { api } from "../..";
import { IApiResponse } from "@/api/api.interface";

async function changePassword(
  url: string,
  { arg }: { arg: { password: string; passwordResetToken: string } }
) {
  return api.post<
    IApiResponse<null>,
    { password: string; passwordResetToken: string }
  >(url, arg);
}

export function useChangePasswordMutation() {
  const { trigger } = useSWRMutation(`/auth/change-password`, changePassword);

  return {
    trigger,
  };
}
