/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTransakcija", query = "SELECT t FROM Transakcija t WHERE t.idTransakcija = :idTransakcija"),
    @NamedQuery(name = "Transakcija.findByTimestamp", query = "SELECT t FROM Transakcija t WHERE t.timestamp = :timestamp"),
    @NamedQuery(name = "Transakcija.findByIznos", query = "SELECT t FROM Transakcija t WHERE t.iznos = :iznos"),
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha"),
    @NamedQuery(name = "Transakcija.findByRedniBroj", query = "SELECT t FROM Transakcija t WHERE t.redniBroj = :redniBroj"),
    @NamedQuery(name = "Transakcija.findByIdFilijala", query = "SELECT t FROM Transakcija t WHERE t.idFilijala = :idFilijala")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTransakcija")
    private Integer idTransakcija;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iznos")
    private int iznos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "svrha")
    private String svrha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "redniBroj")
    private int redniBroj;
    @Column(name = "idFilijala")
    private Integer idFilijala;
    @JoinColumn(name = "idRacunDo", referencedColumnName = "idRacun")
    @ManyToOne
    private Racun idRacunDo;
    @JoinColumn(name = "idRacunOd", referencedColumnName = "idRacun")
    @ManyToOne
    private Racun idRacunOd;

    public Transakcija() {
    }

    public Transakcija(Integer idTransakcija) {
        this.idTransakcija = idTransakcija;
    }
    
    public Transakcija(Transakcija tr) {
        this.idTransakcija = tr.getIdTransakcija();
        this.timestamp = tr.getTimestamp();
        this.iznos = tr.getIznos();
        this.svrha = tr.getSvrha();
        this.redniBroj = tr.getRedniBroj();
    }

    public Transakcija(Integer idTransakcija, Date timestamp, int iznos, String svrha, int redniBroj) {
        this.idTransakcija = idTransakcija;
        this.timestamp = timestamp;
        this.iznos = iznos;
        this.svrha = svrha;
        this.redniBroj = redniBroj;
    }

    public Integer getIdTransakcija() {
        return idTransakcija;
    }

    public void setIdTransakcija(Integer idTransakcija) {
        this.idTransakcija = idTransakcija;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getIznos() {
        return iznos;
    }

    public void setIznos(int iznos) {
        this.iznos = iznos;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public int getRedniBroj() {
        return redniBroj;
    }

    public void setRedniBroj(int redniBroj) {
        this.redniBroj = redniBroj;
    }

    public Integer getIdFilijala() {
        return idFilijala;
    }

    public void setIdFilijala(Integer idFilijala) {
        this.idFilijala = idFilijala;
    }

    public Racun getIdRacunDo() {
        return idRacunDo;
    }

    public void setIdRacunDo(Racun idRacunDo) {
        this.idRacunDo = idRacunDo;
    }

    public Racun getIdRacunOd() {
        return idRacunOd;
    }

    public void setIdRacunOd(Racun idRacunOd) {
        this.idRacunOd = idRacunOd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransakcija != null ? idTransakcija.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTransakcija == null && other.idTransakcija != null) || (this.idTransakcija != null && !this.idTransakcija.equals(other.idTransakcija))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transakcija[ idTransakcija=" + idTransakcija + " ]";
    }
    
}
