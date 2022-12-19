package com.axpe.example.persistence.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Status {
	public static final String ACTIVO = "ACTIVO";
	public static final String DELETE = "DELETE";
	public static final String PENDING = "PENDING";
}