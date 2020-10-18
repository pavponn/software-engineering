package ru.pavponn.sd.refactoring.dao;

import ru.pavponn.sd.refactoring.models.Product;

import java.util.List;

public interface ProductDaoRead {

    List<Product> getAllProducts();

    Product getMinPriceProduct();

    Product getMaxPriceProduct();

    long getSumPrices();

    long getCount();

}
