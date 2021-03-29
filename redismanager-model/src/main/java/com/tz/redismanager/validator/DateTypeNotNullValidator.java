package com.tz.redismanager.validator;

import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.enm.DateTypeEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * <p>日期类型不为空校验器</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-21 15:37
 **/
public class DateTypeNotNullValidator implements ConstraintValidator<DateTypeNotNull, AnalysisParam> {

    @Override
    public void initialize(DateTypeNotNull constraintAnnotation) {

    }

    @Override
    public boolean isValid(AnalysisParam value, ConstraintValidatorContext context) {
        DateTypeEnum dateTypeEnum = Optional.ofNullable(value).map(temp -> temp.getDateType()).map(temp -> DateTypeEnum.getEnumByType(temp)).orElse(null);
        if (null == dateTypeEnum) {
            return false;
        }
        return true;
    }
}
