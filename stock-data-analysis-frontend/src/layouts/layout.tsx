// src/components/Layout.tsx
import React, { useState } from "react";
import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Collapse,
  Box,
  Icon,
} from "@mui/material";
import {
  ExpandLess,
  ExpandMore,
  FolderOpen,
  Folder,
  Description,
} from "@mui/icons-material";
import { Outlet, useNavigate } from "react-router-dom";
import { MenuTree } from "../services/types/menu";

const DRAWER_WIDTH = 240;

interface LayoutProps {
  menuData: MenuTree[];
}

const Layout: React.FC<LayoutProps> = ({ menuData }) => {
  const [openItems, setOpenItems] = useState<Record<string, boolean>>({});
  const navigate = useNavigate();

  const handleToggle = (id: string) =>
    setOpenItems((prev) => ({ ...prev, [id]: !prev[id] }));

  const renderMenu = (menus: MenuTree[], level = 0) =>
    menus.map((item) => {
      const hasChildren = !!item.children?.length;
      const isOpen = openItems[item.menuId] || false;
      return (
        <React.Fragment key={item.menuId}>
          <ListItem disablePadding sx={{ pl: 2 + level * 2 }}>
            <ListItemButton
              onClick={() =>
                hasChildren ? handleToggle(item.menuId) : navigate(item.path)
              }
            >
              <ListItemIcon>
                {item.icon ? (
                  <Icon>{item.icon}</Icon>
                ) : hasChildren ? (
                  isOpen ? (
                    <FolderOpen />
                  ) : (
                    <Folder />
                  )
                ) : (
                  <Description />
                )}
              </ListItemIcon>
              <ListItemText primary={item.name} />
              {hasChildren && (isOpen ? <ExpandLess /> : <ExpandMore />)}
            </ListItemButton>
          </ListItem>
          {hasChildren && (
            <Collapse in={isOpen} timeout="auto" unmountOnExit>
              <List disablePadding>
                {renderMenu(item.children!, level + 1)}
              </List>
            </Collapse>
          )}
        </React.Fragment>
      );
    });

  return (
    <Box sx={{ display: "flex", position: 'relative' }}>
      <Drawer
        variant="permanent"
        anchor="left"
        sx={{
          width: DRAWER_WIDTH,
          flexShrink: 0,
          "& .MuiDrawer-paper": {
            width: DRAWER_WIDTH,
            boxSizing: "border-box",
          },
        }}
      >
        <List>{renderMenu(menuData)}</List>
      </Drawer>

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          display: "flex",
          flexDirection: "column",
          overflow: "auto", // 只在内容超出时滚动
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            minHeight: 0,
          }}
        >
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
};

export default Layout;
