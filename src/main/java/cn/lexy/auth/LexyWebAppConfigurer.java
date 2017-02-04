package cn.lexy.auth;

import cn.lexy.auth.filter.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器
 * @author Jason.Fang
 *
 */
@Configuration
public class LexyWebAppConfigurer extends WebMvcConfigurerAdapter {
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		String[] excludeUrls = new String[]{"/index/**","/welcome/main","/login","/login/**","/logout","/error","/error/**"};
        registry.addInterceptor(new SessionInterceptor())
        		.addPathPatterns("/**")
        		.excludePathPatterns(excludeUrls);
        super.addInterceptors(registry);
    }
}
