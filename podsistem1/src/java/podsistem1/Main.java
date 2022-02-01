/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
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
        context.setClientID("p1");
        consumer = context.createDurableConsumer(topic, "p1", null , true);//dodati ovde filter
        producer = context.createProducer();
        System.out.println("Pokrecem podsistem 1");
    }
    
    private void runClient(){
        ObjectMessage omsg = null;
        Mesto tmpMesto;
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
                            for(Mesto m : mestaArray){
                                m.setFilijalaList(null);
                                m.setKomitentList(null);
                            }
                            System.out.println(mestaArray);
                            omsg = context.createObjectMessage(mestaArray);
                            omsg.setIntProperty("server", 1);
                            producer.send(serverQueue, omsg);
                            break;
                            
                        case "filijala":
                            System.out.println("mestoGet");
                            TypedQuery<Filijala> qFilijala = em.createQuery("SELECT f FROM Filijala f", Filijala.class);
                            List<Filijala> filijale = qFilijala.getResultList();
                            ArrayList<Filijala> filijalaArray = new ArrayList<Filijala>(filijale);
                            
                            for(Filijala fil : filijalaArray){
                                tmpMesto = 
                                fil.setIdMesto();
                            }
                            System.out.println(filijalaArray);
                            omsg = context.createObjectMessage(filijalaArray);
                            omsg.setIntProperty("server", 1);
                            break;
                        
                        case "komitent":
                            System.out.println("mestoGet");
                            TypedQuery<Komitent> qKomitent = em.createQuery("SELECT k FROM Komitent k", Komitent.class);
                            List<Komitent> komitenti = qKomitent.getResultList();
                            ArrayList<Komitent> komitentArray = new ArrayList<Komitent>(komitenti);
                            System.out.println(komitentArray);
                            omsg = context.createObjectMessage(komitentArray);
                            omsg.setIntProperty("server", 1);
                            break;

                    }
                    producer.send(serverQueue, omsg);
                }else{
                    //upisi
                    try{
                        System.out.println("mestoPost");
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
        
                                if(m1 == null) break;

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
        
                                if(m2 == null) break; //videti da se vraca posiljaocu losa poruka

                                Komitent k = new Komitent();
                                k.setNaziv(nazivKomitenta);
                                k.setAdresa(adresaKomitenta);
                                k.setSediste(m2);
                                em.persist(k);
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
    
    @Override
    public void finalize() {
        System.out.println("pozvan finalize");
        em.close();
        emf.close();
    }
    public static void main(String[] args) {
        Main m = new Main();
        m.runClient();
    }
    
}
