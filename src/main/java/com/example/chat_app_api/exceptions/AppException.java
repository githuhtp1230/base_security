package com.example.chat_app_api.exceptions;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
}
