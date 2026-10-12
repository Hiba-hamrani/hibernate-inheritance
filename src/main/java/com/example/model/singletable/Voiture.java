package com.example.model.singletable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Manager — sous-classe de {@link Vehicule} (SINGLE_TABLE).
 *
 * <p>Stockée dans la table {@code Vehicule} avec {@code vehicule_type = 'VOITURE'}.
 * Sa colonne propre {@code nombrePortes} reste vide (NULL) sur les lignes des motos.
 */
@Entity
@DiscriminatorValue("VOITURE")
public class Voiture extends Vehicule {

    /**
     * Nombre de portes. En {@code Integer} (et non {@code int}) car, dans une table
     * unique, la colonne doit accepter NULL pour les lignes des motos.
     */
    private Integer nombrePortes;

    /** Constructeur vide exigé par JPA. */
    public Voiture() {}

    /**
     * @param marque       marque de la voiture
     * @param nombrePortes nombre de portes
     */
    public Voiture(String marque, Integer nombrePortes) {
        super(marque);
        this.nombrePortes = nombrePortes;
    }

    public Integer getNombrePortes() { return nombrePortes; }
    public void setNombrePortes(Integer nombrePortes) { this.nombrePortes = nombrePortes; }

    @Override
    public String toString() {
        return "Manager [id=" + getId() + ", marque=" + getMarque() + ", portes=" + nombrePortes + "]";
    }
}