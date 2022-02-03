/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "racun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r"),
    @NamedQuery(name = "Racun.findByIdRacun", query = "SELECT r FROM Racun r WHERE r.idRacun = :idRacun"),
    @NamedQuery(name = "Racun.findByKomitent", query = "SELECT r FROM Racun r WHERE r.komitent = :komitent"),
    @NamedQuery(name = "Racun.findByPotrazuje", query = "SELECT r FROM Racun r WHERE r.potrazuje = :potrazuje"),
    @NamedQuery(name = "Racun.findByDuguje", query = "SELECT r FROM Racun r WHERE r.duguje = :duguje"),
    @NamedQuery(name = "Racun.findByStatus", query = "SELECT r FROM Racun r WHERE r.status = :status"),
    @NamedQuery(name = "Racun.findByTimestamp", query = "SELECT r FROM Racun r WHERE r.timestamp = :timestamp"),
    @NamedQuery(name = "Racun.findByBrojTransakcija", query = "SELECT r FROM Racun r WHERE r.brojTransakcija = :brojTransakcija"),
    @NamedQuery(name = "Racun.findByDozvoljeniMinus", query = "SELECT r FROM Racun r WHERE r.dozvoljeniMinus = :dozvoljeniMinus")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRacun")
    private Integer idRacun;
    @Basic(optional = false)
    @NotNull
    @Column(name = "komitent")
    private int komitent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "potrazuje")
    private int potrazuje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duguje")
    private int duguje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "brojTransakcija")
    private int brojTransakcija;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dozvoljeniMinus")
    private int dozvoljeniMinus;
    @OneToMany(mappedBy = "idRacunDo")
    private List<Transakcija> transakcijaList;
    @OneToMany(mappedBy = "idRacunOd")
    private List<Transakcija> transakcijaList1;

    public Racun() {
    }

    public Racun(Integer idRacun) {
        this.idRacun = idRacun;
    }

    public Racun(Racun ra){
        this.idRacun = ra.getIdRacun();
        this.komitent = ra.getKomitent();
        this.potrazuje = ra.getPotrazuje();
        this.duguje = ra.getDuguje();
        this.status = ra.getStatus();
        this.timestamp = ra.getTimestamp();
        this.brojTransakcija = ra.getBrojTransakcija();
        this.dozvoljeniMinus = ra.getDozvoljeniMinus();
    }

    public Racun(Integer idRacun, int komitent, int potrazuje, int duguje, String status, Date timestamp, int brojTransakcija, int dozvoljeniMinus) {
        this.idRacun = idRacun;
        this.komitent = komitent;
        this.potrazuje = potrazuje;
        this.duguje = duguje;
        this.status = status;
        this.timestamp = timestamp;
        this.brojTransakcija = brojTransakcija;
        this.dozvoljeniMinus = dozvoljeniMinus;
    }

    public Integer getIdRacun() {
        return idRacun;
    }

    public void setIdRacun(Integer idRacun) {
        this.idRacun = idRacun;
    }

    public int getKomitent() {
        return komitent;
    }

    public void setKomitent(int komitent) {
        this.komitent = komitent;
    }

    public int getPotrazuje() {
        return potrazuje;
    }

    public void setPotrazuje(int potrazuje) {
        this.potrazuje = potrazuje;
    }

    public int getDuguje() {
        return duguje;
    }

    public void setDuguje(int duguje) {
        this.duguje = duguje;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getBrojTransakcija() {
        return brojTransakcija;
    }

    public void setBrojTransakcija(int brojTransakcija) {
        this.brojTransakcija = brojTransakcija;
    }

    public int getDozvoljeniMinus() {
        return dozvoljeniMinus;
    }

    public void setDozvoljeniMinus(int dozvoljeniMinus) {
        this.dozvoljeniMinus = dozvoljeniMinus;
    }

    @XmlTransient
    public List<Transakcija> getTransakcijaList() {
        return transakcijaList;
    }

    public void setTransakcijaList(List<Transakcija> transakcijaList) {
        this.transakcijaList = transakcijaList;
    }

    @XmlTransient
    public List<Transakcija> getTransakcijaList1() {
        return transakcijaList1;
    }

    public void setTransakcijaList1(List<Transakcija> transakcijaList1) {
        this.transakcijaList1 = transakcijaList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRacun != null ? idRacun.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.idRacun == null && other.idRacun != null) || (this.idRacun != null && !this.idRacun.equals(other.idRacun))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Racun[ idRacun=" + idRacun + " ]";
    }
    
}
