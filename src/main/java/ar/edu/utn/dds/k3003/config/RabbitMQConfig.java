package ar.edu.utn.dds.k3003.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.host:localhost}")
    private String host;

    @Value("${rabbitmq.port:5672}")
    private int port;

    @Value("${rabbitmq.username:guest}")
    private String username;

    @Value("${rabbitmq.password:guest}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(username);
        
        // Configuración para reconexión automática
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(10000); // 10 segundos
        factory.setConnectionTimeout(30000); // 30 segundos
        
        return factory;
    }
}
