package com.eurodyn.qlack.util.data;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * @author EUROPEAN DYNAMICS SA
 */
public class SpringBeansUtils {

    /**
     * Static method that returns the class implementation of a Spring proxied bean interface.
     *
     * @param proxy Proxied object
     * @param targetClass Unused argument, useful only for type-casting
     * @param <T> Determined class type
     * @return Original class wrapped by proxy
     * @see <a href="http://web.archive.org/web/20090611002829/http://www.techper.net:80/2009/06/05/how-to-acess-target-object-behind-a-spring-proxy/">how-to-acess-target-object-behind-a-spring-proxy</a>
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return (T) ((Advised) proxy).getTargetSource().getTarget();
        } else {
            // expected to be cglib proxy then, which is simply a specialized class
            return (T) proxy;
        }
    }

}
