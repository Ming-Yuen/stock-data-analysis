// src/components/FormTable.tsx
import React, { useState, useMemo, useEffect, useCallback } from "react";
import { Box, Paper, Typography, Button, Snackbar, Alert } from "@mui/material";
import {
  DataGrid,
  GridRowsProp,
  GridRowSelectionModel,
} from "@mui/x-data-grid";
import { Add, Delete } from "@mui/icons-material";
import { FormDialog } from "./FormDialog";

export interface ColumnDefinition<T, ExtraProps = any> {
  key: keyof T | string;
  label: string;
  width?: number;
  type?: "string" | "number" | "date" | "boolean";
  render?: (value: any, row: T, index?: number, extra?: ExtraProps) => React.ReactNode;
  sortable?: boolean;
  filterable?: boolean;
}

export interface FormField<T> {
  key: keyof T;
  label: string;
  type: "text" | "email" | "number" | "select" | "date" | "checkbox";
  required?: boolean;
  options?: { value: string | number; label: string }[];
  validation?: (value: any) => string | undefined;
}

export interface FormTableApiConfig<T> {
  fetchData: (page: number, pageSize: number) => Promise<{ items: T[]; hasMore: boolean }>;
  createItem?: (data: Partial<T>) => Promise<T>;
  updateItem?: (id: string | number, data: Partial<T>) => Promise<T>;
  deleteItem?: (id: string | number) => Promise<void>;
  transformData?: (data: any) => T[];
}

export interface FormTableProps<T extends Record<string, any>, ExtraProps = any> {
  title: string;
  columns: ColumnDefinition<T, ExtraProps>[];
  formFields: FormField<T>[];
  apiConfig?: Partial<FormTableApiConfig<T>>; // 改为可选
  // 新增：外部数据传递支持
  data?: T[];
  loading?: boolean;
  error?: Error | null;
  total?: number;
  page?: number;
  pageSize?: number;
  onPageChange?: (page: number) => void;
  onPageSizeChange?: (pageSize: number) => void;
  // 原有属性
  idField?: keyof T;
  enableSelection?: boolean;
  enableFilter?: boolean;
  pageSizeOptions?: number[];
  enableCreate?: boolean; // 新增: 控制是否显示新增按钮
  extraRenderProps?: ExtraProps;
}

export function FormTable<T extends Record<string, any>, ExtraProps = any>({
  title,
  columns,
  formFields,
  apiConfig,
  // 新增：外部数据传递支持
  data: externalData,
  loading: externalLoading,
  error: externalError,
  total: externalTotal,
  page: externalPage,
  pageSize: externalPageSize,
  onPageChange,
  onPageSizeChange,
  // 原有属性
  idField = "id" as keyof T,
  enableSelection = true,
  enableFilter = true,
  pageSizeOptions = [5, 10, 25, 50],
  enableCreate = true,
  extraRenderProps,
}: FormTableProps<T, ExtraProps>) {
  // 判断是否使用外部数据模式
  const useExternalData = externalData !== undefined;
  
  // 无限滚动相关状态（仅在内部数据模式下使用）
  const [data, setData] = useState<T[]>([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  // 使用外部数据或内部数据
  const currentData = useExternalData ? externalData : data;
  const currentLoading = useExternalData ? externalLoading : loading;
  const currentError = useExternalData ? externalError : error;
  const currentPage = useExternalData ? externalPage : page;
  const currentPageSize = useExternalData ? (externalPageSize ?? 10) : 10;

  // 其它状态
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<T | null>(null);
  const [selectionModel, setSelectionModel] = useState<GridRowSelectionModel>({
    type: "include",
    ids: new Set(),
  });
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success" as any,
  });

  // 新增/编辑/删除相关方法
  const canCreate = enableCreate && !!apiConfig?.createItem;
  const canUpdate = !!apiConfig?.updateItem;
  const canDelete = !!apiConfig?.deleteItem;
  const hasActions = canUpdate || canDelete;

  const createItem = apiConfig?.createItem;
  const updateItem = apiConfig?.updateItem;
  const deleteItem = apiConfig?.deleteItem;

  // 重新加载全部数据（用于新增/编辑/删除后刷新）
  const refetch = useCallback(async () => {
    if (useExternalData) {
      // 外部数据模式：通知父组件刷新
      onPageChange?.(1);
      return;
    }
    
    setData([]);
    setPage(1);
    setHasMore(true);
    setError(null);
    setLoading(true);
    try {
      const result = await apiConfig?.fetchData?.(1, currentPageSize);
      if (result) {
        setData(result.items);
        setHasMore(result.hasMore);
        setPage(2);
      }
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  }, [apiConfig, currentPageSize, useExternalData, onPageChange]);

  // 加载数据（仅在内部数据模式下使用）
  const loadMore = useCallback(async () => {
    if (useExternalData || loading || !hasMore) return;
    setLoading(true);
    setError(null);
    try {
      const result = await apiConfig?.fetchData?.(page, currentPageSize);
      if (result) {
        setData((prev) => [...prev, ...result.items]);
        setHasMore(result.hasMore);
        setPage((prev) => prev + 1);
      }
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  }, [apiConfig, page, currentPageSize, loading, hasMore, useExternalData]);

  // 首次加载（仅在内部数据模式下使用）
  useEffect(() => {
    if (useExternalData) return;
    
    setData([]);
    setPage(1);
    setHasMore(true);
  }, [apiConfig, currentPageSize, useExternalData]);
  
  useEffect(() => {
    if (useExternalData || page !== 1) return;
    loadMore();
    // eslint-disable-next-line
  }, [page, useExternalData]);

  // 无限滚动 IntersectionObserver（仅在内部数据模式下使用）
  const loaderRef = React.useRef<HTMLDivElement | null>(null);
  useEffect(() => {
    if (useExternalData || !hasMore || loading) return;
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          loadMore();
        }
      },
      { threshold: 1 }
    );
    if (loaderRef.current) {
      observer.observe(loaderRef.current);
    }
    return () => {
      if (loaderRef.current) observer.unobserve(loaderRef.current);
    };
  }, [loadMore, hasMore, loading, useExternalData]);

  const handleEdit = (item: T) => {
    if (!canUpdate) return;
    setEditingItem(item);
    setDialogOpen(true);
  };
  const handleDelete = async (item: T) => {
    if (!canDelete) return;
    if (window.confirm("確定刪除？")) {
      try {
        await deleteItem!(item[idField]);
        setSnackbar({ open: true, message: "刪除成功", severity: "success" });
        refetch();
      } catch {
        setSnackbar({ open: true, message: "刪除失敗", severity: "error" });
      }
    }
  };
  const handleAdd = () => {
    if (!canCreate) return;
    setEditingItem(null);
    setDialogOpen(true);
  };
  const handleBulkDelete = async () => {
    if (!canDelete) return;
    const ids = Array.from(selectionModel.ids);
    if (!ids.length) return;
    if (window.confirm(`確定刪除 ${ids.length} 條？`)) {
      try {
        await Promise.all(ids.map((id) => deleteItem!(id)));
        setSnackbar({
          open: true,
          message: "批量刪除成功",
          severity: "success",
        });
        setSelectionModel({ type: "include", ids: new Set() });
        refetch();
      } catch {
        setSnackbar({ open: true, message: "批量刪除失敗", severity: "error" });
      }
    }
  };
  const handleSubmit = async (formData: Partial<T>) => {
    try {
      if (editingItem && canUpdate) {
        await updateItem!(editingItem[idField], formData);
        setSnackbar({ open: true, message: "更新成功", severity: "success" });
      } else if (!editingItem && canCreate) {
        await createItem!(formData);
        setSnackbar({ open: true, message: "創建成功", severity: "success" });
      }
      setDialogOpen(false);
      setEditingItem(null);
      refetch();
    } catch {
      setSnackbar({ open: true, message: "操作失敗", severity: "error" });
    }
  };

  const gridColumns = useMemo(() => {
    return columns.map((col) => {
      const baseCol = {
        field: String(col.key),
        headerName: col.label,
        width: col.width ?? 150,
        type: col.type ?? "string",
        sortable: col.sortable !== false,
        filterable: col.filterable !== false,
      };
      if (col.render) {
        return {
          ...baseCol,
          renderCell: (params: any) => {
            const row = params.row as T;
            const value = row[col.key as keyof T];
            return col.render!(value, row, undefined, extraRenderProps);
          },
        };
      }
      return baseCol;
    });
  }, [columns, extraRenderProps]);

  const gridRows = useMemo<GridRowsProp>(
    () => currentData.map((item) => ({ ...item, id: item[idField] })),
    [currentData, idField]
  );

  // 计算 rowCount
  const rowCount = useExternalData ? (externalTotal ?? 0) : gridRows.length;

  if (currentError)
    return (
      <Box sx={{ p: 3 }}>
        <Alert severity="error">錯誤：{currentError.message}</Alert>
      </Box>
    );

  return (
    <Box sx={{
      display: "flex",
      flexDirection: "column",
      height: "100%",
      width: "100%",
      overflow: "hidden",
      minWidth: 0,
      minHeight: 0,
      flex: 1,
    }}>
      <Typography variant="h4" gutterBottom>
        {title}
      </Typography>
      {(canCreate || (canDelete && selectionModel.ids.size > 0)) && (
        <Box sx={{ mb: 2, display: "flex", gap: 2 }}>
          {canCreate && (
            <Button variant="contained" startIcon={<Add />} onClick={handleAdd}>
              新增
            </Button>
          )}
          {canDelete && selectionModel.ids.size > 0 && (
            <Button
              variant="outlined"
              color="error"
              startIcon={<Delete />}
              onClick={handleBulkDelete}
            >
              批量刪除 ({selectionModel.ids.size})
            </Button>
          )}
        </Box>
      )}
      <Paper sx={{
        flex: 1,
        width: "100%",
        display: "flex",
        minHeight: 0,
        overflow: "hidden",
        minWidth: 0,
      }}>
        <Box sx={{
          flex: 1,
          width: "100%",
          display: "flex",
          flexDirection: "column",
          minHeight: 0,
          minWidth: 0,
        }}>
          <DataGrid
            rows={gridRows}
            columns={gridColumns}
            loading={currentLoading}
            checkboxSelection={enableSelection && canDelete}
            rowSelectionModel={selectionModel}
            onRowSelectionModelChange={setSelectionModel}
            disableColumnFilter={!enableFilter}
            disableRowSelectionOnClick
            // 分页配置（仅在外部数据模式下启用）
            pagination={useExternalData || undefined}
            paginationMode={useExternalData ? "server" : "client"}
            paginationModel={useExternalData ? {
              page: currentPage ? currentPage - 1 : 0,
              pageSize: currentPageSize
            } : undefined}
            pageSizeOptions={pageSizeOptions}
            rowCount={rowCount}
            onPaginationModelChange={useExternalData ? (model) => {
              onPageChange?.(model.page + 1);
              onPageSizeChange?.(model.pageSize);
            } : undefined}
            sx={{ 
              border: 0, 
              flex: 1, 
              minHeight: 0,
              width: "100%",
              minWidth: 0,
              "& .MuiDataGrid-root": {
                width: "100%",
                minWidth: 0,
              },
              "& .MuiDataGrid-main": {
                width: "100%",
                minWidth: 0,
              },
              "& .MuiDataGrid-virtualScroller": {
                minHeight: "200px", // 防止空数据塌陷
              },
            }}
          />
          {/* 仅在内部数据模式下显示无限滚动指示器 */}
          {!useExternalData && (
            <div ref={loaderRef} style={{ height: 40 }} />
          )}
        </Box>
      </Paper>
      {currentLoading && <Typography align="center">載入中...</Typography>}
      {(canCreate || canUpdate) && (
        <FormDialog
          open={dialogOpen}
          onClose={() => setDialogOpen(false)}
          onSubmit={handleSubmit}
          fields={formFields}
          initialData={editingItem}
          title={editingItem ? "編輯記錄" : "新增記錄"}
        />
      )}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={() => setSnackbar((prev) => ({ ...prev, open: false }))}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={() => setSnackbar((prev) => ({ ...prev, open: false }))}
          severity={snackbar.severity}
          sx={{ width: "100%" }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
