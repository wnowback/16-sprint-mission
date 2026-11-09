package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    // 1. 바이너리 파일 1개 다운로드
    @RequestMapping(
            value = "/{binaryContentId}",
            method = RequestMethod.GET
    )
    public ResponseEntity<byte[]> download(
            @PathVariable UUID binaryContentId
    ) {

        // 기존 서비스를 이용해 파일 조회
        BinaryContent binaryContent =
                binaryContentService.find(binaryContentId);

        // 파일의 Content-Type 설정
        MediaType mediaType = MediaType.parseMediaType(
                binaryContent.getContentType()
        );

        // 파일 데이터를 HTTP 응답으로 반환
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment"
                )
                .body(binaryContent.getBytes());
    }

    // 2. 바이너리 파일 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContent> findAllByIdIn(
            @RequestParam List<UUID> binaryContentIds
    ) {

        // 여러 파일을 ID 목록으로 조회
        return binaryContentService.findAllByIdIn(
                binaryContentIds
        );
    }
}
