package ru.deevdenis.streamingvideo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import static ru.deevdenis.streamingvideo.constants.ApplicationConstants.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MainController {

    @Value("classpath:avatar2.mp4")
    public Resource resource;

    public static final String PLAY = "/play";


    @GetMapping(PLAY)
    @ResponseBody
    public Mono<ResponseEntity<ResourceRegion>> play(
        @RequestHeader(value = "Range", required = false) String rangeHeader
    ) throws IOException {
        long rangeStart;
        long rangeEnd  = resource.contentLength();
        long fileSize = rangeEnd;

        UrlResource urlResource = new UrlResource(resource.getURL());
        ResourceRegion resourceRegion;
        if (rangeHeader == null) {
            rangeStart = 0L;
            resourceRegion = new ResourceRegion(urlResource, rangeStart, rangeEnd);

            return Mono.just(
                    ResponseEntity
                            .status(HTTP_STATUS)
                            .header(CONTENT_TYPE, VIDEO_CONTENT)
                            .body(resourceRegion)
            );
        }

        String[] ranges = rangeHeader.split("-");
        rangeStart = Long.parseLong(ranges[0].substring(6));

        if (ranges.length > 1)
        {
            rangeEnd = Long.parseLong(ranges[1]);
        }
        else
        {
            rangeEnd = fileSize - 1;
        }

        if (fileSize < rangeEnd)
        {
            rangeEnd = fileSize - 1;
        }

        resourceRegion = new ResourceRegion(urlResource, rangeStart, rangeEnd);

        return Mono.just(
                ResponseEntity
                        .status(HTTP_STATUS)
                        .header(CONTENT_TYPE, VIDEO_CONTENT)
                        .header(CONTENT_LENGTH, String.valueOf(fileSize))
                        .header(ACCEPT_RANGES, BYTES)
                        .header(CONTENT_RANGE, "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                        .body(resourceRegion)
        );
    }

}
