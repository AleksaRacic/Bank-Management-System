/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.is1.klijent;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Lazar
 */

public class Klijent {
   
    public static void kreiraj_mesto(String name, int pb){
        try {

             String URLAddress = "http://localhost:8080/BankaServer/resources/mesto/post/" + name + "/" + pb;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("POST");
             connection.setDoInput(true);
             
             System.out.println("\n1\n");

            responseCode = connection.getResponseCode();
            System.out.println("\n2\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            System.out.println("\n3\n");
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            System.out.println("\n4\n");
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void kreiraj_filijalu(String name, String adresa, String mesto){
        try {
             
             String URLAddress = "http://localhost:8080/BankaServer/resources/filijala/post/" + name + "/" + adresa + "/" + mesto;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("POST");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void kreiraj_komitenta(String name, String adresa, String mesto){
        try {

             
             String URLAddress = "http://localhost:8080/BankaServer/resources/komitent/post/" + name + "/" + adresa + "/" + mesto;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("POST");
             connection.setDoInput(true);
             
            responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));

            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void promena_sedista_komitenta(int IdK, String mesto){
        try {
             
             String URLAddress = "http://localhost:8080/BankaServer/resources/patch/" + IdK +"/" + mesto;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("PUT");
             connection.setDoInput(true);
             
            responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
    public static void kreiraj_racun(int IdK, int dm){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/racun/post/" + IdK + "/" + dm;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("POST");
             connection.setDoInput(true);
             
             System.out.println("\n1\n");

            responseCode = connection.getResponseCode();
            System.out.println("\n2\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            System.out.println("\n3\n");
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            System.out.println("\n4\n");
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static void dohvati_sva_mesta(){
        try {

             
             String URLAddress = "http://localhost:8080/BankaServer/resources/mesto/get";
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);
             
            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void dohvati_sve_filijale(){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/filijala/get";
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void dohvati_sve_komitente(){
        try {
             
             String URLAddress = "http://localhost:8080/BankaServer/resources/komitent/get";
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void dohvati_racune(int IdK){
        try {
             
             String URLAddress = "http://localhost:8080/BankaServer/resources/racun/get/" + IdK;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);
             
            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void dohvati_transakcije(int IdR){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/transakcija/get/" + IdR;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void dohvati_razliku(){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/backup/diff";
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void dohvati_backup(){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/backup/all";
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void zatvori_racun(int idr){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/racun/patch/"+idr;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("PATCH");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void prenos(int racunod, int racundo, int iznos, String svrha){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/post/prenos/"+racunod+"/"+racundo+"/"+iznos+"/"+svrha;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void uplata(int filijala, int racundo, int iznos, String svrha){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/post/uplata/"+filijala+"/"+racundo+"/"+iznos+"/"+svrha;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void isplata(int filijala, int racunod, int iznos, String svrha){
        try {
             String URLAddress = "http://localhost:8080/BankaServer/resources/post/prenos/"+filijala+"/"+racunod+"/"+iznos+"/"+svrha;
             String inputString = null;
             int responseCode = 0;
             
             URL url = new URL(URLAddress);
             
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             
             connection.setRequestMethod("GET");
             connection.setDoInput(true);

            responseCode = connection.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
              System.out.println("\n\n" + inputString + "\n\n");
            }
            in.close();
            
            } catch (MalformedURLException ex) {
             Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.println("Izaberite opciju:\n" +
                    "1. Kreiranje mesta\n" +
                    "2. Kreiranje filijale u mestu\n" +
                    "3. Kreiranje komitenta\n" +
                    "4. Promena sedišta za zadatog komitenta\n" +
                    "5. Otvaranje racuna\n" +
                    "6. Zatvaranje racuna\n" +
                    "7. Kreiranje transakcije koja je prenos sume sa jednog racuna na drugi racun\n" +
                    "8. Kreiranje transakcije koja je uplata novca na racun\n" +
                    "9. Kreiranje transakcije koja je isplata novca sa racuna\n" +
                    "10. Dohvatanje svih mesta\n" +
                    "11. Dohvatanje svih filijala\n" +
                    "12. Dohvatanje svih komitenata\n" +
                    "13. Dohvatanje svih racuna za komitenta\n" +
                    "14. Dohvatanje svih transakcija za racun\n" +
                    "15. Dohvatanje svih podataka iz rezervne kopije\n" +
                    "16. Dohvatanje razlike u podacima u originalnim podacima i u rezervnoj kopiji\n");
            
            int br = scan.nextInt();
            scan.nextLine();
             
            //System.out.println("\n" + br + "\n");
            
            switch(br){
                case 1:{
                    System.out.println("Unesite ime mesta: ");
                    String s1 = scan.nextLine().replace(" ", "%20");
                    System.out.println("Unesite postanski broj mesta: ");
                    int pb = scan.nextInt();
                    kreiraj_mesto(s1, pb);
                    break;
                }
                case 2:{
                    System.out.println("Unesite ime filijale: ");
                    String s1 = scan.nextLine().replace(" ", "%20");
                    System.out.println("Unesite adresu filijale: ");
                    String s2 = scan.nextLine().replace(" ", "%20");
                    System.out.println("Unesite ime mesta u kojoj otvarate filijalu: ");
                    String s3 = scan.nextLine().replace(" ", "%20");
                    kreiraj_filijalu(s1, s2, s3);
                    break;
                }
                case 3:{
                    System.out.println("Unesite ime komitenta: ");
                    String s1 = scan.nextLine().replace(" ", "%20");
                    System.out.println("Unesite adresu komitenta: ");
                    String s2 = scan.nextLine().replace(" ", "%20");
                    System.out.println("Unesite ime mesta u kojoj kreirate komitenta: ");
                    String s3 = scan.nextLine().replace(" ", "%20");
                    kreiraj_komitenta(s1, s2, s3);
                    break;
                }
                case 4:{
                    System.out.println("Unesite Id komitenta: ");
                    int s1 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite ime novog sedista komitenta: ");
                    String s3 = scan.nextLine().replace(" ", "%20");
                    promena_sedista_komitenta(s1, s3);
                    break;
                }
                case 5:{
                    System.out.println("Unesite dozvoljeni minus racuna: ");
                    int s1 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite ID komitenta koji otvara racun: ");
                    int s3 = scan.nextInt();
                    scan.nextLine();
                    kreiraj_racun(s3, s1);
                    break;
                }
                case 6:{
                    System.out.println("Unesite broj racuna: ");
                    int s1 = scan.nextInt();
                    zatvori_racun(s1);
                    break;
                }
                case 7:{
                    System.out.println("Unesite broj racuna sa kojeg se isplacuje: ");
                    int s1 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite broj racuna na koji se upkacuje: ");
                    int s2 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite iznos: ");
                    int s3 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite svrhu: ");
                    String s4 = scan.nextLine().replace(" ", "%20");
                    scan.nextLine();
                    prenos(s1, s2, s3, s4);
                    break;
                }
                case 8:{
                    System.out.println("Unesite broj filijale: ");
                    int s1 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite broj racuna na koji se upkacuje: ");
                    int s2 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite iznos: ");
                    int s3 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite svrhu: ");
                    String s4 = scan.nextLine().replace(" ", "%20");
                    scan.nextLine();
                    uplata(s1, s2, s3, s4);
                    break;
                }
                case 9:{
                    System.out.println("Unesite broj filijale: ");
                    int s1 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite broj racuna sa kojeg se isplacuje: ");
                    int s2 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite iznos: ");
                    int s3 = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Unesite svrhu: ");
                    String s4 = scan.nextLine().replace(" ", "%20");
                    scan.nextLine();
                    prenos(s1, s2, s3, s4);
                    break;
                }
                case 10 :{
                    dohvati_sva_mesta();
                    break;
                }
                case 11 :{
                    dohvati_sve_filijale();
                    break;
                }
                case 12 :{
                    dohvati_sve_komitente();
                    break;
                }
                case 13:{
                    System.out.println("Unesite Id komitenta: ");
                    int s1 = scan.nextInt();
                    scan.nextLine();
                    dohvati_racune(s1);
                    break;
                }
                case 14:{
                    System.out.println("Unesite Id racuna: ");
                    int s1 = scan.nextInt();
                    scan.nextLine();
                    dohvati_transakcije(s1);
                    break;
                    
                }
                case 15 :{
                    dohvati_razliku();
                    break;
                }
                case 16 :{
                    dohvati_backup();
                    break;
                }
                default:{
                    break;
                }
            }
            
        }
         
    }
}
