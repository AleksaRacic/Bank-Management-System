/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class Container implements Serializable{
    private Object klasa;
    private String ime;
    private boolean merge;

        
    public Container(){
        
    }
    
    public Container(Object klasa, String ime) {
        this.klasa = klasa;
        this.ime = ime;
        this.merge = false;
    }

    public Object getKlasa() {
        return klasa;
    }

    public void setKlasa(Object klasa) {
        this.klasa = klasa;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge() {
        this.merge = true;
    }
    
    
}
