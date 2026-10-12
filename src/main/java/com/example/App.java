package com.example;

import com.example.model.joined.Developpeur;
import com.example.model.joined.Employe;
import com.example.model.joined.Manager;
import com.example.model.singletable.Moto;
import com.example.model.singletable.Vehicule;
import com.example.model.singletable.Voiture;
import com.example.model.tableperclass.Electronique;
import com.example.model.tableperclass.Livre;
import com.example.model.tableperclass.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Démonstration des trois stratégies d'héritage JPA :
 * création (CREATE), lecture polymorphique (READ), modification (UPDATE)
 * et suppression (DELETE). Observer le SQL généré dans la console.
 */
public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-inheritance");
        EntityManager em = emf.createEntityManager();
        try {
            // ---------- CREATE ----------
            System.out.println("\n===== CRÉATION =====");
            em.getTransaction().begin();

            // SINGLE_TABLE
            Voiture voiture = new Voiture("Toyota", 4);
            Moto moto = new Moto("Harley", true);
            em.persist(voiture);
            em.persist(moto);

            // JOINED
            Developpeur dev = new Developpeur("Alice", "Java");
            Manager manager = new Manager("Bob", 3);
            em.persist(dev);
            em.persist(manager);

            // TABLE_PER_CLASS
            Livre livre = new Livre("Clean Code", "Robert C. Martin");
            Electronique electronique = new Electronique("Smartphone", 24);
            em.persist(livre);
            em.persist(electronique);

            em.getTransaction().commit();
            em.clear(); // vide le cache : les lectures suivantes vont vraiment en base

            // ---------- READ (requêtes polymorphiques) ----------
            System.out.println("\n--- Liste des véhicules (SINGLE_TABLE : 1 seule table) ---");
            em.createQuery("SELECT v FROM Vehicule v", Vehicule.class).getResultList()
                    .forEach(v -> System.out.println(v.getClass().getSimpleName() + ": " + v.getMarque()));

            System.out.println("\n--- Liste des employés (JOINED : jointures) ---");
            em.createQuery("SELECT e FROM Employe e", Employe.class).getResultList()
                    .forEach(e -> System.out.println(e.getClass().getSimpleName() + ": " + e.getNom()));

            System.out.println("\n--- Liste des produits (TABLE_PER_CLASS : UNION) ---");
            em.createQuery("SELECT p FROM Produit p", Produit.class).getResultList()
                    .forEach(p -> System.out.println(p.getClass().getSimpleName() + ": " + p.getNom()));

            // ---------- UPDATE ----------
            System.out.println("\n===== MODIFICATION =====");
            em.getTransaction().begin();
            Developpeur devRelu = em.find(Developpeur.class, dev.getId());
            devRelu.setLangage("Kotlin"); // dirty checking : pas d'appel à update()
            em.getTransaction().commit();
            System.out.println("Après modification : " + devRelu);

            // ---------- DELETE ----------
            System.out.println("\n===== SUPPRESSION =====");
            em.getTransaction().begin();
            em.remove(em.find(Moto.class, moto.getId()));
            em.getTransaction().commit();
            Long nbVehicules = em.createQuery("SELECT COUNT(v) FROM Vehicule v", Long.class).getSingleResult();
            System.out.println("Véhicules restants : " + nbVehicules);

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}