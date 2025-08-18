// serviceFactory.ts
import { ApiEndpoint, apiConfig } from "../apiConfig";
import { apiClient } from "./apiClient";

type Params = Record<string, string | number>;
type Data = Record<string, unknown>;

export function buildService(config: ApiEndpoint) {
  // 加上 export
  return async (params: Params = {}, data: Data = {}) => {
    let path = config.url;
    Object.entries(params).forEach(([key, val]) => {
      path = path.replace(`:${key}`, String(val));
    });
    const response = await apiClient.request({
      method: config.method,
      url: path,
      params: config.method === "get" ? params : undefined,
      data: config.method !== "get" ? data : undefined,
    });
    return response.data;
  };
}

export const service = Object.fromEntries(
  Object.entries(apiConfig).map(([key, cfg]) => [key, buildService(cfg)])
) as Record<keyof typeof apiConfig, (p?: Params, d?: Data) => Promise<any>>;
