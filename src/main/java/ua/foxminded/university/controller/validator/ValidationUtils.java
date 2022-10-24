package ua.foxminded.university.controller.validator;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationUtils {
    
    private ValidationUtils() {
        
    }

    public static String getErrorMessages(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuilder allErrors = new StringBuilder();
        for (FieldError error : errors ) {
            allErrors.append(error.getDefaultMessage() + ", ");
        }
        return allErrors.toString();
      }
}
