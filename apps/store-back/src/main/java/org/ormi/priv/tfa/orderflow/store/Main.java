package org.ormi.priv.tfa.orderflow.store;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Classe de démarrage pour l'application store-back.
 *
 * Point d'entrée Quarkus utilisé pour lancer l'application en mode natif ou dev.
 */

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(
            ProductRegistryDomainApplication.class,
            (exitCode, exception) -> { },
            args);
    }

    public static class ProductRegistryDomainApplication implements QuarkusApplication {

        @Override
        /**
         * Démarre l'application et attend la terminaison.
         * @param args arguments de la ligne de commande (non utilisés)
         */
        public int run(final String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
