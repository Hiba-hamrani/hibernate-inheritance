package com.example.model.tableperclass;

import javax.persistence.*;

/**
 * Classe mère de la hiérarchie des produits — stratégie <b>TABLE_PER_CLASS</b>.
 *
 * <p>Il n'y a <b>pas de table {@code Produit}</b> : chaque classe concrète
 * ({@link Livre}, {@link Electronique}) a sa propre table complète, qui répète
 * les colonnes communes ({@code id}, {@code nom}). Une requête sur
 * {@code Produit} est traduite par un {@code UNION} de ces tables.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Produit {

    /**
     * Clé primaire. <b>Pas d'IDENTITY ici</b> : chaque table aurait son propre
     * compteur, et un Livre et un Electronique pourraient avoir le même id,
     * ce qui casserait les requêtes sur Produit. {@code AUTO} utilise une
     * séquence commune à toute la hiérarchie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Nom du produit, obligatoire. */
    @Column(nullable = false)
    private String nom;

    /** Constructeur vide exigé par JPA. */
    public Produit() {}

    /** @param nom nom du produit */
    public Produit(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}