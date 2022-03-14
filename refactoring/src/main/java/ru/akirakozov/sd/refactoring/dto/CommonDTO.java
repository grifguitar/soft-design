package ru.akirakozov.sd.refactoring.dto;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

public class CommonDTO {
    private final String message;
    private final List<Product> products;
    private final Integer value;

    public CommonDTO(String message) {
        this.message = message;
        this.products = null;
        this.value = null;
    }

    public CommonDTO(String message, List<Product> products) {
        this.message = message;
        this.products = products;
        this.value = null;
    }

    public CommonDTO(String message, Integer value) {
        this.message = message;
        this.value = value;
        this.products = null;
    }

    public String getMessage() {
        return message;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Integer getValue() {
        return value;
    }
}
