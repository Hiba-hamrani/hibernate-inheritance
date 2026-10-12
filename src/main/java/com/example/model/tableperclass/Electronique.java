package com.example.model.tableperclass;

import javax.persistence.Entity;

/**
 * Produit électronique — sous-classe de {@link Produit} (TABLE_PER_CLASS).
 *
 * <p>Table {@code Electronique} complète : {@code id}, {@code nom} (hérités) et {@code garantie}.
 */
@Entity
public class Electronique extends Produit {

    /** Durée de la garantie, en mois. */
    private int garantie;

    /** Constructeur vide exigé par JPA. */
    public Electronique() {}

    /**
     * @param nom      nom du produit
     * @param garantie durée de garantie en mois
     */
    public Electronique(String nom, int garantie) {
        super(nom);
        this.garantie = garantie;
    }

    public int getGarantie() { return garantie; }
    public void setGarantie(int garantie) { this.garantie = garantie; }

    @Override
    public String toString() {
        return "Electronique [id=" + getId() + ", nom=" + getNom() + ", garantie=" + garantie + " mois]";
    }
}