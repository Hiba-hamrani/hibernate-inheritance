package com.example.model.singletable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Moto — sous-classe de {@link Vehicule} (SINGLE_TABLE).
 *
 * <p>Stockée dans la table {@code Vehicule} avec {@code vehicule_type = 'MOTO'}.
 * Sa colonne propre {@code sidecar} reste vide (NULL) sur les lignes des voitures.
 */
@Entity
@DiscriminatorValue("MOTO")
public class Moto extends Vehicule {

    /**
     * Présence d'un side-car. En {@code Boolean} (et non {@code boolean}) car la
     * colonne doit accepter NULL pour les lignes des voitures.
     */
    private Boolean sidecar;

    /** Constructeur vide exigé par JPA. */
    public Moto() {}

    /**
     * @param marque  marque de la moto
     * @param sidecar {@code true} si la moto a un side-car
     */
    public Moto(String marque, Boolean sidecar) {
        super(marque);
        this.sidecar = sidecar;
    }

    public Boolean getSidecar() { return sidecar; }
    public void setSidecar(Boolean sidecar) { this.sidecar = sidecar; }

    @Override
    public String toString() {
        return "Moto [id=" + getId() + ", marque=" + getMarque() + ", sidecar=" + sidecar + "]";
    }
}