package com.integ.task.controller;

import com.integ.task.dto.PostDto;
import com.integ.task.entity.Post;
import com.integ.task.service.PostService;
import com.integ.task.util.LoggerUtil;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/posts")
public class PostController extends BaseController <PostService , Post , PostDto, Long>{


    /**
     * Constructor
     *
     * @param loggerUtil   Logger util
     * @param service      Service
     */
    protected PostController(LoggerUtil loggerUtil, PostService service) {
        super(loggerUtil, service, "Post");
    }
}
