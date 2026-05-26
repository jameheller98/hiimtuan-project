import axios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  CreateAxiosDefaults,
} from "axios";
import { IApiResponse, TVersion } from "./api.interface";
import { useAuthStore } from "@/stores";

const defaultConfig = {
  baseURL: "http://localhost:8765/api",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
};

let isRefreshToken = false;

class API {
  private axiosInstance: AxiosInstance;

  constructor(config: CreateAxiosDefaults = defaultConfig) {
    this.axiosInstance = axios.create(config);

    this.axiosInstance.interceptors.request.use(
      (config) => {
        config.baseURL =
          config.baseURL +
          (config.params?.version ? `/${config.params?.version}` : "/v1");

        delete config.params?.version;

        const authStore = useAuthStore.getState();

        if (!!authStore.token) {
          config.headers["Authorization"] = "Bearer " + authStore.token;
        }

        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    this.axiosInstance.interceptors.response.use(
      (response) => {
        return response.data;
      },
      async (error) => {
        const { response, config } = error;
        const status = response?.status;
        const authStore = useAuthStore.getState();

        if (status === 406) {
          authStore.clearAll();
          window.location.href = "/auth/login";
        }

        if (
          status === 401 &&
          !!authStore.token &&
          !!authStore.refreshToken &&
          !isRefreshToken
        ) {
          isRefreshToken = true;

          try {
            const response: IApiResponse<{
              token: string;
              refreshToken: string;
            }> = await this.post("/auth/refresh", {
              refreshToken: authStore.refreshToken,
            });

            if (response?.status === 200) {
              authStore.setAllToken(
                response.data.token,
                response.data.refreshToken
              );

              config.baseURL = defaultConfig.baseURL;

              return this.axiosInstance(config);
            }
          } catch (refreshError) {
            console.log("Token refresh failed:", refreshError);
            authStore.clearAll();
            window.location.href = "/auth/login";
            return Promise.reject(refreshError);
          } finally {
            isRefreshToken = false;
          }
        }

        return Promise.reject(error);
      }
    );
  }

  get<T, D>(
    url: string,
    requestConfig?: AxiosRequestConfig<D> & { version: TVersion }
  ) {
    const newRequestConfig = this.adapterVersionToParams<D>(requestConfig);

    return this.axiosInstance.get<T, AxiosResponse<T>["data"]>(
      url,
      newRequestConfig
    );
  }

  post<T, D>(
    url: string,
    data: D,
    requestConfig?: AxiosRequestConfig<D> & { version: TVersion }
  ) {
    const newRequestConfig = this.adapterVersionToParams<D>(requestConfig);

    return this.axiosInstance.post<T, AxiosResponse<T>["data"], D>(
      url,
      data,
      newRequestConfig
    );
  }

  private adapterVersionToParams<T>(
    requestConfig?: AxiosRequestConfig<T> & { version: TVersion }
  ): AxiosRequestConfig<T> | undefined {
    if (!requestConfig) return undefined;

    const { version, ...requestConfigRest } = requestConfig;

    requestConfigRest.params = Object.assign({}, requestConfigRest.params, {
      version,
    });

    return requestConfigRest;
  }
}

export const api = new API();
