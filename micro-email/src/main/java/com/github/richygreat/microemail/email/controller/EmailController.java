package com.github.richygreat.microemail.email.controller;

import com.github.richygreat.microemail.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/emails/{id}/click")
    public void click(@PathVariable("id") String id) {
        log.info("click: Entering id: {}", id);
        emailService.handleLinkClick(id);
    }
}
