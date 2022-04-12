package com.xwdx.xwdxlogin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.xwdx.xwdxlogin.constant.CommonConstant.*;

/**
 * @author metinkong
 * @date 2022/4/8 12:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO {

    private int code;
    private String msg;
    private Object data;


    public static ResponseDTO success() {
        return ResponseDTO.builder().code(SUCCESS_CODE).msg(SUCCESS_MSG).build();
    }

    public static ResponseDTO success(Object data) {
        return ResponseDTO.builder().code(SUCCESS_CODE).msg(SUCCESS_MSG).data(data).build();
    }

    public static ResponseDTO error() {
        return ResponseDTO.builder().code(CODE_SYS_ERROR).msg(MSG_SYS_ERROR).build();
    }

    public static ResponseDTO nullParam() {
        return ResponseDTO.builder().code(CODE_NULL_PARAM).msg(MSG_NULL_PARAM).build();
    }

    public static ResponseDTO response(int code, String msg) {
        return ResponseDTO.builder().code(code).msg(msg).build();
    }

    public static ResponseDTO response(int code, String msg, Object data) {
        return ResponseDTO.builder().code(code).msg(msg).data(data).build();
    }
}
