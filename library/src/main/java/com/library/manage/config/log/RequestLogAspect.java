package com.library.manage.config.log;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Spring boot 控制器 请求日志，方便代码调试
 *
 * @author L.cm
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RequestLogAspect {

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private static final Gson GSON = new Gson();

    /**
     * AOP 环切 控制器 R 返回值
     *
     * @param point JoinPoint
     * @return Object
     * @throws Throwable 异常
     */
    @Around("(execution(* com.library.manage.controller..*.*(..))))")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUrl = Objects.requireNonNull(request).getRequestURI();
        String requestMethod = request.getMethod();

        // 构建成一条长 日志，避免并发下日志错乱
        StringBuilder beforeReqLog = new StringBuilder(300);
        // 日志参数
        List<Object> beforeReqArgs = new ArrayList<>();
        beforeReqLog.append("\n\n================  Request Start  ================\n");
        // 打印路由
        beforeReqLog.append("===> {}: {}");
        beforeReqArgs.add(requestMethod);
        beforeReqArgs.add(requestUrl);
        // 打印请求参数
        logIngArgs(point, beforeReqLog, beforeReqArgs);
        // 打印请求 headers
        logIngHeaders(request, beforeReqLog, beforeReqArgs);
        beforeReqLog.append("================   Request End   ================\n");

        // 打印执行时间
        long startNs = System.nanoTime();
        log.info(beforeReqLog.toString(), beforeReqArgs.toArray());
        // aop 执行后的日志
        StringBuilder afterReqLog = new StringBuilder(200);
        // 日志参数
        List<Object> afterReqArgs = new ArrayList<>();
        afterReqLog.append("\n\n================  Response Start  ================\n");
        try {
            Object result = point.proceed();
            // 打印返回结构体
            afterReqLog.append("===Result===  {}\n");
            afterReqArgs.add(GSON.toJson(result));
            return result;
        } finally {
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            afterReqLog.append("<=== {}: {} ({} ms)\n");
            afterReqArgs.add(requestMethod);
            afterReqArgs.add(requestUrl);
            afterReqArgs.add(tookMs);
            afterReqLog.append("================   Response End   ================\n");
            log.info(afterReqLog.toString(), afterReqArgs.toArray());
        }
    }

    /**
     * 激励请求参数
     *
     * @param point         ProceedingJoinPoint
     * @param beforeReqLog  StringBuilder
     * @param beforeReqArgs beforeReqArgs
     */
    public void logIngArgs(ProceedingJoinPoint point, StringBuilder beforeReqLog, List<Object> beforeReqArgs) {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Object[] args = point.getArgs();
        // 请求参数处理
        final Map<String, Object> paraMap = new HashMap<>(16);
        // 一次请求只能有一个 request body
        Object requestBodyValue = null;
        for (int i = 0; i < args.length; i++) {
            // 读取方法参数
            MethodParameter methodParameter = new SynthesizingMethodParameter(method, i);
            methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
            // PathVariable 参数跳过
            PathVariable pathVariable = methodParameter.getParameterAnnotation(PathVariable.class);
            if (pathVariable != null) {
                continue;
            }
            RequestBody requestBody = methodParameter.getParameterAnnotation(RequestBody.class);
            String parameterName = methodParameter.getParameterName();
            Object value = args[i];
            // 如果是body的json则是对象
            if (requestBody != null) {
                requestBodyValue = value;
                continue;
            }
            // 处理 参数
            if (value instanceof HttpServletRequest) {
                paraMap.putAll(((HttpServletRequest) value).getParameterMap());
                continue;
            } else if (value instanceof WebRequest) {
                paraMap.putAll(((WebRequest) value).getParameterMap());
                continue;
            } else if (value instanceof HttpServletResponse) {
                continue;
            } else if (value instanceof MultipartFile) {
                MultipartFile multipartFile = (MultipartFile) value;
                String name = multipartFile.getName();
                String fileName = multipartFile.getOriginalFilename();
                paraMap.put(name, fileName);
                continue;
            } else if (value instanceof List) {
                List<?> list = (List<?>) value;
                AtomicBoolean isSkip = new AtomicBoolean(false);
                for (Object o : list) {
                    if ("StandardMultipartFile".equalsIgnoreCase(o.getClass().getSimpleName())) {
                        isSkip.set(true);
                        break;
                    }
                }
                if (isSkip.get()) {
                    paraMap.put(parameterName, "此参数不能序列化为json");
                    continue;
                }
            }
            // 参数名
            RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
            String paraName = parameterName;
            if (requestParam != null && StrUtil.isNotBlank(requestParam.value())) {
                paraName = requestParam.value();
            }
            if (value == null) {
                paraMap.put(paraName, null);
            } else {
                paraMap.put(paraName, value);
            }
        }
        // 请求参数
        if (paraMap.isEmpty()) {
            beforeReqLog.append("\n");
        } else {
            beforeReqLog.append(" Parameters: {}\n");
            beforeReqArgs.add(JSONUtil.toJsonStr(paraMap));
        }
        if (requestBodyValue != null) {
            beforeReqLog.append("====Body=====  {}\n");
            beforeReqArgs.add(JSONUtil.toJsonStr(requestBodyValue));
        }
    }

    /**
     * 记录请求头
     *
     * @param request       HttpServletRequest
     * @param beforeReqLog  StringBuilder
     * @param beforeReqArgs beforeReqArgs
     */
    public void logIngHeaders(HttpServletRequest request, StringBuilder beforeReqLog, List<Object> beforeReqArgs) {
        // 打印请求头
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            String headerValue = request.getHeader(headerName);
            beforeReqLog.append("===Headers===  {}: {}\n");
            beforeReqArgs.add(headerName);
            beforeReqArgs.add(headerValue);
        }
    }
}
