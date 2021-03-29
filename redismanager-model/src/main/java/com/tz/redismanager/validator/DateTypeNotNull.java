package com.tz.redismanager.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>日期类型不为空校验注解</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-21 15:30
 * @see NotBlank
 **/
@Documented
@Constraint(validatedBy = {DateTypeNotNullValidator.class})
@Target({TYPE})
@Retention(RUNTIME)
public @interface DateTypeNotNull {

    String message() default "dateType is null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
