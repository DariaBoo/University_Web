package ua.foxminded.university.controller.validator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@ExtendWith(SpringExtension.class)
class ValidationUtilsTest {

    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private ValidationUtils validationUtils;
    private FieldError fieldError;
    private FieldError fieldError2;
    private String errorMessage = "Field may not be blank or null";
    private String errorMessage2 = "Field must be 5 symbols size";

    @Test
    void getErrorMessages_shouldReturnErrorMessage_whenCallTheMethod() {
        fieldError = new FieldError("Entity", "field", errorMessage);
        fieldError2 = new FieldError("Entity", "field2", errorMessage2);
        List<FieldError> errors = Stream.of(fieldError, fieldError2).collect(Collectors.toList());
        given(bindingResult.getFieldErrors()).willReturn(errors);
        String result = ValidationUtils.getErrorMessages(bindingResult);
        assertNotNull(result);
        assertTrue(result.contains(errorMessage));
        assertTrue(result.contains(errorMessage2));
    }
}
