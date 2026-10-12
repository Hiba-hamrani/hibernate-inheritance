package com.example.model.joined;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Développeur — sous-classe de {@link Employe} (JOINED).
 *
 * <p>Table {@code Developpeur} : {@code id} (clé étrangère vers {@code Employe})
 * et {@code langage}. Comme la table ne contient que des développeurs, la colonne
 * propre peut être rendue obligatoire — impossible en SINGLE_TABLE.
 */
@Entity
public class Developpeur extends Employe {

    /** Langage principal du développeur, obligatoire. */
    @Column(nullable = false)
    private String langage;

    /** Constructeur vide exigé par JPA. */
    public Developpeur() {}

    /**
     * @param nom     nom du développeur
     * @param langage langage principal
     */
    public Developpeur(String nom, String langage) {
        super(nom);
        this.langage = langage;
    }

    public String getLangage() { return langage; }
    public void setLangage(String langage) { this.langage = langage; }

    @Override
    public String toString() {
        return "Developpeur [id=" + getId() + ", nom=" + getNom() + ", langage=" + langage + "]";
    }
}