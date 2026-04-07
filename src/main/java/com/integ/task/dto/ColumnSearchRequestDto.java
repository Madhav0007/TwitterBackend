package com.integ.task.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class ColumnSearchRequestDto implements Serializable {
	private String columnName;
		private String searchString;
}
