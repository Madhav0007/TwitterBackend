package com.integ.task.controller;

import com.integ.task.dto.ResponseDto;
import com.integ.task.dto.UserFollowDto;
import com.integ.task.service.UserFollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserFollowController {

    private final UserFollowService userFollowService;

    public UserFollowController(UserFollowService userFollowService) {
        this.userFollowService = userFollowService;
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<ResponseDto> followUser(@PathVariable Long userId) {
        UserFollowDto response = userFollowService.followUser(userId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("User followed successfully")
                        .responseObject(response)
                        .build()
        );
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<ResponseDto> unfollowUser(@PathVariable Long userId) {
        userFollowService.unfollowUser(userId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("User unfollowed successfully")
                        .build()
        );
    }

    @GetMapping("/{userId}/follow/status")
    public ResponseEntity<ResponseDto> followStatus(@PathVariable Long userId) {
        boolean following = userFollowService.isFollowing(userId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Follow status fetched successfully")
                        .responseObject(following)
                        .build()
        );
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<ResponseDto> followersCount(@PathVariable Long userId) {
        long count = userFollowService.getFollowersCount(userId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Followers count fetched successfully")
                        .responseObject(count)
                        .build()
        );
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<ResponseDto> followingCount(@PathVariable Long userId) {
        long count = userFollowService.getFollowingCount(userId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Following count fetched successfully")
                        .responseObject(count)
                        .build()
        );
    }
}