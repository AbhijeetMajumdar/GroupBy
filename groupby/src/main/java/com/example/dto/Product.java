package com.example.dto;

@lombok.Getter
@lombok.Setter
@lombok.RequiredArgsConstructor
public class Product {
	Integer id;
	String name;
	Category category;
}
