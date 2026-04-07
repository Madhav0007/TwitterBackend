package com.integ.task.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class UserLoginUtil {

    public static Long getCareProvider() {
        Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsDto.getClaim("careProviderIDP");
    }

    public static Long getUserLoginIDP() {
        Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsDto.getClaim("userLoginIDP");
    }

    public static Long getUserTypeIDP() {
        Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsDto.getClaim("userTypeIDP");
    }

    public static Long getUserTypeReferenceIDF() {
        Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsDto.getClaim("userTypeReferenceIDF");
    }
    public static String getMobileNumber(){
        Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsDto.getClaim("mobileNumber");
    }
    public static Long getCitizenID(){
        Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsDto.getClaim("citizenId");
    }
    public static String getCitizenCode(){
        Jwt userDetailsDto = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsDto.getClaim("citizenCode");
    }



}