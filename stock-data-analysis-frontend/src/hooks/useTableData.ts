// hooks/useTableData.ts
import { useState, useEffect, useCallback } from "react";
import { FormTableApiConfig } from "../components/formTable";

export interface UseTableDataReturn<T> {
  data: T[];
  loading: boolean;
  error: Error | null;
  refetch: () => Promise<void>;
  createItem: (data: Partial<T>) => Promise<T | void>;
  updateItem: (id: string | number, data: Partial<T>) => Promise<T | void>;
  deleteItem: (id: string | number) => Promise<void>;
}

export function useTableData<T>(
  apiConfig: FormTableApiConfig<T>
): UseTableDataReturn<T> {
  const [data, setData] = useState<T[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  const fetchData = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      // 默认第一页，默认每页10条
      const result = await apiConfig.fetchData(1, 10);
      const items = result.items;
      const transformed = apiConfig.transformData
        ? apiConfig.transformData(items)
        : items;
      setData(transformed);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  }, [apiConfig]);

  const createItem = useCallback(
    async (itemData: Partial<T>) => {
      if (!apiConfig.createItem) {
        // 若無該方法，直接跳過
        return;
      }
      const newItem = await apiConfig.createItem(itemData);
      setData((prev) => [...prev, newItem]);
      return newItem;
    },
    [apiConfig]
  );

  const updateItem = useCallback(
    async (id: string | number, itemData: Partial<T>) => {
      if (!apiConfig.updateItem) {
        return;
      }
      const updated = await apiConfig.updateItem(id, itemData);
      setData((prev) =>
        prev.map((item) =>
          // 假設資料物件有 id 欄位
          (item as any).id === id ? updated : item
        )
      );
      return updated;
    },
    [apiConfig]
  );

  const deleteItem = useCallback(
    async (id: string | number) => {
      if (!apiConfig.deleteItem) {
        return;
      }
      await apiConfig.deleteItem(id);
      setData((prev) => prev.filter((item) => (item as any).id !== id));
    },
    [apiConfig]
  );

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return {
    data,
    loading,
    error,
    refetch: fetchData,
    createItem,
    updateItem,
    deleteItem,
  };
}
