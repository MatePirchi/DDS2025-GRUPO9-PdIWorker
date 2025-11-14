package ar.edu.utn.dds.k3003.messaging;

import ar.edu.utn.dds.k3003.app.PdIWorker;
import ar.edu.utn.dds.k3003.clients.dtos.PDIDTO;
import ar.edu.utn.dds.k3003.model.PdI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class deliveryHandler {

    private final PdIWorker pdiWorker;
    private final ObjectMapper objectMapper;
    private final Connection connection;
    
    @Value("${rabbitmq.queue.name:pdis.queue}")
    private String queueName;
    
    private Channel channel;

    @Autowired
    public deliveryHandler(PdIWorker pdiWorker, ObjectMapper objectMapper, Connection connection) {
        this.pdiWorker = pdiWorker;
        this.objectMapper = objectMapper;
        this.connection = connection;
    }

    @PostConstruct
    public void init() throws IOException {
        channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String mensaje = new String(delivery.getBody(), StandardCharsets.UTF_8);
            handleMessage(mensaje);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        System.out.println("Esperando mensajes de RabbitMQ en la cola: " + queueName);
    }

    public void handleMessage(String mensaje) {
        try {
            // Deserializar el JSON a PDIDTO
            PDIDTO pdiDTO = objectMapper.readValue(mensaje, PDIDTO.class);
            
            // Crear PdI y asignar campos manualmente
            PdI pdi = new PdI();
            pdi.setId(pdiDTO.id());
            pdi.setHechoId(pdiDTO.hechoId());
            pdi.setDescripcion(pdiDTO.descripcion());
            pdi.setLugar(pdiDTO.lugar());
            pdi.setMomento(pdiDTO.momento());
            pdi.setUrlImagen(pdiDTO.urlImagen());
            pdi.setTextoImagen(pdiDTO.textoImagen());
            pdi.setEtiquetas(pdiDTO.etiquetas());
            
            // Procesar el PdI
            pdiWorker.poner_a_procesar(pdi);
            
            System.out.println("PdI procesado correctamente desde RabbitMQ: " + pdiDTO.id());
            
        } catch (IOException e) {
            System.err.println("Error al deserializar mensaje de RabbitMQ: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al procesar mensaje de RabbitMQ: " + e.getMessage());
        }
    }
    
    @PreDestroy
    public void cleanup() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException | TimeoutException e) {
            System.err.println("Error al cerrar el canal de RabbitMQ: " + e.getMessage());
        }
    }
}
