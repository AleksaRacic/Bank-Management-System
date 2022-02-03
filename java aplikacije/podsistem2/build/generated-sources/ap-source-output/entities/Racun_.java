package entities;

import entities.Transakcija;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-02T18:00:53")
@StaticMetamodel(Racun.class)
public class Racun_ { 

    public static volatile SingularAttribute<Racun, Integer> brojTransakcija;
    public static volatile SingularAttribute<Racun, Integer> potrazuje;
    public static volatile SingularAttribute<Racun, Integer> idRacun;
    public static volatile SingularAttribute<Racun, Integer> komitent;
    public static volatile ListAttribute<Racun, Transakcija> transakcijaList;
    public static volatile SingularAttribute<Racun, Integer> dozvoljeniMinus;
    public static volatile ListAttribute<Racun, Transakcija> transakcijaList1;
    public static volatile SingularAttribute<Racun, Integer> duguje;
    public static volatile SingularAttribute<Racun, String> status;
    public static volatile SingularAttribute<Racun, Date> timestamp;

}