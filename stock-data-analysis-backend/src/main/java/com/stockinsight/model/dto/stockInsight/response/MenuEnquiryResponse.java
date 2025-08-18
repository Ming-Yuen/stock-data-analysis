package com.stockinsight.model.dto.stockInsight.response;

import com.ykm.common.common_lib.model.ApiResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuEnquiryResponse extends ApiResponse {

    private List<MenuTree> menuTrees;

    @Data
    public static class MenuTree{
        private String menuId;
        private String parentId;
        private String name;
        private String icon;
        private String path;
        private List<MenuTree> children = new ArrayList<>();
    }
}
