package org.ormi.priv.tfa.orderflow.productregistry;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Classe de démarrage pour le service de domaine du product-registry.
 *
 * Permet l'exécution via Quarkus (`quarkus:dev` ou exécutable).
 */

@QuarkusMain
public class Main {

    public static void main(final String... args) {
        Quarkus.run(
            ProductRegistryDomainApplication.class,
            (exitCode, exception) -> { },
            args);
    }

    public static class ProductRegistryDomainApplication implements QuarkusApplication {

        @Override
        /**
         * Démarre l'application et attend l'arrêt de Quarkus.
         * @param args arguments de la ligne de commande (non utilisés).
         */
        public int run(final String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
