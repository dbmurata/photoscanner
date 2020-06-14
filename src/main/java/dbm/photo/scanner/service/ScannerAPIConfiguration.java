package dbm.photo.scanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class ScannerAPIConfiguration implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ScannerAPIConfiguration.class);

    @Value("#{'${photoscanner.api.allowedOrigins:}'.split(',')}")
    private List<String> allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        log.info("Allowed Origins: {}", allowedOrigins);

        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            registry.addMapping("/**").allowedOrigins(allowedOrigins.toArray(new String[]{}));
        }
    }
}
