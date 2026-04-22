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
        public Companies(){}
    }

    public static class Error {
        public static final String FILE_NOT_FOUND = "File not found: %s";
        public static final String COULD_NOT_READ_FILE = "Couldn't read file %s";
        public Error() {}
    }
}
