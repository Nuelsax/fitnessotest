package com.decagon.fitnessoapp.service;

import com.decagon.fitnessoapp.dto.CheckOutRequest;
import com.decagon.fitnessoapp.dto.CheckOutResponse;

public interface CheckOutService {
    CheckOutResponse save(CheckOutRequest checkOutRequest);
}
