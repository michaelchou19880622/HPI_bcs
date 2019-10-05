package com.hpicorp.bcs.entities.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	
    @NotBlank
    private String account;

    @NotBlank
    private String password;

}
