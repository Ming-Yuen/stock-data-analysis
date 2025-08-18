package com.stockinsight.controller;

import com.stockinsight.model.dto.stockInsight.response.MenuEnquiryResponse;
import com.stockinsight.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("menu")
public class MenuController {
    private MenuService menuService;
    @PostMapping("enquiry")
    public MenuEnquiryResponse enquiry(){
        return menuService.enquiry();
    }
}
