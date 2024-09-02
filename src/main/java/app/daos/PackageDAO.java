package app.daos;

import app.enums.DeliveryStatus;
import app.config.HibernateConfig;
import app.entities.Package;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.hibernate.exception.ConstraintViolationException;

public class PackageDAO {

    EntityManagerFactory emf;

    public PackageDAO(EntityManagerFactory emf) {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    //    Persist a new package
    public Package createPackage(Package p) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        }
        return p;
    }

    //    Retrieve a package by its tracking number
    public Package getPackage(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.trackingNumber = :trackingNumber", Package.class);
            query.setParameter("trackingNumber", trackingNumber);
            Package found = query.getSingleResult();
            if (found == null) {
                throw new EntityNotFoundException();
            }
            return found;
        }
    }

    //    Update the delivery status of a package
    public Package updateDeliveryStatus(String trackingNumber, DeliveryStatus deliveryStatus) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.trackingNumber = :trackingNumber", Package.class);
            query.setParameter("trackingNumber", trackingNumber);
            Package p = query.getSingleResult();
            p.setDeliveryStatus(deliveryStatus);
            em.getTransaction().commit();
            return p;
        } catch (ConstraintViolationException e) {
            System.out.println("Constraint violation: " + e.getMessage());
            return null;
        }
    }

    //    Remove a package from the system
    public Package deletePackage(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.trackingNumber = :trackingNumber", Package.class);
            query.setParameter("trackingNumber", trackingNumber);
            Package p = query.getSingleResult();
            if (p != null) {
                em.getTransaction().begin();
                em.remove(p);
                em.getTransaction().commit();
                return p;
            }
        } catch (ConstraintViolationException e) {
            System.out.println("Constraint violation: " + e.getMessage());
        }
        return null;
    }
}
