/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entities.Racun;
import entities.Racun_;
import entities.Transakcija;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author HP
 */
public class Main {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    static private ConnectionFactory cf;
    
    @Resource(lookup = "crkni")
    static public Topic topic;
    
    @Resource(lookup = "serverQueue")
    static public Queue serverQueue;
    
    JMSContext context;
    JMSConsumer consumer;
    JMSProducer producer;
    EntityManagerFactory emf;
    EntityManager em;
    
    public Main(){
        context = cf.createContext();
        context.setClientID("p2");
        consumer = context.createDurableConsumer(topic, "p2", "p=2" , true);//dodati ovde filter
        producer = context.createProducer();
        System.out.println("Pokrecem podsistem 1");
    }
    
    @Override
    public void finalize() {
        em.close();
        emf.close();
    }
    
    private void runClient(){
        ObjectMessage omsg = null;
        while(true){
            try {
                System.out.println("Ceka poruku");
                Message msg = this.consumer.receive();
                System.out.println("Primio");
                emf = Persistence.createEntityManagerFactory("podsistem2PU");
                em = emf.createEntityManager();
                
                if(msg.getBooleanProperty("get")){
                    switch(msg.getStringProperty("tabela")){
                        case "racun":
                            TypedQuery<Racun> q1 = em.createNamedQuery("Racun.findByKomitent", Racun.class)
                                    .setParameter("komitent", msg.getIntProperty("komitent"));
                            List<Racun> r = q1.getResultList();
                            ArrayList<Racun> racunArray = new ArrayList<Racun>(r);
                            JsonArrayBuilder lista= Json.createArrayBuilder();
                            for(Racun ra : racunArray){
                                lista.add(Json.createObjectBuilder()
                                .add("potrazuje", ra.getPotrazuje())
                                .add("duguje", ra.getDuguje())
                                .add("status", ra.getStatus())
                                .add("timestamp",ra.getTimestamp().toString())
                                .add("broj_transakcija", ra.getBrojTransakcija())
                                .add("dozvoljen_minus", ra.getDozvoljeniMinus()));
       
                            }
                            JsonObject toSend = Json.createObjectBuilder()
                                    .add("racuni", lista)
                                    .add("komitent", msg.getIntProperty("komitent")).build();
                            System.out.println(toSend);
                            omsg = context.createObjectMessage(toSend.toString());
                            omsg.setIntProperty("server", 1);
                            producer.send(serverQueue, omsg);
                            break;

                            
                        case "transakcija":
                            TypedQuery<Transakcija> q2 = em.createNamedQuery("SELECT t FROM Transakcija t where idRacunOd = :idrac OR idRacunDo = :idrac", Transakcija.class)
                                    .setParameter("idrac", msg.getIntProperty("racun"));
                            List<Transakcija> t = q2.getResultList();
                            ArrayList<Transakcija> transakcijaArray = new ArrayList<Transakcija>(t);
                            JsonArrayBuilder lista1= Json.createArrayBuilder();
                            for(Transakcija tr : transakcijaArray){
                                
                                JsonObjectBuilder tmpObj = Json.createObjectBuilder()
                                .add("idTransakcija", tr.getIdTransakcija())
                                .add("timestamp", tr.getTimestamp().toString())
                                .add("iznos", tr.getIznos())
                                .add("redni_broj",tr.getRedniBroj())
                                .add("filijala", tr.getIdFilijala());
                                if(tr.getIdRacunDo()!=null){
                                    tmpObj.add("racun_uplata", tr.getIdRacunDo().getIdRacun());
                                }
                                if(tr.getIdRacunOd()!=null){
                                    tmpObj.add("racun_isplata", tr.getIdRacunOd().getIdRacun());
                                }
                                
                                lista1.add(tmpObj);
       
                            }
                            JsonObject toSend1 = Json.createObjectBuilder()
                                    .add("transakcije", lista1)
                                    .add("racun", msg.getIntProperty("racun")).build();
                            System.out.println(toSend1);
                            omsg = context.createObjectMessage(toSend1.toString());
                            omsg.setIntProperty("server", 1);
                            producer.send(serverQueue, omsg);
                            break;
                        
                        case "komitent":
                            
                            break;

                    }
                    producer.send(serverQueue, omsg);
                }else if(msg.getBooleanProperty("post")){
                    //post
                    try{
                        em.getTransaction().begin();
                        switch(msg.getStringProperty("tabela")){
                            case "racun":
                                System.out.println("Persistujem racun");
                                int komitent = msg.getIntProperty("komitent");
                                int dozMin = msg.getIntProperty("minus");
                                Racun ra = new Racun();
                                ra.setKomitent(komitent);
                                ra.setDozvoljeniMinus(dozMin);
                                ra.setTimestamp(new Timestamp(System.currentTimeMillis()));
                                ra.setDuguje(0);
                                ra.setPotrazuje(0);
                                ra.setStatus("A");
                                ra.setBrojTransakcija(0);
                                em.persist(ra);
                                break;

                            case "transakcija_prenos":
                                int idRacOd = msg.getIntProperty("racunod");
                                int idRacDo = msg.getIntProperty("racundo");
                                int iznos = msg.getIntProperty("iznos");
                                String svrha = msg.getStringProperty("svrha");
                                
                                Racun RacOd = em.find(Racun.class, idRacOd);
                                Racun RacDo = em.find(Racun.class, idRacDo);
                                
                                if(RacOd == null || RacDo == null) throw new Exception("Resource not found");
                                if(RacOd.getStatus().equals("B")) throw new Exception("Blokiran Racun");
                                int brTransOd = RacOd.getBrojTransakcija();
                                int brTransDo = RacDo.getBrojTransakcija();
                                int uplacenoOd = RacOd.getPotrazuje();
                                int uplacenoDo = RacDo.getPotrazuje() + iznos;
                                int isplacenoOd = RacOd.getDuguje() + iznos;
                                int isplacenoDo = RacDo.getDuguje();
                                RacDo.setBrojTransakcija(brTransDo + 1);
                                RacOd.setBrojTransakcija(brTransOd + 1);
                                
                                RacDo.setPotrazuje(uplacenoDo);
                                RacOd.setDuguje(isplacenoOd);
                                if(isplacenoDo - uplacenoDo < RacDo.getDozvoljeniMinus()) 
                                    RacDo.setStatus("A");
                                
                                if(isplacenoOd - uplacenoOd > RacOd.getDozvoljeniMinus()) 
                                    RacOd.setStatus("B");
                                
                                Transakcija trans = new Transakcija();
                                trans.setTimestamp(new Timestamp(System.currentTimeMillis()));
                                trans.setIznos(iznos);
                                trans.setSvrha(svrha);
                                trans.setRedniBroj(brTransDo + 1);
                                trans.setIdRacunDo(RacDo);
                                trans.setIdRacunOd(RacOd);
                                em.persist(RacDo);
                                em.persist(RacOd);
                                em.persist(trans);
                            break;
                                
                            case "transakcija_uplata":
                                int idRacDo1 = msg.getIntProperty("racundo");
                                int iznos1 = msg.getIntProperty("iznos");
                                String svrha1 = msg.getStringProperty("svrha");
                                int filijala = msg.getIntProperty("filijala");
                                
                                Racun RacDo1 = em.find(Racun.class, idRacDo1);
                                
                                if(RacDo1 == null) throw new Exception("Resource not found");

                                int brTransDo1 = RacDo1.getBrojTransakcija();

                                int uplacenoDo1 = RacDo1.getPotrazuje() + iznos1;
                                int isplacenoDo1 = RacDo1.getDuguje();
                                RacDo1.setBrojTransakcija(brTransDo1 + 1);

                                
                                RacDo1.setPotrazuje(uplacenoDo1);
                                if(isplacenoDo1 - uplacenoDo1 < RacDo1.getDozvoljeniMinus()) 
                                    RacDo1.setStatus("A");
                                
                                Transakcija trans1 = new Transakcija();
                                trans1.setTimestamp(new Timestamp(System.currentTimeMillis()));
                                trans1.setIznos(iznos1);
                                trans1.setSvrha(svrha1);
                                trans1.setRedniBroj(brTransDo1 + 1);
                                trans1.setIdRacunDo(RacDo1);
                                trans1.setIdFilijala(filijala);
                                em.persist(RacDo1);
                                em.persist(trans1);
                            break;
                            
                            case "transakcija_isplata":
                                int idRacOd2 = msg.getIntProperty("racunod");
                                int iznos2 = msg.getIntProperty("iznos");
                                String svrha2 = msg.getStringProperty("svrha");
                                
                                Racun RacOd2 = em.find(Racun.class, idRacOd2);
                                
                                if(RacOd2 == null) throw new Exception("Resource not found");
                                if(RacOd2.getStatus().equals("B")) throw new Exception("Blokiran Racun");
                                int brTransOd2 = RacOd2.getBrojTransakcija();
                                int uplacenoOd2 = RacOd2.getPotrazuje();
                                int isplacenoOd2 = RacOd2.getDuguje() + iznos2;
                                RacOd2.setBrojTransakcija(brTransOd2 + 1);

                                RacOd2.setDuguje(isplacenoOd2);
                                if(isplacenoOd2 - uplacenoOd2 > RacOd2.getDozvoljeniMinus()) 
                                    RacOd2.setStatus("B");
                                
                                Transakcija trans2 = new Transakcija();
                                trans2.setTimestamp(new Timestamp(System.currentTimeMillis()));
                                trans2.setIznos(iznos2);
                                trans2.setSvrha(svrha2);
                                trans2.setRedniBroj(brTransOd2 + 1);
                                trans2.setIdRacunOd(RacOd2);
                                em.persist(RacOd2);
                                em.persist(trans2);
                            break;

                        }
                        em.getTransaction().commit();
                    }catch (ConstraintViolationException e) {
                        System.out.println(e.getConstraintViolations().toString());
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }finally{
                        if(em.getTransaction().isActive()) em.getTransaction().rollback();
                    }
                   }else{
                    //patch
                    try{
                        em.getTransaction().begin();
                        switch(msg.getStringProperty("tabela")){
                            case "racun":
                                int idRac = msg.getIntProperty("racun");
                                Racun racunToDelete = em.find(Racun.class, idRac);
                                if(racunToDelete != null) em.remove(racunToDelete);
                                break;
                        }
                        em.getTransaction().commit();
                    }finally{
                        if(em.getTransaction().isActive()) em.getTransaction().rollback();
                    }
                }
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                em.close();
                emf.close();
            }
    }
    }
    public static void main(String[] args) {
        Main m = new Main();
        m.runClient();
    }
    
}
