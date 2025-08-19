package com.two_fact_auth._fa.demo.dto.request;

public class ResetPassEmailReqDto {

    private String email;
    private String resetLink;

    // Constructors
    public ResetPassEmailReqDto() {
    }

    public ResetPassEmailReqDto(String email, String resetLink) {
        this.email = email;
        this.resetLink = resetLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetLink() {
        return resetLink;
    }

    public void setResetLink(String resetLink) {
        this.resetLink = resetLink;
    }
}