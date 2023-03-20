package com.snoodify.user.controller;

import com.snoodify.user.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class OtpController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OtpService otpService;

    @GetMapping("/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam("username") String username) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String username = auth.getName();
        int otp = otpService.generateOTP(username);
        logger.info("OTP : " + otp);
        return new ResponseEntity<>("OTP generated successfully", HttpStatus.OK);
        //Todo: send OTP for validation
    }

    @GetMapping(value ="/validateOtp")
    public ResponseEntity<String> validateOtp(@RequestParam("otpnum") int otpnum,
                                              @RequestParam("username") String username) {
        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String username = auth.getName();
        logger.info("Otp Number : " + otpnum);
        //Validate the Otp
        if (otpnum >= 0) {
            int serverOtp = otpService.getOtp(username);

            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    otpService.clearOTP(username);
                    return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(FAIL, HttpStatus.BAD_REQUEST);
    }
}
