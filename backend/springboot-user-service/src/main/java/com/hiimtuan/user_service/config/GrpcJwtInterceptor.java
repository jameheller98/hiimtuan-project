package com.hiimtuan.user_service.config;

import io.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class GrpcJwtInterceptor implements ServerInterceptor {
    public static final Context.Key<String> JWT_CTX_KEY = Context.key("jwt");
    private static final Metadata.Key<String> AUTH_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <Q, R> ServerCall.Listener<Q> interceptCall(
            ServerCall<Q, R> call, Metadata headers, ServerCallHandler<Q, R> next) {
        String authHeader = headers.get(AUTH_HEADER);
        Context ctx = Context.current();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            ctx = ctx.withValue(JWT_CTX_KEY, authHeader.substring(7));
        }
        return Contexts.interceptCall(ctx, call, headers, next);
    }
}
