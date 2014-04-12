# { développeur: "viens découvrir mongoDB"} #

Le NoSQL est de plus en plus présent dans les projets Java. Avec plus de 5 millions de téléchargements et plus de 600 grand comptes : MongoDB fait parti des bases leader dans le domaine des bases de données Big Data.

Dans cet atelier pratique, ludique et dynamique : Vous, développeur Java, venez découvrir comment utiliser MongoDB et comment l'intégrer dans un site web.

On partira du shell, on passera à l'utilisation de drivers MongoDB en Java, puis on intégrera l'ensemble dans un site web.

Installation de mongodb
---

Dézipper mongodb dans un répertoire. Puis ajouter le répertoire ```bin``` de mongodb dans votre PATH.
Pour vérifier votre installaton :

    mongo --version

Lancer le serveur via maven
----
mvn exec:java -Dexec.mainClass="com.github.mongo.labs.Main"

Création du repository local pour accès offline
---

    mvn clean install dependency:go-offline -Dmaven.repo.local=repository -Pdev

+ d'information ici : http://www.aheritier.net/launch-a-maven-build-with-a-temporary-empty-local-repository/

Requirements
----

- Java 7
- Mongo 2.6
- Git
- Maven 3


