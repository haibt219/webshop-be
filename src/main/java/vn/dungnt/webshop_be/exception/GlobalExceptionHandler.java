package vn.dungnt.webshop_be.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import vn.dungnt.webshop_be.dto.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceInUseException.class)
  public ResponseEntity<ErrorResponse> handleResourceInUseException(
      ResourceInUseException ex, WebRequest request) {

    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), null, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
      DuplicateResourceException ex, WebRequest request) {

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request) {

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.FORBIDDEN.value(), "Không có quyền truy cập", null, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(
      UnauthorizedException ex, WebRequest request) {

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    ErrorResponse apiError =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), "Lỗi xác thực dữ liệu", errors, LocalDateTime.now());
    return ResponseEntity.badRequest().body(apiError);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodNotAllowedException.class)
  public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(
      MethodNotAllowedException ex, WebRequest request) {

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage(), null, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex, WebRequest request) {

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.CONFLICT.value(), "Lỗi ràng buộc dữ liệu", null, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleNotFoundExceptions(NotFoundException ex) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage(), null, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {

    String errorMessage = "Lỗi định dạng dữ liệu JSON";
    Map<String, String> errors = new HashMap<>();

    Throwable cause = ex.getCause();
    if (cause instanceof InvalidFormatException) {
      InvalidFormatException ife = (InvalidFormatException) cause;
      if (ife.getPath() != null && !ife.getPath().isEmpty()) {
        String fieldName = ife.getPath().get(0).getFieldName();
        Object invalidValue = ife.getValue();
        Class<?> targetType = ife.getTargetType();

        String detailedMessage =
            String.format(
                "Giá trị '%s' không thể chuyển đổi thành kiểu %s",
                invalidValue, targetType.getSimpleName());

        errorMessage = String.format("Lỗi định dạng dữ liệu ở trường '%s'", fieldName);
        errors.put(fieldName, detailedMessage);
      }
    }

    if (errors.isEmpty()) {
      errors.put("error", ex.getMessage());
    }

    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), errorMessage, errors, LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
