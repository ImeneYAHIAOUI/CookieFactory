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
-  [OrderStatusChange.feature](src/test/resources/features/order/OrderStatusChange.feature)
  - Client cancels order
#### [#5](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/5) Création des recettes

- En tant que Cuisinier, je veux ajouter une recette
- [CreateRecipe.feature](src/test/resources/features/store/CreateRecipe.feature)
  - Suggest recipe with name "Iced Cookie"
  - Cookie with  name "Iced Cookie" is accepted
  - Suggest recipe with name "Iced Cookie"
  - Cookie with  name "Iced Cookie" is declined

#### [#6](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/6) Changer les heures d'ouvertures et de fermeture du magasi
- En tant que Store manager, je veux changer les horaires du magasin
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
  - registered client cannot register 
  - client cannot register with the same email more than one time
  
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
-  [ModifyQuantityInCart.feature](src/test/resources/features/client/ModifyQuantityInCart.feature)
      - registred client can increase quantity
      - unregistred client can increase quantity
      - registred client can decrease quantity
      - unregistred client can decrease quantity
      - registred client can remove all items in cart
      - unregistred client can remove all items in cart
#####  [#13](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/13) Task Gestion des SMS
- En tant que client je veux recevoir un SMS quand ma commande est prête 
- [NotifyClient.feature](src/test/resources/features/order/NotifyClient.feature)
  - Order status is changed to "READY"
  - client in notified 
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
- En tant que Client non enregistré, je veux m'authentifier afin de pouvoir me connecter et me déconnecter
- [LogIn.feature](src/test/resources/features/client/LogIn.feature)
  - logIn with valid input
  - logIn with invalid password
  - logIn with invalid id

#### [#18](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/18) Appliquer la réduction, liée à l'US [#16](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/16)

- En tant que Client enregistré je veux pouvoir appliquer ma réduction de fidélité
- [GetDiscount.feature](src/test/resources/features/order/GetDiscount.feature)
  - Tous les scénarios du fichier

#### [#19](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/19) et [#21](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/21) Affichage des commandes passées
- Task associée : [#23](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/23)
- En tant que Client enregistré je veux pouvoir accéder à mon historique de commandes
- [RetrievePastOrders.feature](src/test/resources/features/client/RetrievePastOrders.feature)
    - get past orders
#### [#42](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/42) Ban user, task associée : [#41](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/41)
- En tant que client, si j'annule 2 commandes en moins de 8 minutes, alors je suis banni pendant 10 minutes
- [ChoosePartyCookie.feature](src/test/resources/features/cookie/ChoosePartyCookie.feature)
### Milestone 4
#### [#53](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/53) Choose store, task associée [#55]
- En tant que client je veux trouver les magasins les plus proches de ma position
- [getNearbyStores.feature](src/test/resources/features/store/getNearbyStores.feature)
  - get nearby stores
  - get stores with a specific distance
  - get stores with a specific distance 2
#### [#20](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/20) Choisir un horaire de récupération de la commande, Task associée : [#22](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/22)
- En tant que client, je veux choisir un horaire de récupération afin de pouvoir l'ajouter à ma commande
- [ChoosePickupTime.feature](src/test/resources/features/order/ChoosePickupTime.feature)
  - Choose pickup time
  - Choose pickup time not in the valid pickup time list
- [PayOrder.feature](src/test/resources/features/client/PayOrder.feature)
  - As a Client Pay an Order (no problems)
  - As a Client Pay an Order (already orders in the list)
### Milestone 5
#### Task [#51](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/51) Taxes magasins, associée à l'US [#50](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/50)
- En tant que Manager, je veux que mes clients payent une taxe au magasin pour chaque commande afin de pouvoir gagner plus d'argent
- [PayTax.feature](src/test/resources/features/order/PayTax.feature)
####  [56](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/56) Commander un Party Cookie avec une taille choisie, Task associée : [#52](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/52)
- En tant que Client, je veux pouvoir commander un Party Cookie avec une certaine taille
- [ChoosePartyCookie.feature](src/test/resources/features/cookie/ChoosePartyCookie.feature)
  - Choose an available party cookies
  - Choose party cookies with wrong size
  - Choose party cookies that doesn't exist
  - Choose party cookies with wrong amount
  - Choose many party cookies
  - More Ingredients are used for a party cookie size XL
  - More Ingredients are used for a party cookie size XXL
  - More Ingredients are used for a party cookie size L
  - Higher Price for a party cookie with size XL than a normal cookie
  - Higher Price for a party cookie with size XXL than a normal cookie
####  [#59](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/59) Commander un Party cookie avec un thème
- En tant que Client je veux commander un Party cookie avec un thème
- [ChoosePartyCookie.feature](src/test/resources/features/cookie/ChoosePartyCookie.feature)
  - Order a party cookie with a theme
  - Choose unavailable theme
  - Choose available theme
#### Task [#60](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/60) notify client once order is ready
- [notifyClient.feature](src/test/resources/features/client/notifyClient.feature)
  - client picks up command
  - client never picks up the order

### Milestone 6
#### Task [#66](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/66) Le client peut choisir un thème/ une occasion ou pas, liée à l'US [#59](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/59)
####  [#67](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/67) Too good to go : créer panier à partir des commandes obsolètes
- En tant que responsable du magasin, je veux créer des paniers avec les commandes obsolètes pour limiter le gaspillage et de gagner plus d'argent
- [TooGoodToGo.feature](src/test/resources/features/order/TooGoodToGo.feature)
  - Bag creation from obsolete orders
  - Bag creation but there are no orders
  - Bag creation with some obsolete orders and some not
#### [#86](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/86) Make an order for a later date, Task associée : [#87](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/87)
- En tant que Client, je veux pouvoir commander des cookies pour une date ultérieure, afin de pouvoir les récupérer plus tard
- [ChoosePickUpTimeForADate.feature](src/test/resources/features/order/ChoosePickUpTimeForADate.feature)
  - Choose pickup time
  - Choose pickup time not in the valid pickup time list
  - Choose pickup time on an invalid date
- [TimeSlotAttributionForADate.feature](src/test/resources/features/store/TimeSlotAttributionForADate.feature)
  - Cook can do the order
  - Cook can't do the order
  - Cancel order
####  [#90](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/90) Demander à être notifié des paniers Too Good To Go
- En tant que Client, je veux être notifié afin de pouvoir savoir quand des paniers cookies sont disponibles
- [TooGoodToGo.feature](src/test/resources/features/client/TooGoodToGo.feature)
   - only client who wants to be notified is notifies
   - notify client when order has staty Obselete

#### Task [#95](https://github.com/PNS-Conception/cookiefactory-22-23-o/issues/95) Verify if service “Personalized Cookies for your Party (PCP)” is available


   
   
