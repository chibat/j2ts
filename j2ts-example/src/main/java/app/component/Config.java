package app.component;

import java.util.Properties;

import lombok.val;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
public class Config extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //
        // レスポンスのキャッシュを制御する Interceptor
        //
        val interceptor = new WebContentInterceptor();
        interceptor.setCacheSeconds(0);
        interceptor.setUseExpiresHeader(true);
        interceptor.setUseCacheControlHeader(true);
        interceptor.setUseCacheControlNoStore(true);
        val cacheMappings = new Properties();
        // cache for one month
        cacheMappings.put("/assets/**", "2592000"); // TODO
                                                    // local環境だけキャッシュしないとか分岐が必要
        cacheMappings.put("/webjars/**", "2592000");
        interceptor.setCacheMappings(cacheMappings);
        registry.addInterceptor(interceptor);
    }
}
