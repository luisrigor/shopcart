package com.gsc.shopcart.repository.scart;

import com.gsc.shopcart.security.UserPrincipal;

import java.util.List;

public interface ProductPropertyCustomRepository {

    String mergeProductProperty(Integer id, Integer idProduct, String label, String option_value,
                                Integer max_lenght, String data_type, Integer rank, String help,
                                String status, String mandatory, String user);

}
