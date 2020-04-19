package com.alexm.bearspendings.test;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;

/**
 * @author AlexM
 * Date: 4/12/20
 **/
public enum SAMPLE_PRODUCTS {
    CARTOFI(1L, "Cartofi"),
    CARNE(2L, "Carne"),
    CHEFIR(3L, "Chefir");

    public final UIProduct uiProduct;
    public final Product product;
    public final Long id;
    public final String productName;

    SAMPLE_PRODUCTS(long id, String productName) {
        this.id = id;
        this.productName = productName;
        this.uiProduct = UIProduct.builder().id(id).name(productName).build();
        this.product = Product.builder().id(id).name(productName).build();
    }
}
