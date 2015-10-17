package app.component.web;

import io.github.chibat.j2ts.annotation.TypeScriptClass;
import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author tomofumi
 */
@RestController
public class CalculateController {

    @RequestMapping("/calc")
    ResponseEntity<Result> calculate(Input input, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Result result = new Result();
        result.add = input.arg1 + input.arg2;
        result.subtract = input.arg1 - input.arg2;
        return ResponseEntity.ok(result);
    }

    @TypeScriptClass
    @Data
    public static class Input {
        private Integer arg1;
        private Integer arg2;
    }

    @TypeScriptClass
    public static class Result {
        public Integer add;
        public Integer subtract;
    }

    // @Bean
    // public WebContentInterceptor webConfig() {
    // WebContentInterceptor interceptor = new WebContentInterceptor();
    // interceptor.setCacheSeconds(0);
    // interceptor.setUseExpiresHeader(true);
    // interceptor.setUseCacheControlHeader(true);
    // interceptor.setUseCacheControlNoStore(true);
    // return interceptor;
    // }
}
