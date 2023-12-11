package com.pumppals.pumppalsapi.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RouteController {

    @GetMapping("/user/**")
    public RedirectView indexRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/forum")
    public RedirectView adminRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/profile")
    public RedirectView loginRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/login")
    public RedirectView registerRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/create-account")
    public RedirectView logoutRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/edit-profile")
    public RedirectView forumRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/create-post")
    public RedirectView profileRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/log-workout")
    public RedirectView createPostRedirect() {
        return new RedirectView("/");
    }

    @GetMapping("/about")
    public RedirectView logWorkoutRedirect() {
        return new RedirectView("/");
    }

}