package com.vehiqon.features.email.service;

import com.vehiqon.features.email.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
