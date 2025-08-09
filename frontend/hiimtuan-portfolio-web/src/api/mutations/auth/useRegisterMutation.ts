import useSWRMutation from "swr/mutation";
import { api } from "../..";
import { IApiResponse } from "@/api/api.interface";

async function register(
  url: string,
  { arg }: { arg: { fullName: string; email: string; password: string } }
) {
  return api.post<IApiResponse<string>, { email: string; password: string }>(
    url,
    arg
  );
}

export function useRegisterMutation() {
  const { trigger } = useSWRMutation(`/auth/register`, register);

  return {
    trigger,
  };
}
