package com.vehiqon.email.service;

import com.vehiqon.email.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
