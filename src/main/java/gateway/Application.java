package gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@EnableZuulProxy
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}


@ConfigurationProperties
class UriConfiguration {
	
	private String httpbin = "http://httpbin.org:80";

	public String getHttpbin() {
		return httpbin;
	}

	public void setHttpbin(String httpbin) {
		this.httpbin = httpbin;
	}
}
