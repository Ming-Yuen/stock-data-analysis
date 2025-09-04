// pages/batchJobMainance.tsx
import React, { useState, useCallback, useEffect } from "react";
import { Box, IconButton, Tooltip } from "@mui/material";
import { PlayArrow } from "@mui/icons-material";
import DynamicFormTable, { Column } from "../components/DynamicFormTable";
import { useFetch, useMutate } from "../hooks/useApi";
import { ApiResponse } from "../hooks/api";
import { Job, EnquiryJobResponse } from "../services/types/batch";
import { MenuTree } from "../services/types/menu";
import { apiConfig } from "../apiConfig";


const columns: Column[] = [
  { id: "jobName", label: "作業名稱", width: 200 },
  { 
    id: "enabled", 
    label: "狀態", 
    width: 120,
    render: (value, row, index) => {
      // 自定义渲染状态字段
      if (value === true || value === 'true') {
        return <span style={{ color: 'green' }}>啟用</span>;
      } else if (value === false || value === 'false') {
        return <span style={{ color: 'red' }}>停用</span>;
      }
      return <span style={{ color: 'gray' }}>{value || '-'}</span>;
    }
  },
  { 
    id: "actions", 
    label: "操作", 
    width: 120,
    isActionColumn: true,
    render: (value, row, index, { launchBatchJob }) => (
      <Tooltip title="启动">
        <IconButton
          color="primary"
          size="small"
          onClick={() => launchBatchJob.mutate({ jobName: row.jobName , jobParams: row.jobParams, taskGroup: row.taskGroup})}
          disabled={launchBatchJob.isPending}
        >
          <PlayArrow />
        </IconButton>
      </Tooltip>
    )
  }
];

interface JobManagementPageProps {
  menuTree: MenuTree;
}

export function JobManagementPage({ menuTree }: JobManagementPageProps) {
  // 分页状态
  const [page, setPage] = useState(1);
  const [pageSize] = useState(10);
  const [allData, setAllData] = useState<Job[]>([]);
  const [hasMore, setHasMore] = useState(true);

  // 使用 useFetch 获取数据（React Query 缓存和状态管理）
  const { data, isLoading, isError, error } = useFetch<EnquiryJobResponse>(
    apiConfig.getJobList,
    { page, pageSize }
  );

  // 启动作业的 mutation
  const launchBatchJob = useMutate<ApiResponse>(apiConfig.launchJobList);

  // 处理数据更新
  useEffect(() => {
    if (data?.jobTaskList) {
      if (page === 1) {
        // 第一页：重置数据
        setAllData(data.jobTaskList);
      } else {
        // 后续页：追加数据
        setAllData(prev => [...prev, ...data.jobTaskList]);
      }
      
      // 判断是否还有更多数据
      const total = data.total || 0;
      const currentTotal = (page - 1) * pageSize + data.jobTaskList.length;
      setHasMore(currentTotal < total);
    }
  }, [data, page, pageSize]);

  // 加载更多数据
  const handleLoadMore = useCallback(() => {
    if (!isLoading && hasMore) {
      setPage(prev => prev + 1);
    }
  }, [isLoading, hasMore]);

  return (
    <Box
      sx={{
        // height: "100%", // 移除这一行
        width: "100%",
        display: "flex",
        flexDirection: "column",
        minWidth: 0, // 修复 flex 容器最小宽度问题
        minHeight: 0, // 修复 flex 容器最小高度问题
        // flex: 1, // 移除这一行
      }}
    >
      <DynamicFormTable
        title={menuTree.name}
        columns={columns}
        // 外部数据模式
        data={allData}
        loading={isLoading}
        error={isError ? error : null}
        hasMore={hasMore}
        onLoadMore={handleLoadMore}
        // 其他配置
        enableInfiniteScroll={true}
        extraRenderProps={{ launchBatchJob }}
      />
    </Box>
  );
}
