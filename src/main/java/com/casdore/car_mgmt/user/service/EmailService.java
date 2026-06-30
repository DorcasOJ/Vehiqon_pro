package com.casdore.car_mgmt.user.service;

import com.casdore.car_mgmt.user.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
