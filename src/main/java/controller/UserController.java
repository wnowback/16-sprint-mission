package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    // 1. 사용자 등록
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Object create(
            @RequestPart("userCreateRequest") UserCreateRequest request,
            @RequestPart(value = "profile", required = false)
            MultipartFile profile
    ) throws IOException {

        return userService.create(
                request,
                toProfileRequest(profile)
        );
    }

    // 2. 모든 사용자 조회
    @RequestMapping(method = RequestMethod.GET)
    public Object findAll() {
        return userService.findAll();
    }

    // 3. 사용자 정보 수정
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.PATCH,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Object update(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest request,
            @RequestPart(value = "profile", required = false)
            MultipartFile profile
    ) throws IOException {

        return userService.update(
                userId,
                request,
                toProfileRequest(profile)
        );
    }

    // 4. 사용자 삭제
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.DELETE
    )
    public void delete(@PathVariable UUID userId) {
        userService.delete(userId);
    }

    // 5. 사용자 온라인 상태 업데이트
    @RequestMapping(
            value = "/{userId}/status",
            method = RequestMethod.PATCH
    )
    public Object updateStatus(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequest request
    ) {
        return userStatusService.updateByUserId(userId, request);
    }

    // 프로필 이미지를 서비스 요청 객체로 변환
    private Optional<BinaryContentCreateRequest> toProfileRequest(
            MultipartFile profile
    ) throws IOException {

        if (profile == null || profile.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                new BinaryContentCreateRequest(
                        profile.getOriginalFilename(),
                        profile.getContentType(),
                        profile.getBytes()
                )
        );
    }
}
