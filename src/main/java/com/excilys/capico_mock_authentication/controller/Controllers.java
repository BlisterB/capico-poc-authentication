package com.excilys.capico_mock_authentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Controllers {

    @RequestMapping("/")
    public ModelAndView indexPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        return model;
    }

    @RequestMapping("/api/token")
    public ModelAndView tokenPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("token");
        return model;
    }

    @RequestMapping("/http-basic")
    public ModelAndView httpBasicPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("http-basic");
        return model;
    }
}
