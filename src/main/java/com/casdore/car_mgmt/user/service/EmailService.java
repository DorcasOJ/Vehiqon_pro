package com.casdore.car_mgmt.user.service;

import com.casdore.car_mgmt.common.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
