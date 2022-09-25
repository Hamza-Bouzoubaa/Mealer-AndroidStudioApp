# Project Mealer - Group 8

## Getting started

To get started, you will need `Android Studio`. Once you've opened the project, please accept any requests to download plugins. This project currently depends on the [Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions) and [Lombok](https://plugins.jetbrains.com/plugin/6317-lombok).

[Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions) ensures unified imports and code formating is applied to all contributed code.

[Lombok](https://plugins.jetbrains.com/plugin/6317-lombok) creates Getters, Setters, and other tedious methods automatically through the use of `Annotations`. A quick read of the documentation can be very useful.

Once your IDE is setup, see [Contributions](#contributions) for information on how to contribute to the project.

## Login Architecture

### Api (Firebase)

User experience:

```mermaid
stateDiagram-v2
    direction LR

    check_sign_in : Check sign in status
    sign_in : Single Sign On
    home : Home Screen
    setup_account : Setup Account

    [*] --> check_sign_in

    check_sign_in --> sign_in: If not signed in
    check_sign_in --> home: If signed in

    sign_in --> home

    home --> setup_account : If user account details missing
    setup_account --> home

    home --> [*]
```

We use `FirebaseUI` for Signle Sign On. This enables users to use multiple account providers with Mealer.

Setup Account populates Firebase's `Cloud Firestore` with data.

Architecture:

<b style="color:red">Not all methods are implemented yet. Please add it if you need it.</b>

```mermaid
classDiagram
    class MealerClient {
        <<singleton>>
        User user

        public MealerUser getUser()
        public MealerRole getRole()
    }

    class MealerUser {
        String firstName
        String lastName
        String email
        String profilePictureUrl
        String voidCheckUrl
        String address
        String biography
        String creditCard
        String menuId
        MealerRole role
        int[] ratings
        int totalSales
    }

    class MealerRole {
        <<Enumeration>>
        USER,
        CHEF,
        ADMIN
    }

    class Menu {
        String chefId
        List~Recipe~ recipes
        int[] ratings
    }

    class Recipe {
        String name
        String course
        String[] categories
        String[] ingredients
        String[] allergens
        float price
        String description
    }
```

## Contributions

<b style="color:red">Ne contribuer pas directement à main!</b>

```mermaid
gitGraph
    commit id: "Create project"
    branch login-support
    commit id: "Add login"
    commit id: "Tweek login"
    checkout main
    merge login-support tag: "Merge login PR"
    branch firebase-support
    commit id: "Add Firebase"
    checkout main
    merge firebase-support tag: "Merge Firebase PR"

```

Nous utilisons des [Pull Request](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request) pour contribuer. Quelqu'un de l'équipe peut te montrer comment faire.

## Firebase

Nous utilisons [Firebase](https://firebase.google.com/docs/reference/android/packages) comme base de données. Envoyer un message à quelqu'un de l'equipe pour avoir access au projet Firebase.
