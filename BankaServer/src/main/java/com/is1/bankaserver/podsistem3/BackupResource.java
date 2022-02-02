/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.is1.bankaserver.podsistem3;

import com.is1.bankaserver.podsistem1.MestoResource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Path("backup")
@Stateless
public class BackupResource {
    @Resource(lookup = "jms/__defaultConnectionFactory")
    public ConnectionFactory cf;
    
    @Resource(lookup = "crkni")
    public Topic topic;
    
    @Resource(lookup = "serverQueue")
    public Queue serverQueue;
    
    
    public Serializable getDiffFrom(int podsistem, JMSContext context, JMSProducer producer, JMSConsumer consumer){
        Message msg = context.createMessage();
        
        try {
            msg.setIntProperty("p", podsistem);
            msg.setStringProperty("tabela", "diff");
            msg.setBooleanProperty("get", true);
            
        
            producer.send(topic, msg);
            Message receivedMessage = consumer.receive(30000);
        
            if(receivedMessage instanceof ObjectMessage){
                ObjectMessage objm = (ObjectMessage) receivedMessage;
                return objm.getObject();
        }
            } catch (JMSException ex) {
                Logger.getLogger(MestoResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        return null;
    
    }
    //
    @GET
    @Path("diff")
    public Response getDiff(){
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(serverQueue);
        
        String ob1 = (String)getDiffFrom(1, context, producer, consumer);
        String ob2 = (String)getDiffFrom(2, context, producer, consumer);
        System.out.println(ob1);
        System.out.println(ob2);
        StringBuilder sb = new StringBuilder();
        if(ob1!=null){
            sb.append("{\"podsistem1\":").append(ob1);
        }
        if(ob2!=null){
            sb.append(", \"podsistem2\":").append(ob2).append("}");
        }
        return Response
                .ok("ok")
                .entity(sb.toString())
                .build();
    }   
     
    @GET
    @Path("all")
    public Response getAll(){
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(serverQueue);
        
        Message msg = context.createMessage();
        
        try {
            msg.setIntProperty("p", 3);
            msg.setStringProperty("tabela", "all");
            msg.setBooleanProperty("get", true);
            
        
            producer.send(topic, msg);
            Message receivedMessage = consumer.receive(30000);
        
            if(receivedMessage instanceof ObjectMessage){
                ObjectMessage objm = (ObjectMessage) receivedMessage;
                context.close();
                return Response
                .ok("top")
                .entity(objm.getObject())
                .build();
        }
        } catch (JMSException ex) {
            Logger.getLogger(MestoResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        context.close();
        return Response
                .status(500)
                .build();
    }
        
        
    
}
