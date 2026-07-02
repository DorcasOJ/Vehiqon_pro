package com.vehiqon.car_mgmt.common.service;

import com.vehiqon.car_mgmt.common.dto.response.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
