package com.vehiqon.features.email.service;

import com.vehiqon.features.email.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
