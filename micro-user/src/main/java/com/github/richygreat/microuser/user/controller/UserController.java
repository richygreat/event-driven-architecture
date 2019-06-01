package com.github.richygreat.microuser.user.controller;

import com.github.richygreat.microuser.user.model.UserDTO;
import com.github.richygreat.microuser.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/createuser")
    public UserDTO createUser(@RequestParam("email") String email, @RequestParam("taxid") String taxId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setTaxId(taxId);
        userService.createUser(userDTO);
        log.info("createUser: Streamed userDTO: {}", userDTO);
        return userDTO;
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable("id") String id) {
        log.info("getUser: Entering id: {}", id);
        return userService.getUser(id);
    }
}
