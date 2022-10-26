package ua.foxminded.university.controller.validator;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtils {
    
    private ValidationUtils() {
        
    }

    public static String getErrorMessages(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuilder resultErrors = new StringBuilder();
        for (FieldError error : errors ) {
            resultErrors.append(error.getDefaultMessage() + ", ");
        }
        log.info("[ON getErrorMessages]:: errors - {}", resultErrors.toString());
        return resultErrors.toString();
      }
}
