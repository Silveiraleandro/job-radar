package com.leandrosilveira.jobradar.common;

public class Constants {
    public static class Greenhouse {
        public static final String BOARD_TOKEN = "stripe";
        public static final String GREENHOUSE_URL = "https://boards-api.greenhouse.io/v1/boards/" + BOARD_TOKEN + "/jobs";
        public static final String UNKNOWN = "Unknown";
        public Greenhouse(){}
    }

    public static class GreenhouseError {
        public static final String FAILED_TO_FETCH = "Failed to fetch Greenhouse jobs. HTTP status: %s";
        public static final String INTERRUPTED_WHILE_FETCHING = "Interrupted while fetching Greenhouse jobs";
        public static final String FAIL_TO_PARSE_JOB_RESPONSE = "Failed to parse Greenhouse jobs response";
        public GreenhouseError(){}
    }

    public static class Companies {
        public static final String STRIPE = "Stripe";
        public static final String PROPER = "Proper";
        public Companies(){}
    }

    public static class Html {
        public static final String POSTING = ".posting";
        public static final String POSTING_TITLE_H5 = ".posting-title h5";
        public static final String H5 = "h5";
        public static final String POSTING_TITLE = ".posting-title";
        public static final String SORT_BY_LOCATION = ".sort-by-location";
        public static final String ABS_HREF = "abs:href";
        public static final String USER_AGENT = "Mozilla/5.0";
        public Html(){}
    }

    public static class Error {
        public static final String FILE_NOT_FOUND = "File not found: %s";
        public static final String COULD_NOT_READ_FILE = "Couldn't read file %s";
        public static final String JOB_NOT_FOUND = "Job not found with id %s";
        public static final String FAILED_TO_FETCH = "Failed to fetch HTML jobs";
        public Error() {}
    }
}
