package com.smartlibrary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {
    private String timestamp;
    private int status;
    private String message;
    private String path;
}
