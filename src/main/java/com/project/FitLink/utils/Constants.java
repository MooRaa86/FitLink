package com.project.FitLink.utils;

public final class Constants {
    public static final String JWT_HEADER = "Authorization";
    public static final long JWT_ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60;
    public static final long JWT_REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 30;
}
