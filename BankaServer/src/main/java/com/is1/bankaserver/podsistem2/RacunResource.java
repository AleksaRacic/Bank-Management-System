/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.is1.bankaserver.podsistem2;

import com.is1.bankaserver.podsistem1.MestoResource;
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
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author H
 */

@Path("racun")
@Stateless
public class RacunResource {
    @Resource(lookup = "jms/__defaultConnectionFactory")
    public ConnectionFactory cf;
    
    @Resource(lookup = "crkni")
    public Topic topic;
    
    @Resource(lookup = "serverQueue")
    public Queue serverQueue;
    
    @POST
    @Path("post/{komitent}/{minus}")
    public Response postRacun(@PathParam("komitent") int komitent, @PathParam("minus") int minus){
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        Message msg = context.createMessage();
        
        try {
            msg.setIntProperty("p", 2);
            msg.setIntProperty("komitent", komitent);
            msg.setIntProperty("minus", minus);
            msg.setStringProperty("tabela", "racun");
            msg.setBooleanProperty("get", false);
            msg.setBooleanProperty("post", true);
        } catch (JMSException ex) {
            Logger.getLogger(MestoResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        System.out.println("PoslanaPoruka");
        context.close();
        return Response
                .ok("ok")
                .build();
    }
    
    @GET
    @Path("get/{komitent}")
    public Response getRacun(@PathParam("komitent") int komitent){
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(serverQueue);
        Message msg = context.createMessage();
        System.out.println("CheckpointGet\n\n");
        try {
            msg.setStringProperty("tabela", "racun");
            msg.setIntProperty("p", 2);
            msg.setIntProperty("komitent", komitent);
            msg.setBooleanProperty("get", true);
        } catch (JMSException ex) {
            Logger.getLogger(MestoResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("PoslanaPoruka");
        producer.send(topic, msg);
        Message receivedMessage = consumer.receive(20000);
        System.out.println("POST> primio poruku");
        if(receivedMessage instanceof ObjectMessage){
            try {
                ObjectMessage objm = (ObjectMessage) receivedMessage;
                System.out.println(objm.getObject());
                context.close();
        return Response
                .ok("top")
                .entity(objm.getObject())
                .build();
            } catch (JMSException ex) {
                Logger.getLogger(MestoResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        context.close();
        return Response
                .status(500)
                .build();
    }
    
    @PATCH
    @Path("patch/{racun}")
    public Response patchRacun(@PathParam("racun") int racun){
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        Message msg = context.createMessage();
        
        try {
            msg.setIntProperty("p", 2);
            msg.setIntProperty("racun", racun);
            msg.setStringProperty("tabela", "racun");
            msg.setBooleanProperty("get", false);
            msg.setBooleanProperty("post", false);
        } catch (JMSException ex) {
            Logger.getLogger(MestoResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        System.out.println("PoslanaPoruka");
        context.close();
        return Response
                .ok("ok")
                .build();
    }
    
    
}
