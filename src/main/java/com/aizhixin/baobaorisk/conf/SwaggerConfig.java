package com.aizhixin.baobaorisk.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${sys.version}")
	private String systemPublish;

    @Bean
    public Docket openApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("微信相关API")
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .paths(or(regex("/open/.*")))//过滤的接口
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("微信相关API")
                        .version(systemPublish)
                        .build());
    }


    @Bean
    public Docket otherApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("其它API")
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .paths(or(regex("/manual/.*")))//过滤的接口
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("其它API")
                        .version(systemPublish)
                        .build());
    }
}
