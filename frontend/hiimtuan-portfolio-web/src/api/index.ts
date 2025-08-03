import axios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  CreateAxiosDefaults,
} from "axios";
import { TVersion } from "./api.interface";

const defaultConfig = {
  baseURL: "http://localhost:8765/api",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
};

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
      (error) => {
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
