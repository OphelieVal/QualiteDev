## Responsabilités dans l'équipe

**Équipe architecture et domaine métier** :
- versionnage, cohérence entre les microservices
- appliquer le CQRS, garantir les règles métiers

**Équipe tests et qualité** :
- gestion de la base de données et migrations (entité JPA)
- tests unitaires, d’intégration et couverture des tests
- automatisation et documentation

## Règles de collaboration
### Branches principales 
- `main` : branche de production, code stable et testé
- `develop` : branche de développement, intégration des fonctionnalités en cours

### Branches de fonctionnalités
- Créer une branche à partir de `develop` nommée `feature/nom-fonctionnalité`
- Faire des commits avec des messages clairs 

### Corrections de bugs
- Créer une branche à partir de `develop` nommée `fix/description-bug`


### Convention des commits
- Utiliser le format : `type(scope): description`
  - Types : `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
  - Exemple : `feat(auth): ajouter la validation des tokens JWT`

