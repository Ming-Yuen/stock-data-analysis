package com.stockinsight.converter;

import com.stockinsight.model.entity.Menu;
import com.stockinsight.model.dto.stockInsight.response.MenuEnquiryResponse;
import io.micrometer.common.util.StringUtils;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface MenuConverter {

    MenuEnquiryResponse.MenuTree menuToMenuTree(Menu menu);

    default MenuEnquiryResponse mapMenuListToResponse(List<Menu> menuList) {
        if (menuList == null || menuList.isEmpty()) {
            return new MenuEnquiryResponse();
        }

        Map<String, MenuEnquiryResponse.MenuTree> menuMap = new HashMap<>();

        List<MenuEnquiryResponse.MenuTree> rootMenuList = new ArrayList<>();

        Map<String, List<MenuEnquiryResponse.MenuTree>> childrenMenuMap = new HashMap<>();
        for(Menu menu : menuList){
            MenuEnquiryResponse.MenuTree menuTree = menuToMenuTree(menu);
            if(menu.getParentId() == null){
                rootMenuList.add(menuTree);
            }else{
                MenuEnquiryResponse.MenuTree parentMenuTree = menuMap.get(menuTree.getParentId());
                if(parentMenuTree != null){
                    parentMenuTree.getChildren().add(menuTree);
                }else{
                    childrenMenuMap.computeIfAbsent(menuTree.getParentId(), v -> new ArrayList<>()).add(menuTree);
                }
            }

            List<MenuEnquiryResponse.MenuTree> childrenMenuList = childrenMenuMap.get(menu.getMenuId());
            if(childrenMenuList != null){
                menuTree.setChildren(childrenMenuList);
                childrenMenuMap.remove(menu.getMenuId());
            }
            menuMap.put(menuTree.getMenuId(), menuTree);
        }

        MenuEnquiryResponse response = new MenuEnquiryResponse();
        response.setMenuTrees(rootMenuList);
        return response;
    }
}
