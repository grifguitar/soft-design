package ru.akirakozov.sd.refactoring.dto;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

public class CommonDTO {
    public String message;
    public List<Product> products = null;
    public Integer value = null;

    public CommonDTO(String message) {
        this.message = message;
    }

    public CommonDTO(String message, List<Product> products) {
        this.message = message;
        this.products = products;
    }

    public CommonDTO(String message, Integer value) {
        this.message = message;
        this.value = value;
    }
}
