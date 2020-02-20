package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginResource {


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ModelAttribute("ROLE")
    public String message(@AuthenticationPrincipal User customUserDetails) {
        return customUserDetails.getUserRole().getRoleName();
    }

}
