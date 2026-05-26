export type TVersion = "v1";

export interface IApiResponse<T> {
  data: T;
  message: string;
  status: number;
}
