package com.example.model.tableperclass;

import javax.persistence.Entity;

/**
 * Livre — sous-classe de {@link Produit} (TABLE_PER_CLASS).
 *
 * <p>Table {@code Livre} complète : {@code id}, {@code nom} (hérités) et {@code auteur}.
 */
@Entity
public class Livre extends Produit {

    /** Auteur du livre. */
    private String auteur;

    /** Constructeur vide exigé par JPA. */
    public Livre() {}

    /**
     * @param nom    titre du livre
     * @param auteur auteur du livre
     */
    public Livre(String nom, String auteur) {
        super(nom);
        this.auteur = auteur;
    }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    @Override
    public String toString() {
        return "Livre [id=" + getId() + ", nom=" + getNom() + ", auteur=" + auteur + "]";
    }
}