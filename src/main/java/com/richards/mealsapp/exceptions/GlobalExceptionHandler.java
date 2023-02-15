package com.richards.mealsapp.exceptions;

import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.InputMismatchException;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<BaseResponse<String>> userAlreadyExists(InputMismatchException ne) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ne.getMessage());
        errorResponse.setCode(ResponseCodeEnum.ID_ALREADY_EXISTS.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> userNotFound(UserNotFoundException ne) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ne.getMessage());
        errorResponse.setCode(ResponseCodeEnum.USER_NOT_FOUND.getCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<BaseResponse<String>> alreadyExist(AlreadyExistsException ne) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ne.getMessage());
        errorResponse.setCode(ResponseCodeEnum.ERROR_DUPLICATE_USER.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> resourceNotFound(ResourceNotFoundException ne) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ne.getMessage());
        errorResponse.setCode(ResponseCodeEnum.RESOURCE_NOT_FOUND.getCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> productNotFound(ProductNotFoundException ne) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ne.getMessage());
        errorResponse.setCode(ResponseCodeEnum.PRODUCT_NOT_FOUND.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(PickupCenterNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> pickupCenterNotFound(PickupCenterNotFoundException ne){
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ne.getMessage());
        errorResponse.setCode(ResponseCodeEnum.PRODUCT_NOT_FOUND.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthorizedUserException.class})
    public ResponseEntity<BaseResponse<String>> handleUnauthorizedUserException(UnauthorizedUserException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ex.getMessage());
        errorResponse.setCode(ResponseCodeEnum.PRODUCT_NOT_FOUND.getCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> invalidProductAttributes(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();

        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(errorMessage);
        errorResponse.setCode(ResponseCodeEnum.ERROR_EMAIL_INVALID.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> userAlreadyExists(UsernameNotFoundException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ex.getMessage());
        errorResponse.setCode(ResponseCodeEnum.ID_ALREADY_EXISTS.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseResponse<String>> notAvailable(UnauthorizedException ex){
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ex.getMessage());
        errorResponse.setCode(ResponseCodeEnum.UNAUTHORISED_ACCESS.getCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<String>> illegalArgumentException(IllegalArgumentException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ex.getMessage());
        errorResponse.setCode(ResponseCodeEnum.ERROR.getCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<BaseResponse<String>> passwordMismatchException(PasswordMisMatchException ex) {
        BaseResponse<String> errorResponse = new BaseResponse<>();
        errorResponse.setDescription(ex.getMessage());
        errorResponse.setCode(ResponseCodeEnum.ERROR_PASSWORD_MISMATCH.getCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}

