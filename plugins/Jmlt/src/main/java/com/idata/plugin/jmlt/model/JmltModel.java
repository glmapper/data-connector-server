package com.idata.plugin.jmlt.model;

import com.idata.common.annotations.Colum;
import com.idata.common.annotations.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "user_info")
public class JmltModel implements Serializable {
    @Colum(name = "id")
    private Long id;
    @Colum(name = "email")
    private String email;
    @Colum(name = "name")
    private String name;
    @Colum(name = "create_time")
    private Date create_time;
}
