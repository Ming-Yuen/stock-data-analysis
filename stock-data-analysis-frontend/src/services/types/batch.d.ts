import { ApiResponse } from "../../hooks/api";

export interface LaunchJobRequest<T> {
  jobName: string;
  config: T;
}

export interface EnquiryJobRequest extends Record<string, unknown> {
  jobName: string;
  page?: number;
  pageSize?: number;
}

export type TaskGroup = 
  | 'STOCK'
  | 'STOCK_SCHEDULE' 
  | 'STOCK_REPORT'
  | 'REPORT_SCHEDULE'
  | 'MAINTENANCE_SCHEDULE'
  | 'DEFAULT';

export interface Job {
  jobName: string;
  taskGroup: TaskGroup;
  jobParams: { [key: string]: any };
  enabled: boolean;
  createTime: string;
}

export interface EnquiryJobResponse extends ApiResponse {
  jobTaskList: Job[];
  total: number;
  page: number;
  pageSize: number;
}
