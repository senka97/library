package com.hybrid.libraryproject.security;

public class AuthenticationResponse {

    private String accessToken;

    private Long expiresIn;

    public AuthenticationResponse() {
        this.accessToken = null;
        this.expiresIn = null;
    }

    public AuthenticationResponse(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
