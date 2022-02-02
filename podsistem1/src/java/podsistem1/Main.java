/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import java.math.BigDecimal;
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
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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
        consumer = context.createDurableConsumer(topic, "p1", "p=1" , true);//dodati ovde filter
        producer = context.createProducer();
        System.out.println("Pokrecem podsistem 1");
    }
    
    private void runClient(){
        ObjectMessage omsg = null;
        while(true){
            try {
                System.out.println("Ceka poruku");
                Message msg = this.consumer.receive();
                System.out.println("Primio");
                emf = Persistence.createEntityManagerFactory("podsistem1PU");
                em = emf.createEntityManager();
                
                if(msg.getBooleanProperty("get")){
                    switch(msg.getStringProperty("tabela")){
                        case "mesto":
                            System.out.println("mestoGet");
                            TypedQuery<Mesto> qMesta = em.createQuery("SELECT m FROM Mesto m", Mesto.class);
                            List<Mesto> mesta = qMesta.getResultList();
                            ArrayList<Mesto> mestaArray = new ArrayList<Mesto>(mesta);
                            JsonArrayBuilder lista= Json.createArrayBuilder();
                            for(Mesto m : mestaArray){
                                lista.add(Json.createObjectBuilder()
                                .add("idMesto", m.getIdMesto())
                                .add("naziv", m.getNaziv())
                                .add("postanskiBroj", m.getPostanskiBroj()));
       
                            }
                            JsonObject toSend = Json.createObjectBuilder()
                                    .add("mesta", lista).build();
                            System.out.println(toSend);
                            omsg = context.createObjectMessage(toSend.toString());
                            omsg.setIntProperty("server", 1);
                            producer.send(serverQueue, omsg);
                            break;

                            
                        case "filijala":
                            System.out.println("mestoGet");
                            TypedQuery<Filijala> qFilijala = em.createQuery("SELECT f FROM Filijala f", Filijala.class);
                            List<Filijala> filijale = qFilijala.getResultList();
                            ArrayList<Filijala> filijalaArray = new ArrayList<Filijala>(filijale);
                            JsonArrayBuilder listaFilijala = Json.createArrayBuilder();
                            for(Filijala f : filijalaArray){
                                listaFilijala.add(Json.createObjectBuilder()
                                .add("idFilijala", f.getIdFilijala())
                                .add("naziv", f.getNaziv())
                                .add("adresa", f.getAdresa())
                                .add("mesto", f.getIdMesto().getNaziv()));
       
                            }
                            JsonObject filijalaToSend = Json.createObjectBuilder()
                                    .add("filijale", listaFilijala).build();
                            
                            System.out.println(filijalaToSend);
                            omsg = context.createObjectMessage(filijalaToSend.toString());
                            omsg.setIntProperty("server", 1);
                            break;
                        
                        case "komitent":
                            System.out.println("mestoGet");
                            TypedQuery<Komitent> qKomitent = em.createQuery("SELECT k FROM Komitent k", Komitent.class);
                            List<Komitent> komitenti = qKomitent.getResultList();
                            ArrayList<Komitent> komitentArray = new ArrayList<Komitent>(komitenti);
                            JsonArrayBuilder listaKomitenata = Json.createArrayBuilder();
                            for(Komitent k : komitentArray){
                                listaKomitenata.add(Json.createObjectBuilder()
                                .add("idKomitent", k.getIdKomitent())
                                .add("naziv", k.getNaziv())
                                .add("adresa", k.getAdresa())
                                .add("mesto", k.getSediste().getNaziv()));
       
                            }
                            JsonObject komitentToSend = Json.createObjectBuilder()
                                    .add("komitenti", listaKomitenata).build();
                            System.out.println(komitentToSend);
                            omsg = context.createObjectMessage(komitentToSend.toString());
                            omsg.setIntProperty("server", 1);
                            break;

                    }
                    producer.send(serverQueue, omsg);
                }else if(msg.getBooleanProperty("post")){
                    //upisi
                    try{
                        em.getTransaction().begin();
                        switch(msg.getStringProperty("tabela")){
                            case "mesto":
                                String pb = msg.getStringProperty("pb");
                                String naziv = msg.getStringProperty("naziv");
                                Mesto m = new Mesto();
                                m.setNaziv(naziv);
                                m.setPostanskiBroj(pb);
                                em.persist(m);
                                break;

                            case "filijala":
                                String mestoFilijale = msg.getStringProperty("mesto");
                                String nazivFilijale = msg.getStringProperty("naziv");
                                String adresaFilijale = msg.getStringProperty("adresa");
                                TypedQuery<Mesto> q1 = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", mestoFilijale);
                                Mesto m1 = q1.getSingleResult();
        
                                if(m1 == null) throw new Exception("Resource not found");

                                Filijala f = new Filijala();
                                f.setNaziv(nazivFilijale);
                                f.setAdresa(adresaFilijale);
                                f.setIdMesto(m1);
                                em.persist(f);
                                break;
                                
                            case "komitent":
                                String mestoKomitenta = msg.getStringProperty("mesto");
                                String nazivKomitenta = msg.getStringProperty("naziv");
                                String adresaKomitenta = msg.getStringProperty("naziv");
                                TypedQuery<Mesto> q2 = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", mestoKomitenta);
                                Mesto m2 = q2.getSingleResult();
        
                                if(m2 == null) throw new Exception("Resource not found");

                                Komitent k = new Komitent();
                                k.setNaziv(nazivKomitenta);
                                k.setAdresa(adresaKomitenta);
                                k.setSediste(m2);
                                em.persist(k);
                                break;

                        }
                        em.getTransaction().commit();
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
                            case "komitent":
                                int idK = msg.getIntProperty("idk");
                                String nazivSedista = msg.getStringProperty("mesto");
                                TypedQuery<Komitent> k1 = em.createNamedQuery("Komitent.findByIdKomitent", Komitent.class).setParameter("idKomitent", idK);
                                Komitent kom = k1.getSingleResult();
        
                                if(kom == null) throw new Exception("Resource not found");
        
                                TypedQuery<Mesto> sediste = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", nazivSedista);
                                Mesto sed = sediste.getSingleResult();
        
                                if(sed == null) throw new Exception("Resource not found");
        
                                int executeUpdate = em.createQuery("UPDATE  Komitent a SET a.sediste=:s WHERE a.idKomitent=:k", Komitent.class).setParameter("s", sed).setParameter("k",kom.getIdKomitent()).executeUpdate();
                                System.out.println(executeUpdate);
                                break;
                        }
                        em.getTransaction().commit();
                    }catch(Exception e){
                        System.out.println(e.getMessage());
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
    
    @Override
    public void finalize() {
        em.close();
        emf.close();
    }
    public static void main(String[] args) {
        Main m = new Main();
        m.runClient();
    }
    
}
