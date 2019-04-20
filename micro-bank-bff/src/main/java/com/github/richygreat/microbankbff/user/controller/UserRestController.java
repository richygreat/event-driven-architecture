package com.github.richygreat.microbankbff.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.richygreat.microbankbff.user.model.UserDTO;
import com.github.richygreat.microbankbff.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController {
	private final UserService userService;

	@GetMapping("/createuser/{userName}/{taxId}")
	public UserDTO createUser(String userName, String taxId) {
		UserDTO userDTO = new UserDTO();
		userService.createUser(userDTO);
		log.info("createUser: Streamed userDTO: {}", userDTO);
		return userDTO;
	}
}
