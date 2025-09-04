export interface ApiEndpoint {
  method: "get" | "post" | "put" | "delete";
  url: string;
}

type ApiKey = "getMenu" | "getJobList" | "launchJobList";

// Step 2：编写配置并用 satisfies 验证
export const apiConfig = {
  getMenu: {
    method: "post",
    url: process.env.REACT_APP_MENU_ENQUIRY!,
  },
  getJobList: {
    method: "post",
    url: process.env.REACT_APP_JOB_ENQUIRY!,
  },
  launchJobList: {
    method: "post",
    url: process.env.REACT_APP_JOB_LAUNCH!,
  },
} satisfies Record<ApiKey, ApiEndpoint>;

// Step 3：封装调用函数
export function callApi<K extends ApiKey>(
  key: K,
  payload?: unknown
): Promise<unknown> {
  const { method, url } = apiConfig[key];
  return fetch(url, { method, body: JSON.stringify(payload) }).then((res) =>
    res.json()
  );
}
