package com.descodeuses.planit;

class Persone {
    private int age;
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
    private String prenom;
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}

class Apprenante extends Persone{
    private String nomAnneePromo;
    public String getNomAnneePromo() {
        return nomAnneePromo;
    }

    public void setNomAnneePromo(String nomAnneePromo) {
        this.nomAnneePromo = nomAnneePromo;
    }
 
}


class TestJava {
    public static void main(String[] args){
        Apprenante descodeuses = new Apprenante();
        descodeuses.setAge(10);
        descodeuses.setPrenom("Amena");
        descodeuses.setNomAnneePromo("Promo Danielle");
        System.out.println(" age: "+descodeuses.getAge()+" ,Name: "+descodeuses.getPrenom()+"Promo: "+descodeuses.getNomAnneePromo());

        int somme = 1+1;
        System.out.println("Resultat: "+somme);
    }
}