package com.gsc.shopcart.repository.scart;

public interface CategoryCustomRepository {


    void createCategoryProduct(int idCategory, int idProduct, String createdBy);
}
