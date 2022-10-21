# Project Mealer - Group 8

## Getting started

To get started, you will need `Android Studio`. Once you've opened the project, please accept any requests to download plugins. This project currently depends on the [Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions).

[Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions) ensures unified imports and code formating is applied to all contributed code.

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
    MealerSerializable <|-- MealerUser
    MealerSerializable <|-- MealerMenu
    MealerSerializable <|-- MealerRecipe
    MealerSerializable <|-- MealerOrder
    MealerSerializable <|-- MealerMenu
    MealerSerializable *-- MealerSerializableElement

    MealerClient <|-- FirebaseClient

    Services "1" --> MealerClient

    MealerOrder "1" --> "1" MealerOrderStatus

    MealerUser "1" --> "*" MealerRole


    class MealerClient {
        <<interface>>
        +getUser(String id)* Future~MealerUser~
        +getUser()* Future~MealerUser~
        +getUserMenu(String id)* Future~MealerMenu~
        +getUserMenu()* Future~MealerMenu~
        +getMenu(String id)* Future~MealerMenu~
        +getRecipe(String id)* Future~MealerRecipe~
        +updateRecipe(MealerRecipe recipe)* Future~Void~
        +updateMenu(MealerMenu menu)* Future~Void~
        +updateUser(MealerUser user)* Future~Void~
        +userInfoRequired()* Future~Boolean~
    }

    class FirebaseClient {
        <<interface>>
        +getUser(String id) Future~MealerUser~
        +getUser() Future~MealerUser~
        +getUserMenu(String id) Future~MealerMenu~
        +getUserMenu() Future~MealerMenu~
        +getMenu(String id) Future~MealerMenu~
        +getRecipe(String id) Future~MealerRecipe~
        +updateRecipe(MealerRecipe recipe) Future~Void~
        +updateMenu(MealerMenu menu) Future~Void~
        +updateUser(MealerUser user) Future~Void~
        +userInfoRequired() Future~Boolean~
    }

    class Services {
        <<Singleton>>
        #MealerClient database
        +getDatabaseClient() MealerClient
    }

    class MealerUser {
        #String firstName
        #String lastName
        #String email
        #String profilePictureUrl
        #String voidCheckUrl
        #String address
        #String biography
        #String creditCard
        #String menuId
        #List~MealerRole~ role
        #List~Integer~ ratings
        #int totalSales
    }

    class MealerRole {
        <<Enumeration>>
        USER,
        CHEF,
        ADMIN
    }

    class MealerMenu {
        String chefId
        List~Recipe~ recipes
        List~Integer~ ratings
    }

    class MealerRecipe {
        String name
        String course
        List~String~ categories
        List~String~ ingredients
        List~String~ allergens
        float price
        String description
    }

    class MealerOrder {

    }

    class MealerOrderStatus {
        <<Enumeration>>
        IN_PROGRESS,
        COMPLETE,
        ACCEPTED,
        REJECTED,
        CANCELED
    }

    class MealerSerializable {
        <<@Interface>>
    }

    class MealerSerializer {
        +toMap(Object o)$ Map
        +isSerializable(Object o)$ bool
    }

    class MealerSerializableElement {
        <<@Interface>>
        +key() String
    }
```

```mermaid
classDiagram
    class ActivityUtils {

    }

    class HomeActivity {

    }

    class MainActivity {

    }

    class SignInActivity {

    }

    class SignInWithEmailActivity {

    }

    class userInfoForm {

    }

    class PhotoCard {

    }

    class ProgressiveHorizontalScrollView {

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

## Attributions
<a href='https://pngtree.com/so/Eat'>Eat png from pngtree.com/</a>
<a href='https://pngtree.com/so/cute'>cute png from pngtree.com/</a>