package com.example.lpgtest.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class LpgTestConfiguration {

    @Bean
    @Primary
    public ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        return objectMapper;
    }

    @Bean
    public ObjectMapper customJson() {
        return new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                .build();
    }


    /* Cloud Redis Server */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration factory = new RedisStandaloneConfiguration();
        factory.setHostName("${REDIS_AZURE_HOST}");
        factory.setPort(6379);
        factory.setPassword("${REDIS_AZURE_SECRET}");

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(factory);
        lettuceConnectionFactory.setShareNativeConnection(false);
        return lettuceConnectionFactory;

    }


    /* Local Redis Server
    @Bean
    ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }*/

    @Bean
    public ReactiveRedisTemplate<byte[], byte[]> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {

        RedisSerializationContext<byte[], byte[]> serializationContext =
                RedisSerializationContext
                        .<byte[], byte[]>newSerializationContext(new StringRedisSerializer())
                        .hashKey(new StringRedisSerializer())
                        .hashValue(new Jackson2JsonRedisSerializer<>(Object.class))
                        .build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }

    /*******************************
     * Swagger API Documentation
     ******************************* */

    @Value("${enable.swagger.plugin:true}")
    private boolean enableSwaggerPlugin;


    ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Leisure Pass Group Java API ")
                .description("LPG Test Endpoint Documentation")
                .license("")
                .licenseUrl("")
                .version("1.0.0")
                .contact(new Contact("I.A", "", "isaac.alwar@gmail.com"))
                .build();
    }

    @Bean
    public Docket apiLPGDocument() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.lpgtest"))
                .paths(PathSelectors.any())
//                .paths(PathSelectors.ant("/api/*"))
                .build()
                .enable(enableSwaggerPlugin)
                .apiInfo(apiInfo());
    }

// http://localhost:8080/swagger-ui/
}
