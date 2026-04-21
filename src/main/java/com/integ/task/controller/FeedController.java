package com.integ.task.controller;

import com.integ.task.dto.CollectionResponseDto;
import com.integ.task.dto.PaginationRequestDTO;
import com.integ.task.dto.PostDto;
import com.integ.task.service.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping("/feed")
    public ResponseEntity<CollectionResponseDto> getFeed(@RequestBody PaginationRequestDTO dto) {
        var page = feedService.getFeed(dto);

        return ResponseEntity.ok(
                CollectionResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Feed fetched successfully")
                        .responseObject(page.getContent())
                        .totalRecords(page.getTotalElements())
                        .build()
        );
    }
}