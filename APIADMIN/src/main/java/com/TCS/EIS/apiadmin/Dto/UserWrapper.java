package com.TCS.EIS.apiadmin.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWrapper {

	private String username;
	private String password;
}
