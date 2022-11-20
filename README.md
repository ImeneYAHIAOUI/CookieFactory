# Cookiefactory-22-23-Team-#template
_Template for classroom SI4-COO_

## doc
Contient le rapport final

## .github
   1. Contient sous workflows/maven.yml, une version d'un fichier d'actions qui est déclenché dès que vous poussez du code. 
Sur cette version initiale seule un test Junit5 est déclenché pour vérifier que tout fonctionne.
       - Github Actions (See in .github/workflows) to simply make a maven+test compilation
  2. Contient sous ISSUE_TEMPLATE, les modèles pour les issues user_story et bug. Vous pouvez le compléter à votre guise.

## src
 - pom.xml : 
       - Cucumber 7 et JUnit 5
       - Maven compatible
       - JDK 17



## User stories 
La liste des fonctionnalités livrées par user story.

### Milestone X

Chaque user story doit être décrite par 
   - son identifiant en tant que issue github (#), 
   - sa forme classique (As a… I want to… In order to…) (pour faciliter la lecture)
   - Le nom du fichier feature Cucumber et le nom des scénarios qui servent de tests d’acceptation pour la story.
   Les contenus détaillés sont dans l'issue elle-même.

### Milestone 1 
#### [#1](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/1) Commander un seul cookie 
- En tant que client je veux commander un cookie
- ?
#### [#2](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/2) Récupération de la commande
- En tant que cuisinier je veux changer le statut de la commande
- [OrderStatusChange.feature](src/test/resources/features/order/OrderStatusChange.feature)
  - order status is "in progress" and we want to cancel it
  - order status is "Ready" and we want to cancel it
  - order status is "completed" and we want to cancel it
  - order status is "in progress" and we want to pass it to "ready"
  - order status is "ready" and we want to pass it to "completed"
  - order status is "in progress" and we want to pass it to "completed"
#### [#3](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/3) Annuler une commande
- En tant que client je veux annuler ma commande
- [MakeOrder.feature](src/test/resources/features/order/MakeOrder.feature)
  - Client cancels order
#### [#5](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/5) Création des recettes
- En tant que Cuisinier, Je veux ajouter une recette
- [CreateRecipe.feature](src/test/resources/features/store/CreateRecipe.feature)
  - Suggest recipe with name "Iced Cookie"
  - Cookie with  name "Iced Cookie" is accepted

#### [#6](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/6) Changer les heures d'ouvertures et de fermeture du magasin
- En tant que Store manager, Je veux changer les horaires du magasin
- [ChangeOpeningHours.feature](src/test/resources/features/store/ChangeOpeningHours.feature)
  - Change Opening Hours to a New Opening Time and to a New Closing Time

### Milestone 2
#### [#7](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/7) Modifier l’inventaire et les tâches associées #9 et #11
- As a Manager, I want to modify the inventory so that the amount of the chosen ingredients will be modified
- [ManageInventory.feature](src/test/resources/features/store/ManageInventory.feature)
  - Add a new product as a Store manager
  - Add a new product as a Store manager BUT already in inventory
  - Subtract quantity under 0
#### [#8](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/8) Création de nouveaux comptes
- En tant que client non enregistré, je veux pouvoir me créer un compte
- [Register.feature](src/test/resources/features/client/Register.feature)
  - Register 1 client
#### [#10](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/10) Choisir un cookie 
- En tant que client je veux choisir un cookie afin de pouvoir le commander s'il y a suffisament d'ingrédients
- [ChooseCookie.feature](src/test/resources/features/cookie/ChooseCookie.feature)
  - choose an available cookies 
  - choose an available number of cookies
  - choose an unavailable amount of cookie
  - choose same cookie twice
  - choose an unavailable cookie
#### [#12](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/12) Modifier la quantité d'un cookie
- En tant que client je veux modifier la quantité de cookie dans mon panier
-  ?
#####  [#13](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/13) Task Gestion des SMS
- En tant que client je veux recevoir un SMS quand ma commande est prête 
- [MakeOrder.feature](src/test/resources/features/order/MakeOrder.feature)
  - Order status is changed to "READY"
### Milestone 3
#### [#15](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/15) Task Ajout du Time slot dans les Cook liée à US [#1](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/1)
- [TimeSlotAttribution.feature](src/test/resources/features/store/TimeSlotAttribution.feature)
- Cook can do the order
- Cancel order
#### [#16](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/16) Paiement du client
- En tant que Client, je veux payer ma commande
- [PayOrder.feature](src/test/resources/features/client/PayOrder.feature)
  - As a Client Pay an Order (no problems)
  - As a Client Pay an Order (already orders in the list)
#### [#17](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/17) Authentification
- En tant que Client non enregistré, Je veux m'authentifier afin de pouvoir me connecter et me déconnecter
- [LogIn.feature](src/test/resources/features/client/LogIn.feature)
  - logIn with valid input
  - logIn with invalid password
  - logIn with invalid id
#### [#18](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/18) Appliquer la réduction lié à l'US [#16](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/16)
- En tant que Client enregistré je veux pouvoir appliquer ma réduction de fidélité
- [GetDiscount.feature](src/test/resources/features/order/GetDiscount.feature)
  - Tous les scénarios du fichier

#### [#19](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/19) et [#21](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/21) Affichage des commandes passées
- Task associée : [#23](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/23)
- En tant que Client enregistré je veux pouvoir accéder à mon historique de commandes
- [RetrievePastOrders.feature](src/test/resources/features/client/RetrievePastOrders.feature)
    - get past orders
#### [#42](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/42) Ban user, task associée : [#41](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/41)
- As a client, if I cancel two orders in less than 8 minutes then i get ban for 10 minutes
- ?
### Milestone 4

### Milestone 5

   
   
