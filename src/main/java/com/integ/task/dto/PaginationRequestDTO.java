package com.integ.task.dto;


import com.integ.task.util.Sort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequestDTO implements Serializable {

    private int page;
    private int size;
    private Sort sort;

    private String searchKey;

    private Boolean isActive;

}
