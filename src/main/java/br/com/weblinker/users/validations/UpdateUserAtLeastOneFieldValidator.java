package br.com.weblinker.users.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import br.com.weblinker.users.dto.UpdateUserRequest;

import org.apache.commons.lang3.StringUtils;

public class UpdateUserAtLeastOneFieldValidator implements ConstraintValidator<UpdateUserAtLeastOneField, UpdateUserRequest> {

    @Override
    public boolean isValid(UpdateUserRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return false;
        }

        return StringUtils.isNotBlank(request.getFirstName()) ||
                StringUtils.isNotBlank(request.getLastName()) ||
                StringUtils.isNotBlank(request.getEmail()) ||
                StringUtils.isNotBlank(request.getPhone());
    }
}
