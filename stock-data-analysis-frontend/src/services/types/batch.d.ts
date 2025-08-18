import { ApiResponse } from "../../hooks/api";

export interface LaunchBatchJobRequest<T> {
  jobName: string;
  config: T;
}

export interface EnquiryBatchJobRequest extends Record<string, unknown> {
  jobName: string;
  page?: number;
  pageSize?: number;
}

export interface BatchJob {
  jobName: string;
  jobParams: { [key: string]: any };
  isActive: boolean;
  createTime: string;
}

export interface EnquiryBatchJobResponse extends ApiResponse {
  batchJobList: BatchJob[];
  total: number;
  page: number;
  pageSize: number;
}
