import {
  UseQueryResult,
  useQuery,
  UseMutationResult,
  useMutation,
} from "@tanstack/react-query";
import { buildService } from "../services/serviceFactory";
import { ApiEndpoint } from "../apiConfig";

export function useFetch<T = any>(
  endpoint: ApiEndpoint,
  params?: Record<string, any>
): UseQueryResult<T> {
  return useQuery<T>({
    queryKey: [endpoint.url, endpoint.method, params],
    queryFn: () => {
      // 直接調用 buildService 建構的函數
      const serviceFunction = buildService(endpoint);
      return serviceFunction(params);
    },
  });
}

export function useMutate<
  TResponse = any,
  TData extends Record<string, unknown> = Record<string, unknown>
>(
  endpoint: ApiEndpoint,
  params?: Record<string, any>
): UseMutationResult<TResponse, unknown, TData> {
  return useMutation({
    mutationFn: (data: TData) => buildService(endpoint)(params, data),
  });
}
