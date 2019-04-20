package com.github.richygreat.microbankbff.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/createuser")
	public UserDTO createUser(@RequestParam("username") String userName, @RequestParam("taxid") String taxId) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserName(userName);
		userDTO.setTaxId(taxId);
		userService.createUser(userDTO);
		log.info("createUser: Streamed userDTO: {}", userDTO);
		return userDTO;
	}
}
