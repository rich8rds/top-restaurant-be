package com.richards.mealsapp.mail;

import com.richards.mealsapp.response.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface JavaMailService {

    ResponseEntity<BaseResponse<String>>sendMailAlt(String receiverEmail, String subject, String text) throws IOException;
}
