package com.integ.task.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MultiColumnSearchAndPaginationRequestDto extends PaginationRequestDTO {

	private Collection<ColumnSearchRequestDto> search;

}
