package com.integ.task.service;

import com.integ.task.dto.PaginationRequestDTO;
import com.integ.task.dto.PostDto;
import org.springframework.data.domain.Page;

public interface FeedService {
    Page<PostDto> getFeed(PaginationRequestDTO dto);
}