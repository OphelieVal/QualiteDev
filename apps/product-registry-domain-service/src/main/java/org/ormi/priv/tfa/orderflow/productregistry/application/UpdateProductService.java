package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductDescriptionCommand;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductNameCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d'application pour les commandes de mise à jour de produit.
 *
 * Traite les commandes de changement de nom ou de description, en enregistrant
 * l'agrégat, en persistants l'événement et en publiant via l'outbox.
 */

@ApplicationScoped
public class UpdateProductService {

    private ProductRepository repository;
    private EventLogRepository eventLog;
    private OutboxRepository outbox;

    @Inject
    public UpdateProductService(
        final ProductRepository repository,
        final EventLogRepository eventLog,
        final OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

    /**
     * Gère la commande de mise à jour du nom d'un produit.
     * @param cmd
     * @throws IllegalArgumentException
     */
    @Transactional
    public void handle(final UpdateProductNameCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductNameUpdated> event = product.updateName(cmd.newName());
        // Save domain object
        repository.save(product);
        // Append event to event log
        final EventLogEntity persistedEvent = eventLog.append(event);
        // Publish event to outbox
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }

    /**
     * Gère la commande de mise à jour de la description d'un produit.
     * @param cmd
     * @throws IllegalArgumentException
     */
    @Transactional
    public void handle(final UpdateProductDescriptionCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductDescriptionUpdated> event = product.updateDescription(cmd.newDescription());
        // Save domain object
        repository.save(product);
        // Append event to event log
        final EventLogEntity persistedEvent = eventLog.append(event);
        // Publish event to outbox
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }

    public ProductRepository getRepository() {
        return repository;
    }

    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }

    public EventLogRepository getEventLog() {
        return eventLog;
    }

    public void setEventLog(EventLogRepository eventLog) {
        this.eventLog = eventLog;
    }

    public OutboxRepository getOutbox() {
        return outbox;
    }

    public void setOutbox(OutboxRepository outbox) {
        this.outbox = outbox;
    }
}
