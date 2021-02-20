package cc.yezj.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring boot properties configuration.
 */
@ConfigurationProperties(prefix = "spring.school")
@Getter
@Setter
public final class SpringBootPropertiesConfiguration {

    private String students;

}
