package com.github.achuzhmarov.tutorial.controller;

import com.github.achuzhmarov.tutorial.controller.common.BaseControllerIT;
import com.github.achuzhmarov.tutorial.controller.model.CalculateBonusPointsRequest;
import com.github.achuzhmarov.tutorial.model.Customer;
import com.github.achuzhmarov.tutorial.model.Product;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

import static com.github.achuzhmarov.tutorial.builder.CommonBuilder.mapOf;
import static com.github.achuzhmarov.tutorial.builder.CommonBuilder.number;
import static com.github.achuzhmarov.tutorial.builder.CustomerBuilder.customer;
import static com.github.achuzhmarov.tutorial.builder.ProductBuilder.product;
import static org.junit.Assert.assertEquals;

public class CustomerControllerIT extends BaseControllerIT {
    @Autowired
    CustomerController customerController;

    @Test
    public void setFavProduct_customerFavProductUpdated() {
        Customer customer = customer("customer").premium(false).build();
        Product favProduct = product("favProduct").build();
        customerRepository.save(customer);
        productRepository.save(favProduct);

        customerController.setFavProduct(customer.getLogin(), favProduct.getId());

        Customer dbCustomer = customerRepository.getOne(customer.getId());
        assertEquals(dbCustomer.getFavProduct().getId(), favProduct.getId());
    }

    @Test
    public void calculateBonusPoints_twoProductTypes_correctValueCalculated() {
        Product product1 = product("product1").price("1.01").build();
        Product product2 = product("product2").price("10.00").build();
        productRepository.save(product1);
        productRepository.save(product2);

        Customer customer = customer("customer").build();
        customerRepository.save(customer);

        Map<Long, Long> quantities = mapOf(product1.getId(), 1L, product2.getId(), 2L);


        BigDecimal bonus = customerController.calculateBonusPoints(
                new CalculateBonusPointsRequest("customer", quantities)
        );


        Assert.assertEquals(number("2.10"), bonus);
    }
}