package entities;

import entities.Racun;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-02T18:00:53")
@StaticMetamodel(Transakcija.class)
public class Transakcija_ { 

    public static volatile SingularAttribute<Transakcija, Integer> idTransakcija;
    public static volatile SingularAttribute<Transakcija, Integer> iznos;
    public static volatile SingularAttribute<Transakcija, Racun> idRacunOd;
    public static volatile SingularAttribute<Transakcija, String> svrha;
    public static volatile SingularAttribute<Transakcija, Integer> idFilijala;
    public static volatile SingularAttribute<Transakcija, Racun> idRacunDo;
    public static volatile SingularAttribute<Transakcija, Integer> redniBroj;
    public static volatile SingularAttribute<Transakcija, Date> timestamp;

}