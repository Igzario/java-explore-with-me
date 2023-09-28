package ru.practicum.ewm.validated;

import org.hibernate.validator.internal.util.DomainNameUtil;
import ru.practicum.ewm.user.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class CheckEmailValidatorForUserDto implements ConstraintValidator<EmailValidate, UserDto> {

    @Override
    public void initialize(EmailValidate constraintAnnotation) {
    }

    private static final int MAX_LOCAL_PART_LENGTH = 64;
    private static final String LOCAL_PART_ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uFFFF-]";
    private static final String LOCAL_PART_INSIDE_QUOTES_ATOM = "(?:[a-z0-9!#$%&'*.(),<>\\[\\]:;  @+/=?^_`{|}~\u0080-\uFFFF-]|\\\\\\\\|\\\\\\\")";
    /**
     * Regular expression for the local part of an email address (everything before '@')
     */
    private static final Pattern LOCAL_PART_PATTERN = Pattern.compile(
            "(?:" + LOCAL_PART_ATOM + "+|\"" + LOCAL_PART_INSIDE_QUOTES_ATOM + "+\")" +
                    "(?:\\." + "(?:" + LOCAL_PART_ATOM + "+|\"" + LOCAL_PART_INSIDE_QUOTES_ATOM + "+\")" + ")*", CASE_INSENSITIVE
    );

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext constraintValidatorContext) {
        String value = userDto.getEmail();

        if (value == null || value.length() == 0) {
            return true;
        }
        int splitPosition = value.lastIndexOf('@');
        if (splitPosition < 0) {
            return false;
        }
        String localPart = value.substring(0, splitPosition);
        String domainPart = value.substring(splitPosition + 1);
        if (!isValidEmailLocalPart(localPart)) {
            return false;
        }
        return DomainNameUtil.isValidEmailDomainAddress(domainPart);
    }

    private boolean isValidEmailLocalPart(String localPart) {
        if (localPart.length() > MAX_LOCAL_PART_LENGTH) {
            return false;
        }
        Matcher matcher = LOCAL_PART_PATTERN.matcher(localPart);
        return matcher.matches();
    }
}