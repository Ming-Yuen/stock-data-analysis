/**
 * DynamicFormTable - 动态表单表格组件
 * 
 * 功能特性：
 * 1. 支持无限滚动加载
 * 2. 支持外部数据源和内部数据管理
 * 3. 支持自定义列渲染
 * 4. 支持只读模式和编辑模式
 * 5. 支持错误状态显示
 * 6. 支持加载状态指示
 * 
 * 使用示例：
 * 
 * // 外部数据模式（用于显示API数据）
 * const columns = [
 *   { id: "id", label: "名稱", width: 150 },
 *   { id: "jobName", label: "作業名稱", width: 200 },
 *   { id: "status", label: "狀態", width: 120 },
 *   { id: "createTime", label: "創建時間", width: 180 },
 *   { 
 *     id: "actions", 
 *     label: "操作", 
 *     width: 120,
 *     isActionColumn: true,
 *     render: (value, row, index, { launchBatchJob }) => (
 *       <IconButton onClick={() => launchBatchJob.mutate({ jobName: row.jobName })}>
 *         <PlayArrow />
 *       </IconButton>
 *     )
 *   }
 * ];
 * 
 * <DynamicFormTable
 *   title="批次作業管理"
 *   columns={columns}
 *   data={apiData}
 *   loading={isLoading}
 *   error={error}
 *   hasMore={hasMore}
 *   onLoadMore={handleLoadMore}
 *   extraRenderProps={{ launchBatchJob }}
 * />
 * 
 * // 内部数据模式（用于表单编辑）
 * <DynamicFormTable
 *   columns={columns}
 *   initialRows={initialData}
 *   onRowsChange={handleDataChange}
 * />
 */
// src/components/DynamicFormTable.tsx
import React, { useState, useEffect, useRef, useCallback } from "react";
import {
  TableContainer,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Paper,
  TextField,
  Select,
  MenuItem,
  Button,
  Box,
  Typography,
  CircularProgress,
  Alert,
} from "@mui/material";
import { Add } from "@mui/icons-material";

/* --------- 型別定義 --------- */
export interface Column {
  id: string; // 對應 row 的 key
  label: string; // 表頭文字
  type?: "text" | "number" | "select"; // 欄位輸入型態（預設 text）
  selectOptions?: { label: string; value: string | number }[]; // 下拉選單選項
  width?: number | string; // 欄寬（可選）
  render?: (value: any, row: any, index: number, extraProps?: any) => React.ReactNode; // 自定義渲染
  isActionColumn?: boolean; // 是否为操作列
}

export interface DynamicFormTableProps {
  columns: Column[]; // 欄位結構
  initialRows?: Record<string, any>[]; // 初始資料
  onRowsChange?: (rows: Record<string, any>[]) => void; // 回呼（父層取得最新資料）
  title?: string; // 表格標題
  // 滚动加载相关
  data?: Record<string, any>[]; // 外部数据
  loading?: boolean; // 加载状态
  error?: Error | null; // 错误状态
  hasMore?: boolean; // 是否还有更多数据
  onLoadMore?: () => void; // 加载更多数据的回调
  // 其他配置
  maxHeight?: number | string; // 最大高度
  enableInfiniteScroll?: boolean; // 是否启用无限滚动
  extraRenderProps?: any; // 传递给render函数的额外属性
}

/* --------- 元件實作 --------- */
const DynamicFormTable: React.FC<DynamicFormTableProps> = ({
  columns,
  initialRows = [],
  onRowsChange,
  title,
  // 滚动加载相关
  data: externalData,
  loading = false,
  error = null,
  hasMore = false,
  onLoadMore,
  // 其他配置
  enableInfiniteScroll = true,
  extraRenderProps,
}) => {
  const [rows, setRows] = useState<Record<string, any>[]>(initialRows);
  // 使用外部数据或内部数据
  const useExternalData = externalData !== undefined;
  const currentData = useExternalData ? externalData : rows;
  const currentLoading = useExternalData ? loading : false;
  const currentError = useExternalData ? error : null;

  // 无限滚动相关
  const observerRef = useRef<IntersectionObserver | null>(null);

  /* 每次 rows 變動即呼叫父層回傳最新資料 */
  useEffect(() => {
    if (!useExternalData) {
      onRowsChange?.(rows);
    }
  }, [rows, onRowsChange, useExternalData]);

  /* 无限滚动观察器 */
  const lastElementRef = useCallback((node: HTMLTableRowElement | null) => {
    if (currentLoading) return;
    
    if (observerRef.current) {
      observerRef.current.disconnect();
    }
    
    observerRef.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore && enableInfiniteScroll) {
        onLoadMore?.();
      }
    });
    
    if (node) {
      observerRef.current.observe(node);
    }
  }, [currentLoading, hasMore, enableInfiniteScroll, onLoadMore]);

  /* 單格輸入變動 */
  const handleCellChange = (rowIndex: number, columnId: string, value: any) => {
    if (useExternalData) return; // 外部数据模式下不允许编辑
    
    setRows((prev) =>
      prev.map((r, i) => (i === rowIndex ? { ...r, [columnId]: value } : r))
    );
  };

  /* 新增一列（依 columns 自動產生空值） */
  const handleAddRow = () => {
    if (useExternalData) return; // 外部数据模式下不允许添加
    
    const emptyRow: Record<string, any> = {};
    columns.forEach((col) => (emptyRow[col.id] = ""));
    setRows((prev) => [...prev, emptyRow]);
  };



  /* 渲染单元格内容 */
  const renderCell = (column: Column, row: any, rowIndex: number) => {
    // 如果有自定义渲染函数，使用它
    if (column.render) {
      return column.render(row[column.id], row, rowIndex, extraRenderProps);
    }

    // 默认渲染
    if (useExternalData) {
      // 外部数据模式：只读显示
      return (
        <Typography variant="body2" sx={{ padding: 1 }}>
          {row[column.id] || '-'}
        </Typography>
      );
    }

    // 内部数据模式：可编辑
    return column.type === "select" ? (
      <Select
        fullWidth
        size="small"
        value={row[column.id] ?? ""}
        onChange={(e) => handleCellChange(rowIndex, column.id, e.target.value)}
      >
        {column.selectOptions?.map((opt) => (
          <MenuItem key={opt.value} value={opt.value}>
            {opt.label}
          </MenuItem>
        ))}
      </Select>
    ) : (
      <TextField
        fullWidth
        size="small"
        type={column.type || "text"}
        value={row[column.id] ?? ""}
        onChange={(e) => handleCellChange(rowIndex, column.id, e.target.value)}
        variant="outlined"
      />
    );
  };

  return (
    <Box sx={{ width: "100%", height: "100%" }}>
      {title && (
        <Typography variant="h6" gutterBottom>
          {title}
        </Typography>
      )}

      {currentError && (
        <Alert severity="error" sx={{ mb: 2 }}>
          錯誤：{currentError.message}
        </Alert>
      )}

      <TableContainer 
        component={Paper} 
        sx={{ 
          boxShadow: 'none',
          border: 'none',
          borderRadius: 0,
          backgroundColor: 'background.default',
        }}
        elevation={0}
      >
        <Table 
          size="small" 
          stickyHeader
          sx={{
            '& .MuiTableCell-root': {
              borderBottom: 'none',
            },
            '& .MuiTableRow-root:not(:last-child) .MuiTableCell-root': {
              borderBottom: '1px solid #e0e0e0',
            },
            '& .MuiTableHead-root .MuiTableCell-root': {
              borderBottom: '2px solid #1976d2',
            },
            '& .MuiTableRow-root:last-child .MuiTableCell-root': {
              borderBottom: 'none',
            },
            '& .MuiTable-root': {
              borderCollapse: 'separate',
            },
          }}
        >
          <TableHead>
            <TableRow>
              {columns.map((col) => (
                <TableCell 
                  key={col.id} 
                  sx={{ 
                    width: col.width,
                    fontWeight: 'bold',
                    backgroundColor: 'primary.main',
                    color: 'primary.contrastText'
                  }}
                >
                  {col.label}
                </TableCell>
              ))}

            </TableRow>
          </TableHead>

          <TableBody>
            {currentData.map((row, rowIndex) => (
              <TableRow 
                key={rowIndex}
                ref={rowIndex === currentData.length - 1 ? lastElementRef : null}
                sx={{ 
                  '&:hover': { backgroundColor: '#f5f5f5' }
                }}
              >
                {columns.map((col) => (
                  <TableCell key={col.id}>
                    {renderCell(col, row, rowIndex)}
                  </TableCell>
                ))}

              </TableRow>
            ))}

            {/* 加载更多指示器 */}
            {enableInfiniteScroll && hasMore && (
              <TableRow>
                <TableCell colSpan={columns.length + 1} align="center">
                  <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', py: 2 }}>
                    {currentLoading ? (
                      <>
                        <CircularProgress size={20} sx={{ mr: 1 }} />
                        <Typography variant="body2">載入中...</Typography>
                      </>
                    ) : (
                      <Typography variant="body2" color="text.secondary">
                        滾動加載更多
                      </Typography>
                    )}
                  </Box>
                </TableCell>
              </TableRow>
            )}

            {/* 新增列按鈕（仅在内部数据模式下显示） */}
            {!useExternalData && (
              <TableRow>
                <TableCell colSpan={columns.length} align="center">
                  <Button 
                    startIcon={<Add />} 
                    onClick={handleAddRow}
                    variant="outlined"
                    size="small"
                  >
                    新增列
                  </Button>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default DynamicFormTable;
