package com.tz.redismanager.security.auth;

import com.tz.redismanager.security.domain.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import javax.servlet.ServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * <p>TokenContext参数解析器</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 19:00
 * @see ServletModelAttributeMethodProcessor
 **/
public class AuthContextAttributeMethodProcessor extends ModelAttributeMethodProcessor {
    /**
     * Class constructor.
     *
     * @param annotationNotRequired if "true", non-simple method arguments and
     *                              return values are considered model attributes with or without a
     *                              {@code @ModelAttribute} annotation.
     */
    public AuthContextAttributeMethodProcessor() {
        super(true);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(Auth.class)
                && parameter.getParameterType().isAssignableFrom(AuthContext.class)
                && !parameter.hasParameterAnnotation(RequestBody.class);
    }

    /**
     * 参考：{@link ServletModelAttributeMethodProcessor#createAttribute(java.lang.String, org.springframework.core.MethodParameter, org.springframework.web.bind.support.WebDataBinderFactory, org.springframework.web.context.request.NativeWebRequest)}
     */
    @Override
    protected final Object createAttribute(String attributeName, MethodParameter methodParam,
                                           WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
        Object result = null;
        String value = getRequestValueForAttribute(attributeName, request);
        if (value != null) {
            Object attribute = createAttributeFromRequestValue(
                    value, attributeName, methodParam, binderFactory, request);
            if (attribute != null) {
                result =  attribute;
            }
        }else{
            result = super.createAttribute(attributeName, methodParam, binderFactory, request);
        }

        //设置TokenContext参数的值
        AuthContext authContext = AuthContextHolder.get();
        if (null != authContext) {
            result = authContext;
        }
        return result;
    }

    /**
     * Obtain a value from the request that may be used to instantiate the
     * model attribute through type conversion from String to the target type.
     * <p>The default implementation looks for the attribute name to match
     * a URI variable first and then a request parameter.
     * @param attributeName the model attribute name
     * @param request the current request
     * @return the request value to try to convert, or {@code null} if none
     */
    protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
        Map<String, String> variables = getUriTemplateVariables(request);
        String variableValue = variables.get(attributeName);
        if (StringUtils.hasText(variableValue)) {
            return variableValue;
        }
        String parameterValue = request.getParameter(attributeName);
        if (StringUtils.hasText(parameterValue)) {
            return parameterValue;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
        Map<String, String> variables = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return (variables != null ? variables : Collections.<String, String>emptyMap());
    }

    /**
     * Create a model attribute from a String request value (e.g. URI template
     * variable, request parameter) using type conversion.
     * <p>The default implementation converts only if there a registered
     * {@link Converter} that can perform the conversion.
     * @param sourceValue the source value to create the model attribute from
     * @param attributeName the name of the attribute (never {@code null})
     * @param methodParam the method parameter
     * @param binderFactory for creating WebDataBinder instance
     * @param request the current request
     * @return the created model attribute, or {@code null} if no suitable
     * conversion found
     * @throws Exception
     */
    protected Object createAttributeFromRequestValue(String sourceValue, String attributeName,
                                                     MethodParameter methodParam, WebDataBinderFactory binderFactory, NativeWebRequest request)
            throws Exception {

        DataBinder binder = binderFactory.createBinder(request, null, attributeName);
        ConversionService conversionService = binder.getConversionService();
        if (conversionService != null) {
            TypeDescriptor source = TypeDescriptor.valueOf(String.class);
            TypeDescriptor target = new TypeDescriptor(methodParam);
            if (conversionService.canConvert(source, target)) {
                return binder.convertIfNecessary(sourceValue, methodParam.getParameterType(), methodParam);
            }
        }
        return null;
    }

    /**
     * This implementation downcasts {@link WebDataBinder} to
     * {@link ServletRequestDataBinder} before binding.
     * @see ServletRequestDataBinderFactory
     */
    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        servletBinder.bind(servletRequest);
    }

}
