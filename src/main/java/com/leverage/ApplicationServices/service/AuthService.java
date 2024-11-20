package com.leverage.ApplicationServices.service;

import com.leverage.ApplicationServices.DTO.AuthRequestDto;

public interface AuthService {
    Object authenticate(AuthRequestDto authRequestDto);
}
