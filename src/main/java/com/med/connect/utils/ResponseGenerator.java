package com.med.connect.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
public class ResponseGenerator {


    public static <T> ResponseEntity<Object> generateBadRequestResponse() {
        ResponseDto<T> responseDTO = new ResponseDto<>();
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ResponseDto<T>> generateCreatedResponse(
            T data,String message) {
        ResponseDto<T> responseDTO = new ResponseDto(data, message, true,
                HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    public static <T> ResponseEntity<ResponseDto<T>> generateInternalServerErrorResponse(
            String message) {
        ResponseDto<T> responseDTO = new ResponseDto<T>(null, message, false,
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(responseDTO,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }



    public static <T> ResponseEntity<Object> generateValidationRequest(Object value) {
        ResponseDto<T> responseDTO = new ResponseDto<>();
        return new ResponseEntity<>(value, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ResponseDto<T>> generateConflictResponse(
            String message) {
        ResponseDto<T> responseDTO = new ResponseDto(null, message, false,
                HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }

    public static <T> ResponseEntity<ResponseDto<T>> generateBadRequestResponse(
            String message) {
        ResponseDto<T> responseDTO = new ResponseDto(Optional.empty(), message, false,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }






    public static <T> ResponseEntity<ResponseDto<T>> generateAccessDeniedResponse(
            String message) {
        ResponseDto<T> responseDTO = new ResponseDto<T>(null, message, false,
                HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }

    public static <T> ResponseEntity<?> generateDataNotFound(String message) {
        ResponseDto<T> responseDTO = new ResponseDto<>(null, message, false,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(responseDTO,
                HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<?> generateDataAlreadyExists(String message) {
        ResponseDto<T> responseDTO = new ResponseDto<>(null, message, false,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(responseDTO,
                HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ResponseDto<T>> generateAuthenticationExceptionResponse(
            String message) {
        ResponseDto<T> responseDTO = new ResponseDto<T>(null, message, false,
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }

    public static <T> ResponseEntity<Object> generateHttpMediaTypeNotSupportedResponse(
            String message) {
        ResponseDto<T> responseDTO = new ResponseDto<>(null, message, false,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return new ResponseEntity<>(responseDTO,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public static <T> ResponseEntity<ResponseDto<T>> generateSuccessResponse(
            T data,String message) {
        ResponseDto<T> responseDTO = new ResponseDto(data, message, true,
                HttpStatus.OK.value());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }







}