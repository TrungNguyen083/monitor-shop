package com.mshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRating {
    private BigInteger productId;
    private String productName;
    private BigInteger amount;
}
