package com.idata.common.model;

import com.idata.common.annotations.Colum;
import com.idata.common.annotations.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Table(name = "user_info")
public class StandardMdjfModel implements Serializable {
    @Colum(name = "id")
    private Long id;
    @Colum(name = "email")
    private String email;
    @Colum(name = "name")
    private String name;
}
