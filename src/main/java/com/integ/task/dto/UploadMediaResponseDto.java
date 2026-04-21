package com.integ.task.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadMediaResponseDto {
    private Long postId;
    private int uploadedCount;
}
