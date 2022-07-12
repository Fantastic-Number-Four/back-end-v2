package com.revature.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserLoginDTO {

	@NotNull @NotBlank
	private String publicAddress;
	
	@NotNull @NotBlank
	private String message;
	
}
