package com.example.model.joined;

import javax.persistence.Entity;

/**
 * Manager — sous-classe de {@link Employe} (JOINED).
 *
 * <p>Table {@code Manager} : {@code id} (clé étrangère vers {@code Employe})
 * et {@code nombreEquipes}.
 */
@Entity
public class Manager extends Employe {

    /** Nombre d'équipes encadrées. Un {@code int} suffit : la colonne n'est jamais vide ici. */
    private int nombreEquipes;

    /** Constructeur vide exigé par JPA. */
    public Manager() {}

    /**
     * @param nom           nom du manager
     * @param nombreEquipes nombre d'équipes encadrées
     */
    public Manager(String nom, int nombreEquipes) {
        super(nom);
        this.nombreEquipes = nombreEquipes;
    }

    public int getNombreEquipes() { return nombreEquipes; }
    public void setNombreEquipes(int nombreEquipes) { this.nombreEquipes = nombreEquipes; }

    @Override
    public String toString() {
        return "Manager [id=" + getId() + ", nom=" + getNom() + ", equipes=" + nombreEquipes + "]";
    }
}