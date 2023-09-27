package com.gsc.shopcart.dto;

import com.gsc.shopcart.model.scart.entity.Category;
import com.gsc.shopcart.security.UserPrincipal;
import lombok.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveCategoryDTO {


    private Integer id;
    private Integer idCategory;
    private Integer idCatalog;
    private String name;
    private String description;
    private String status;
    private String ivPath;
    private Integer displayOrder;
    private List<Category> listCategorySelected;
}
