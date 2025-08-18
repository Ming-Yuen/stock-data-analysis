// src/components/FormDialog.tsx
import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  MenuItem,
  Box,
  FormControlLabel,
  Checkbox,
  IconButton,
} from "@mui/material";
import { Close } from "@mui/icons-material";
import { FormField } from "./formTable";

interface FormDialogProps<T> {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: Partial<T>) => void;
  fields: FormField<T>[];
  initialData?: T | null;
  title: string;
}

export function FormDialog<T extends Record<string, any>>({
  open,
  onClose,
  onSubmit,
  fields,
  initialData,
  title,
}: FormDialogProps<T>) {
  const [formData, setFormData] = useState<Partial<T>>({});
  const [errors, setErrors] = useState<Partial<Record<keyof T, string>>>({});

  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    } else {
      const resetData: Partial<T> = {};
      fields.forEach((field) => {
        resetData[field.key] =
          field.type === "checkbox" ? (false as any) : ("" as any);
      });
      setFormData(resetData);
    }
    setErrors({});
  }, [initialData, fields, open]);

  const handleChange = (field: keyof T, value: any) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) setErrors((prev) => ({ ...prev, [field]: undefined }));
  };

  const validateForm = () => {
    const newErrors: Partial<Record<keyof T, string>> = {};
    fields.forEach((field) => {
      const value = formData[field.key];
      if (
        field.required &&
        (value === undefined || value === "" || value === null)
      ) {
        newErrors[field.key] = `${field.label} 為必填項`;
      }
      if (field.validation && value !== undefined && value !== "") {
        const err = field.validation(value);
        if (err) newErrors[field.key] = err;
      }
    });
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateForm()) onSubmit(formData);
  };

  const renderField = (field: FormField<T>) => {
    const value = formData[field.key];
    const error = errors[field.key];

    if (field.type === "checkbox") {
      return (
        <FormControlLabel
          key={String(field.key)}
          sx={{
            m: 0,
            width: "100%",
            flex: 1,
            height: "100%",
            display: "flex",
            alignItems: "center",
          }}
          control={
            <Checkbox
              sx={{ p: 0 }}
              checked={Boolean(value)}
              onChange={(e) => handleChange(field.key, e.target.checked)}
            />
          }
          label={field.label}
        />
      );
    }

    const commonProps = {
      key: String(field.key),
      label: field.label,
      value: value ?? "",
      onChange: (e: any) => handleChange(field.key, e.target.value),
      error: !!error,
      helperText: error,
      required: field.required,
      fullWidth: true,
      margin: "dense" as const,
      sx: {
        flex: 1,
        width: "100%",
        height: "100%",
        "& .MuiInputBase-root": { height: "100%" },
        "& .MuiInputBase-multiline": {
          height: "100%",
          alignItems: "flex-start",
        },
      },
    };

    if (field.type === "select") {
      return (
        <TextField select {...commonProps}>
          {field.options?.map((opt) => (
            <MenuItem key={opt.value} value={opt.value}>
              {opt.label}
            </MenuItem>
          ))}
        </TextField>
      );
    }

    return <TextField {...commonProps} type={field.type} />;
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      fullWidth
      maxWidth={false}
      PaperProps={{
        sx: {
          display: "flex",
          flexDirection: "column",
          height: "100vh",
          width: "100vw",
          maxHeight: "none",
          maxWidth: "none",
        },
      }}
    >
      <DialogTitle sx={{ p: 1, m: 0, flexShrink: 0 }}>
        {title}
        <IconButton
          onClick={onClose}
          sx={{ p: 0, position: "absolute", right: 8, top: 8 }}
        >
          <Close />
        </IconButton>
      </DialogTitle>

      <form
        onSubmit={handleSubmit}
        style={{
          display: "flex",
          flexDirection: "column",
          flex: 1,
          height: "100%",
        }}
      >
        <DialogContent
          sx={{
            p: 0,
            flex: 1,
            display: "flex",
            flexDirection: "column",
            overflow: "hidden", // 禁止滚动
            height: "100%",
          }}
        >
          <Box
            sx={{
              p: 2, // 添加内边距
              m: 0,
              flex: 1,
              display: "flex",
              flexDirection: "column",
              gap: 2,
              overflow: "hidden", // 禁止滚动
              height: "100%",
              boxSizing: "border-box",
            }}
          >
            {fields.map((field) => (
              <Box
                key={String(field.key)}
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  flex: 1,
                  minHeight: 70,
                  height: "100%",
                }}
              >
                {renderField(field)}
              </Box>
            ))}
          </Box>
        </DialogContent>

        <DialogActions sx={{ p: 1, flexShrink: 0 }}>
          <Button onClick={onClose} sx={{ p: 0, minWidth: 64 }}>
            取消
          </Button>
          <Button type="submit" variant="contained" sx={{ p: 0, minWidth: 64 }}>
            {initialData ? "更新" : "創建"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
