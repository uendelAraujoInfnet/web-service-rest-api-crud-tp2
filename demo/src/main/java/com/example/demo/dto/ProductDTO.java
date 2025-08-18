package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {

    @NotBlank @Size(min = 1, max = 120)
    private String name;

    @NotNull @DecimalMin(value = "0.00")
    private BigDecimal price;

    @NotNull @Min(0)
    private Integer stcok;

    public ProductDTO() {}

    public ProductDTO(String name, BigDecimal price, Integer stcok) {
        this.name = name;
        this.price = price;
        this.stcok = stcok;
    }
}
