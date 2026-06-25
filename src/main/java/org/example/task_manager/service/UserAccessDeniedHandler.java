package org.example.task_manager.service;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserAccessDeniedHandler implements MethodAuthorizationDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(UserAccessDeniedHandler.class);

    @Override
    public Object handleDeniedInvocation(
            MethodInvocation methodInvocation,
            AuthorizationResult authorizationResult
    ) {
        log.warn("Access denied to UserService.getAll() — returning empty list as safe default");
        return Collections.emptyList();
    }
}