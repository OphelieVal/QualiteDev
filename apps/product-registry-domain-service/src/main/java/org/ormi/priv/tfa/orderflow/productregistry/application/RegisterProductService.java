package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d'application responsable de l'enregistrement d'un nouveau produit.
 *
 * Crée l'agrégat, persiste l'état et publie l'événement de création via l'outbox.
 */

@ApplicationScoped
public class RegisterProductService {
    
    private ProductRepository repository;
    private EventLogRepository eventLog;
    private OutboxRepository outbox;
    
    /**
     * Constructeur avec injection de dépendances.
     * @param repository
     * @param eventLog
     * @param outbox
     */
    @Inject
    public RegisterProductService(
        final ProductRepository repository,
        final EventLogRepository eventLog,
        final OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

    /**
     * Gère la commande d'enregistrement d'un produit.
     * @param cmd
     * @return
     * @throws IllegalArgumentException
     */
    @Transactional
    public ProductId handle(final RegisterProductCommand cmd) throws IllegalArgumentException {
        if (repository.existsBySkuId(cmd.skuId())) {
            throw new IllegalArgumentException(String.format("SKU already exists: %s", cmd.skuId()));
        }
        Product product = Product.create(
                cmd.name(),
                cmd.description(),
                cmd.skuId());
        // Save domain object
        repository.save(product);
        EventEnvelope<ProductRegistered> evt = EventEnvelope.with(new ProductRegistered(
            product.getId(), product.getSkuId(), cmd.name(), cmd.description()), product.getVersion());
        // Appends event to the log
        final EventLogEntity persistedEvent = eventLog.append(evt);
        // Publish outbox
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
        return product.getId();
    }

    // Access methods
    public ProductRepository getRepository() {
        return repository;
    }

    public void setRepository(final ProductRepository repository) {
        this.repository = repository;
    }

    public EventLogRepository getEventLog() {
        return eventLog;
    }

    public void setEventLog(final EventLogRepository eventLog) {
        this.eventLog = eventLog;
    }

    public OutboxRepository getOutbox() {
        return outbox;
    }

    public void setOutbox(final OutboxRepository outbox) {
        this.outbox = outbox;
    }
}
