package com.example.horoscope.controller;

import com.example.horoscope.model.HoroscopeResult;
import com.example.horoscope.service.HoroscopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class HoroscopeController {

    @Autowired
    private HoroscopeService horoscopeService;

    @GetMapping("/")
    public String index(Model model) {
        List<HoroscopeResult> horoscopes = horoscopeService.getDailyHoroscope();
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
        String todayStr = today.format(formatter);
        
        model.addAttribute("horoscopes", horoscopes);
        model.addAttribute("today", todayStr);
        
        return "index";
    }
}