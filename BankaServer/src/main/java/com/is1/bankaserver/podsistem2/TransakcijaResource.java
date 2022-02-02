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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;


@Path("transakcija")
@Stateless
public class TransakcijaResource {
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    public ConnectionFactory cf;
    
    @Resource(lookup = "crkni")
    public Topic topic;
    
    @Resource(lookup = "serverQueue")
    public Queue serverQueue;
    
    @POST
    @Path("post/prenos/{racunod}/{racundo}/{iznos}/{svrha}")
    public Response postPrenos(@PathParam("racunod") int racunod, 
            @PathParam("racundo") int racundo,
            @PathParam("iznos") int iznos,
            @PathParam("svrha") String svrha){
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        Message msg = context.createMessage();
        
        try {
            msg.setIntProperty("p", 2);
            msg.setIntProperty("racunod", racunod);
            msg.setIntProperty("racundo", racundo);
            msg.setIntProperty("iznos", iznos);
            msg.setStringProperty("tabela", "transakcija_prenos");
            msg.setStringProperty("svrha", svrha);
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
    
    @POST
    @Path("post/uplata/{filijala}/{racundo}/{iznos}/{svrha}")
    public Response postUplata(@PathParam("filijala") int filijala, 
            @PathParam("racundo") int racundo,
            @PathParam("iznos") int iznos,
            @PathParam("svrha") String svrha){
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        Message msg = context.createMessage();
        
        try {
            msg.setIntProperty("p", 2);
            msg.setIntProperty("filijala", filijala);
            msg.setIntProperty("racundo", racundo);
            msg.setIntProperty("iznos", iznos);
            msg.setStringProperty("tabela", "transakcija_uplata");
            msg.setStringProperty("svrha", svrha);
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
    
    @POST
    @Path("post/isplata/{filijala}/{racunod}/{iznos}/{svrha}")
    public Response postIsplata(@PathParam("filijala") int filijala, 
            @PathParam("racunod") int racunod,
            @PathParam("iznos") int iznos,
            @PathParam("svrha") String svrha){
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        Message msg = context.createMessage();
        
        try {
            msg.setIntProperty("p", 2);
            msg.setIntProperty("filijala", filijala);
            msg.setIntProperty("racunod", racunod);
            msg.setIntProperty("iznos", iznos);
            msg.setStringProperty("tabela", "transakcija_isplata");
            msg.setStringProperty("svrha", svrha);
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
    @Path("get/{racun}")
    public Response getRacun(@PathParam("racun") int racun){
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(serverQueue);
        Message msg = context.createMessage();
        System.out.println("CheckpointGet\n\n");
        try {
            msg.setStringProperty("tabela", "transakcija");
            msg.setIntProperty("p", 2);
            msg.setIntProperty("racun", racun);
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
}
