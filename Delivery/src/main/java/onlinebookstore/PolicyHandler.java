package onlinebookstore;

import onlinebookstore.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_Delivery(@Payload Ordered ordered){

        if(!ordered.validate()) return;

        System.out.println("\n\n##### listener Delivery : " + ordered.toJson() + "\n\n");

        // Sample Logic modify- 2021.05.19 //
        Delivery delivery = new Delivery();
        
        delivery.setOrderId(ordered.getOrderid());
        delivery.setDeliverystatus(ordered.getStatus());         
        
        deliveryRepository.save(delivery);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCancelled_CancelDelivery(@Payload OrderCancelled orderCancelled){

        if(!orderCancelled.validate()) return;

        System.out.println("\n\n##### listener CancelDelivery : " + orderCancelled.toJson() + "\n\n");
        
        Optional<Delivery> optDelivery = deliveryRepository.findById(orderCancelled.getOrderid());
        
        Delivery delivery = optDelivery.get();
        
        delivery.setStatus("Delivery cancel");
        delivery.setDeliverystatus(ordercancelled.getStatus());        
        
        deliveryRepository.save(delivery);
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
