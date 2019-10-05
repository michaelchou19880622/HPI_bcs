package com.hpicorp.bcs.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    
	@NotBlank
    @Size(min = 3, max = 20)
    private String account;
	
	@NotBlank
    @Size(min = 4, max = 50)
    private String name;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    
}