/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import container.Container;
import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author HP
 */
public class Main implements Runnable{
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    static private ConnectionFactory cf;
    
    @Resource(lookup = "crkni")
    static public Topic topic;
    
    @Resource(lookup = "backupTopic")
    static public Topic backupTopic;
    
    @Resource(lookup = "serverQueue")
    static public Queue serverQueue;
    
    JMSContext context;
    JMSConsumer consumer;
    JMSProducer producer;
    EntityManagerFactory emf;
    EntityManager em;
    JMSConsumer consumerServer;
    
    public Main(){
        System.out.println("Pokrecem podsistem 3");
        context = cf.createContext();
        context.setClientID("b");
        producer = context.createProducer();
        consumer = context.createDurableConsumer(backupTopic, "b", null , true);
        consumerServer = context.createDurableConsumer(topic, "ba", "p=3", true);
        emf = Persistence.createEntityManagerFactory("podsistem3PU");
        em = emf.createEntityManager();
    }
    
    void doBackup(int podsistemNo){
        try {
            Class<?> persistenceClass = null;
            
            Message msg = context.createMessage();
            msg.setIntProperty("p", podsistemNo);
            producer.send(backupTopic, msg);
            ObjectMessage objm1 = (ObjectMessage)consumer.receive();
            System.out.println(objm1.getObject());
            ArrayList<Container> updateList = (ArrayList<Container>)objm1.getObject();
            System.out.println(updateList);
            em.getTransaction().begin();
            for(Container c : updateList){
                persistenceClass = Class.forName("entities." + c.getIme());
                System.out.println("ubacujem" + c.getKlasa().toString());
                if (c.isMerge()) {
                    System.out.println("Postoji");
                    em.merge(persistenceClass.cast(c.getKlasa()));
                } else {
                    em.persist(persistenceClass.cast(c.getKlasa()));
                    //em.flush();
                    //em.clear();
                }
            }
            em.getTransaction().commit();
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void finalize() {
        em.close();
        emf.close();
    }
    
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                    Thread.sleep(12000);
                    System.out.println("Zapocinjem Backup...");
                    doBackup(1);
                    doBackup(2);
                    System.out.println("Zavrsio Backup...");

            }
        } 
        catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void runClient(){
        ObjectMessage omsg = null;
        while(true){
            try {
                System.out.println("Ceka poruku");
                Message msg = this.consumerServer.receive();
                System.out.println("Primio");
                if(msg.getBooleanProperty("get")){
                    switch(msg.getStringProperty("tabela")){
                        case "all":
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
               
                            
                            TypedQuery<Racun> q1 = em.createNamedQuery("Racun.findAll", Racun.class);
                            List<Racun> r = q1.getResultList();
                            ArrayList<Racun> racunArray = new ArrayList<Racun>(r);
                            JsonArrayBuilder lista4= Json.createArrayBuilder();
                            for(Racun ra : racunArray){
                                lista.add(Json.createObjectBuilder()
                                .add("potrazuje", ra.getPotrazuje())
                                .add("duguje", ra.getDuguje())
                                .add("status", ra.getStatus())
                                .add("timestamp",ra.getTimestamp().toString())
                                .add("broj_transakcija", ra.getBrojTransakcija())
                                .add("dozvoljen_minus", ra.getDozvoljeniMinus()));
       
                            }
                     
                            TypedQuery<Transakcija> q2 = em.createNamedQuery("Transakcija.findAll", Transakcija.class);
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

                            JsonObject toSend = Json.createObjectBuilder()
                                    .add("transakcije", lista1)
                                    .add("racuni", lista4)
                                    .add("komitenti", listaKomitenata)
                                    .add("filijale", listaFilijala)
                                    .add("mesta", lista).build();
                            
                            omsg = context.createObjectMessage(toSend.toString());
                            omsg.setIntProperty("server", 1);
                            producer.send(serverQueue, omsg);
                            break;
                    }
                    
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public static void main(String[] args){
        try {
            Main main = new Main();
            Thread t = new Thread(main);
            t.start();
            main.runClient();
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
