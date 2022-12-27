package ru.deevdenis.streamingvideo.constants;

import org.springframework.http.HttpStatus;

public class ApplicationConstants {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/mp4";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final HttpStatus HTTP_STATUS = HttpStatus.PARTIAL_CONTENT;

    private ApplicationConstants() {
    }
}
