package com.tz.redismanager.trace;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.tz.redismanager.annotation.LoggerMsg;
import com.tz.redismanager.constant.ConstInterface;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <p>调用链路日志追踪Factory</p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-08-29 22:12
 **/
public class TraceLoggerFactory {

    private static TransmittableThreadLocal<String> traceThreadLocal = new TransmittableThreadLocal<>();

    public static void setTraceInfo(String traceInfo) {
        traceThreadLocal.set(traceInfo);
    }

    public static String getTraceInfo() {
        return traceThreadLocal.get();
    }

    public static void clearTraceInfo() {
        traceThreadLocal.remove();
    }

    private static Enhancer enhancer = new Enhancer();

    static {
        enhancer.setSuperclass(TraceLogger.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return proxy.invokeSuper(obj, enhanceArgs(method, args));
            }

            private Object[] enhanceArgs(Method method, Object[] args) {
                if (ArrayUtils.isEmpty(args)) {
                    return args;
                }
                Annotation[][] annotations = method.getParameterAnnotations();
                if (ArrayUtils.isEmpty(annotations)) {
                    return args;
                }
                /**找到{@link LoggerMsg}注解标识的参数的位置*/
                Integer index = findLoggerMsgToMarkParamIndex(annotations);
                if (null != index && ArrayUtils.isNotEmpty(args) && null != args[index]) {
                    //增强参数
                    args[index] = enhanceArg(args[index]);
                }
                return args;
            }

            private Integer findLoggerMsgToMarkParamIndex(Annotation[][] annotations) {
                Integer index = null;
                int i = -1;
                label:
                for (Annotation[] array : annotations) {
                    i++;
                    if (ArrayUtils.isEmpty(array)) {
                        continue;
                    }
                    for (Annotation annotation : array) {
                        if (null != annotation && annotation.annotationType().getName().equals(LoggerMsg.class.getName())) {
                            index = i;
                            break label;
                        }
                    }
                }
                return index;
            }

            private String enhanceArg(Object msg) {
                StringBuilder logBuilder = new StringBuilder();
                //获取方法信息到日志builder
                this.getMethodInfo(logBuilder);

                String traceInfo = getTraceInfo();
                if (null != traceInfo) {
                    logBuilder.append(traceInfo);
                } else {
                    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                    if (requestAttributes instanceof ServletRequestAttributes) {
                        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                        String sid = requestAttributes.getSessionId();
                        logBuilder.append(ConstInterface.Symbol.MIDDLE_BRACKET_LEFT).append("sid-").append(sid).append(ConstInterface.Symbol.MIDDLE_BRACKET_RIGHT);
                    }
                }
                logBuilder.append(" ").append(msg);
                return logBuilder.toString();
            }

            private void getMethodInfo(StringBuilder logBuilder) {
                StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
                if (ArrayUtils.isNotEmpty(stackTraces) && stackTraces.length >= 7) {
                    StackTraceElement stackTrace = stackTraces[6];
                    logBuilder.append(ConstInterface.Symbol.MIDDLE_BRACKET_LEFT);
                    logBuilder.append(stackTrace.getFileName());
                    logBuilder.append(ConstInterface.Symbol.COLON);
                    logBuilder.append(stackTrace.getLineNumber());
                    logBuilder.append(ConstInterface.Symbol.MIDDLE_BRACKET_RIGHT);
                    logBuilder.append(" ");
                }
            }
        });
    }

    public static Logger getLogger(Class<?> clazz) {
        TraceLogger traceLogger = (TraceLogger) enhancer.create();
        traceLogger.logger = LoggerFactory.getLogger(clazz);
        return traceLogger;
    }

    public static class TraceLogger implements Logger {

        private Logger logger;

        @Override
        public String getName() {
            return logger.getName();
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.isTraceEnabled();
        }

        @Override
        public void trace(@LoggerMsg String msg) {
            logger.trace(msg);
        }

        @Override
        public void trace(@LoggerMsg String format, Object arg) {
            logger.trace(format, arg);
        }

        @Override
        public void trace(@LoggerMsg String format, Object arg1, Object arg2) {
            logger.trace(format, arg1, arg2);
        }

        @Override
        public void trace(@LoggerMsg String format, Object... arguments) {
            logger.trace(format, arguments);
        }

        @Override
        public void trace(@LoggerMsg String msg, Throwable t) {
            logger.trace(msg, t);
        }

        @Override
        public boolean isTraceEnabled(Marker marker) {
            return logger.isTraceEnabled(marker);
        }

        @Override
        public void trace(Marker marker, @LoggerMsg String msg) {
            logger.trace(marker, msg);
        }

        @Override
        public void trace(Marker marker, @LoggerMsg String format, Object arg) {
            logger.trace(marker, format, arg);
        }

        @Override
        public void trace(Marker marker, @LoggerMsg String format, Object arg1, Object arg2) {
            logger.trace(marker, format, arg1, arg2);
        }

        @Override
        public void trace(Marker marker, @LoggerMsg String format, Object... argArray) {
            logger.trace(marker, format, argArray);
        }

        @Override
        public void trace(Marker marker, @LoggerMsg String msg, Throwable t) {
            logger.trace(marker, msg, t);
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isDebugEnabled();
        }

        @Override
        public void debug(@LoggerMsg String msg) {
            logger.debug(msg);
        }

        @Override
        public void debug(@LoggerMsg String format, Object arg) {
            logger.debug(format, arg);
        }

        @Override
        public void debug(@LoggerMsg String format, Object arg1, Object arg2) {
            logger.debug(format, arg1, arg2);
        }

        @Override
        public void debug(@LoggerMsg String format, Object... arguments) {
            logger.debug(format, arguments);
        }

        @Override
        public void debug(@LoggerMsg String msg, Throwable t) {
            logger.debug(msg, t);
        }

        @Override
        public boolean isDebugEnabled(Marker marker) {
            return logger.isDebugEnabled(marker);
        }

        @Override
        public void debug(Marker marker, @LoggerMsg String msg) {
            logger.debug(marker, msg);
        }

        @Override
        public void debug(Marker marker, @LoggerMsg String format, Object arg) {
            logger.debug(marker, format, arg);
        }

        @Override
        public void debug(Marker marker, @LoggerMsg String format, Object arg1, Object arg2) {
            logger.debug(marker, format, arg1, arg2);
        }

        @Override
        public void debug(Marker marker, @LoggerMsg String format, Object... arguments) {
            logger.debug(marker, format, arguments);
        }

        @Override
        public void debug(Marker marker, @LoggerMsg String msg, Throwable t) {
            logger.debug(marker, msg, t);
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.isInfoEnabled();
        }

        @Override
        public void info(@LoggerMsg String msg) {
            logger.info(msg);
        }

        @Override
        public void info(@LoggerMsg String format, Object arg) {
            logger.info(format, arg);
        }

        @Override
        public void info(@LoggerMsg String format, Object arg1, Object arg2) {
            logger.info(format, arg1, arg2);
        }

        @Override
        public void info(@LoggerMsg String format, Object... arguments) {
            logger.info(format, arguments);
        }

        @Override
        public void info(@LoggerMsg String msg, Throwable t) {
            logger.info(msg, t);
        }

        @Override
        public boolean isInfoEnabled(Marker marker) {
            return logger.isInfoEnabled(marker);
        }

        @Override
        public void info(Marker marker, @LoggerMsg String msg) {
            logger.info(marker, msg);
        }

        @Override
        public void info(Marker marker, @LoggerMsg String format, Object arg) {
            logger.info(marker, format, arg);
        }

        @Override
        public void info(Marker marker, @LoggerMsg String format, Object arg1, Object arg2) {
            logger.info(marker, format, arg1, arg2);
        }

        @Override
        public void info(Marker marker, @LoggerMsg String format, Object... arguments) {
            logger.info(marker, format, arguments);
        }

        @Override
        public void info(Marker marker, @LoggerMsg String msg, Throwable t) {
            logger.info(marker, msg, t);
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isWarnEnabled();
        }

        @Override
        public void warn(@LoggerMsg String msg) {
            logger.warn(msg);
        }

        @Override
        public void warn(@LoggerMsg String format, Object arg) {
            logger.warn(format, arg);
        }

        @Override
        public void warn(@LoggerMsg String format, Object... arguments) {
            logger.warn(format, arguments);
        }

        @Override
        public void warn(@LoggerMsg String format, Object arg1, Object arg2) {
            logger.warn(format, arg1, arg2);
        }

        @Override
        public void warn(@LoggerMsg String msg, Throwable t) {
            logger.warn(msg, t);
        }

        @Override
        public boolean isWarnEnabled(Marker marker) {
            return logger.isWarnEnabled(marker);
        }

        @Override
        public void warn(Marker marker, @LoggerMsg String msg) {
            logger.warn(marker, msg);
        }

        @Override
        public void warn(Marker marker, @LoggerMsg String format, Object arg) {
            logger.warn(marker, format, arg);
        }

        @Override
        public void warn(Marker marker, @LoggerMsg String format, Object arg1, Object arg2) {
            logger.warn(marker, format, arg1, arg2);
        }

        @Override
        public void warn(Marker marker, @LoggerMsg String format, Object... arguments) {
            logger.warn(marker, format, arguments);
        }

        @Override
        public void warn(Marker marker, @LoggerMsg String msg, Throwable t) {
            logger.warn(marker, msg, t);
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isErrorEnabled();
        }

        @Override
        public void error(@LoggerMsg String msg) {
            logger.error(msg);
        }

        @Override
        public void error(@LoggerMsg String format, Object arg) {
            logger.error(format, arg);
        }

        @Override
        public void error(@LoggerMsg String format, Object arg1, Object arg2) {
            logger.error(format, arg1, arg2);
        }

        @Override
        public void error(@LoggerMsg String format, Object... arguments) {
            logger.error(format, arguments);
        }

        @Override
        public void error(@LoggerMsg String msg, Throwable t) {
            logger.error(msg, t);
        }

        @Override
        public boolean isErrorEnabled(Marker marker) {
            return logger.isErrorEnabled(marker);
        }

        @Override
        public void error(Marker marker, @LoggerMsg String msg) {
            logger.error(marker, msg);
        }

        @Override
        public void error(Marker marker, @LoggerMsg String format, Object arg) {
            logger.error(marker, format, arg);
        }

        @Override
        public void error(Marker marker, @LoggerMsg String format, Object arg1, Object arg2) {
            logger.error(marker, format, arg1, arg2);
        }

        @Override
        public void error(Marker marker, @LoggerMsg String format, Object... arguments) {
            logger.error(marker, format, arguments);
        }

        @Override
        public void error(Marker marker, @LoggerMsg String msg, Throwable t) {
            logger.error(marker, msg, t);
        }
    }
}
