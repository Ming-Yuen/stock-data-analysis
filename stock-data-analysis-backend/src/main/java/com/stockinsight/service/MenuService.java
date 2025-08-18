package com.stockinsight.service;

import com.stockinsight.converter.MenuConverter;
import com.stockinsight.repository.MenuRepository;
import com.stockinsight.model.entity.Menu;
import com.stockinsight.model.dto.stockInsight.response.MenuEnquiryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MenuService {
    private MenuRepository menuRepository;
    private MenuConverter menuConverter;
    public MenuEnquiryResponse enquiry() {
        List<Menu> menuList = menuRepository.findByIsDeleted(0);
        return menuConverter.mapMenuListToResponse(menuList);
    }
}
