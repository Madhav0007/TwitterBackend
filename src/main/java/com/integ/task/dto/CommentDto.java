package com.integ.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto{
    private Long id;
    private Long postId;
    private String username;
    private String content;

}
