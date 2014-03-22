# { développeur: "viens découvrir mongoDB"} #

Le NoSQL est de plus en plus présent dans les projets Java. Avec plus de 5 millions de téléchargements et plus de 600 grand comptes : MongoDB fait parti des bases leader dans le domaine des bases de données Big Data.

Dans cet atelier pratique, ludique et dynamique : Vous, développeur Java, venez découvrir comment utiliser MongoDB et comment l'intégrer dans un site web.

On partira du shell, on passera à l'utilisation de drivers MongoDB en Java, puis on intégrera l'ensemble dans un site web.

Création du repository local pour accès offline
---

    mvn clean install -Dmaven.local.repository=./repository

les dependencies seront téléchargées (même si vous les avez déjà dans votre repository local)
Lors d'un lancement offline, c'est ce repository qui sera utilisé (cf configuration repository pom.xml)
__Attention :__ La surcharge du repository maven central est en commentaire ! Il faut décommenter pour
que maven évite de taper le central online.

Requirements
----

- Java
- Maven 3
- Mongo 2.6
- Maven
- git
