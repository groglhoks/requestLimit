package ecom.market.limit.configs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Configuration
public class RequestLimitAspectConfig {

    @Autowired
    private ecom.market.limit.services.RequestLimitService requestLimitService;

    @Before("@annotation(ecom.market.limit.annotations.RequestLimit)")
    public void beforeLimitedMethods(JoinPoint jp) {
        String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
			       .getRequest().getRemoteAddr();
        requestLimitService.requestAllowed(remoteAddress);         
    }

}
