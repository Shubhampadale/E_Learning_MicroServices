package com.service.video.config;

import java.io.File;

public class AppConstants {

    public static final String DEFAULT_PAGE_NUMBER = "0";

    public static  final String DEFAULT_PAGE_SIZE = "10";

    public static  final String DEFAULT_SORT_BY = "title";

    public static final String COURSE_BANNER_UPLOAD_DIR = "uploads2" + File.separator + "courses"+File.separator+"banners";

    public static final String VIDEO_BANNER_UPLOAD_DIR = "uploads2" + File.separator + "videos"+File.separator+"banners";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_GUEST = "ROLE_GUEST";

    public static final String CATEGORY_SERVICE_BASE_URL="http://localhost:9091/api/v1";
}
