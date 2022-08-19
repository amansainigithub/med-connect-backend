package com.med.connect.utils;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto<T> implements Serializable {

    private static final long serialVersionUID = -6072518716971093796L;
    private T data;
    private String message;
    private boolean success;
    private long timestamp = System.currentTimeMillis();

    /**
     * @param data
     * @param message
     * @param success
     */
    public ResponseDto(T data, String message, boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ResponseDto(T data) {
        super();
        this.data = data;
        this.success = true;
    }

}