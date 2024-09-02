package app;

import app.config.HibernateConfig;
import app.daos.PackageDAO;
import app.entities.Package;
import app.enums.DeliveryStatus;

public class Main {
    public static void main(String[] args) {

        PackageDAO packageDAO = new PackageDAO(HibernateConfig.getEntityManagerFactory());
        packageDAO.createPackage(new Package("test", "test", "test", DeliveryStatus.DELIVERED));

    }
}