/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import container.Container;
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
    
    @Resource(lookup = "serverQueue")
    static public Queue serverQueue;
    
    @Resource(lookup = "backupTopic")
    static public Topic backupTopic;
    
    JMSContext context;
    JMSConsumer consumer;
    JMSProducer producer;
    EntityManagerFactory emf;
    EntityManager em;
    JMSConsumer backupConsumer;
    static ArrayList<Container> updateList = new ArrayList<>();
    
    public Main(){
        context = cf.createContext();
        context.setClientID("p1");
        consumer = context.createDurableConsumer(topic, "p1", "p=1" , true);
        producer = context.createProducer();
        backupConsumer = context.createDurableConsumer(backupTopic, "po1", "p=1" , true);
        emf = Persistence.createEntityManagerFactory("podsistem1PU");
        em = emf.createEntityManager();
        System.out.println("Pokrecem podsistem 1");
    }
    
    private void runClient(){
        ObjectMessage omsg = null;
        while(true){
            try {
                System.out.println("Ceka poruku");
                Message msg = this.consumer.receive();
                System.out.println("Primio");
                
                if(msg.getBooleanProperty("get")){
                    switch(msg.getStringProperty("tabela")){
                        case "diff":
                            JsonArrayBuilder listaMest= Json.createArrayBuilder();
                            JsonArrayBuilder listaFili= Json.createArrayBuilder();
                            JsonArrayBuilder listaKomi= Json.createArrayBuilder();
                            Mesto tmpMest;
                            Filijala tmpFili;
                            Komitent tmpKomi;
                            
                            for(Container c : updateList){
                                switch(c.getIme()){
                                    case "Mesto":
                                        tmpMest = (Mesto)c.getKlasa();
                                        System.out.println(tmpMest);
                                        JsonObjectBuilder tmpObj1 = Json.createObjectBuilder()
                                            .add("naziv", tmpMest.getNaziv())
                                            .add("postanskiBroj", tmpMest.getPostanskiBroj());
                                        if(tmpMest.getIdMesto()!=null){
                                            tmpObj1.add("idMesto", tmpMest.getIdMesto());
                                        }
                                        listaMest.add(tmpObj1);
                                        break;
                                    case "Filijala":
                                        tmpFili = (Filijala)c.getKlasa();
                                        JsonObjectBuilder tmpObj2 = Json.createObjectBuilder()
                                            .add("naziv", tmpFili.getNaziv())
                                            .add("adresa", tmpFili.getAdresa())
                                            .add("mesto", tmpFili.getIdMesto().getNaziv());
                                        if(tmpFili.getIdFilijala() != null){
                                            tmpObj2.add("idFilijala", tmpFili.getIdFilijala());
                                        }
                                        listaFili.add(tmpObj2);
                                        break;
                                    case "Komitent":
                                        tmpKomi = (Komitent)c.getKlasa();
                                        JsonObjectBuilder tmpObj3 = Json.createObjectBuilder()
                                            .add("naziv", tmpKomi.getNaziv())
                                            .add("adresa", tmpKomi.getAdresa())
                                            .add("mesto", tmpKomi.getSediste().getNaziv());
                                        
                                        if(tmpKomi.getIdKomitent()!=null){
                                            tmpObj3.add("idKomitent", tmpKomi.getIdKomitent());
                                        }
                                        listaKomi.add(tmpObj3);
                                        break;
                                        
                                }
                            }
                            JsonObject toSend12 = Json.createObjectBuilder()
                                .add("Mesta", listaMest)
                                .add("Filijale", listaFili)
                                .add("Komitenti", listaKomi).build();
                            System.out.println(toSend12);
                            omsg = context.createObjectMessage(toSend12.toString());
                            omsg.setIntProperty("server", 1);
                            producer.send(serverQueue, omsg);
                            break;
                        case "mesto":
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
                    //post
                    try{
                        em.getTransaction().begin();
                        switch(msg.getStringProperty("tabela")){
                            case "mesto":
                                String pb = msg.getStringProperty("pb");
                                String naziv = msg.getStringProperty("naziv");
                                Mesto m = new Mesto();
                                m.setNaziv(naziv);
                                m.setPostanskiBroj(pb);
                                updateList.add(new Container(new Mesto(m), "Mesto"));
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
                                Mesto tmpMesto = new Mesto(m1);
                                Filijala tmpFilijala = new Filijala(f);
                                tmpFilijala.setIdMesto(tmpMesto);
                                Container c2 = new Container(tmpMesto, "Mesto");
                                c2.setMerge();
                                updateList.add(c2);
                                updateList.add(new Container(tmpFilijala, "Filijala"));
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
                                updateList.add(new Container(new Komitent(k), "Komitent"));
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
        
                                kom.setSediste(sed);
                                Container c1 = new Container(new Komitent(kom), "Komitent");
                                c1.setMerge();
                                updateList.add(c1);
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
            }
    }
    }
    
    @Override
    public void finalize() {
        em.close();
        emf.close();
    }
    
    void listenBackup(){
        System.out.println("Ceka backup..");
        Message msg = backupConsumer.receive();
        System.out.println("Primio Backup Zahtev");
        System.out.println(updateList);
        producer.send(backupTopic, context.createObjectMessage(updateList));
        updateList.clear(); //moze doci do race condition
    }
    
    @Override
    public void run() {
        while(!Thread.interrupted()){
           listenBackup();
       }
    }
    
    public static void main(String[] args) {
        try {
            Main m = new Main();
            Thread t = new Thread(m);
            t.start();
            m.runClient();
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}
