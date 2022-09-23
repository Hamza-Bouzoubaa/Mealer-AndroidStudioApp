# Project Mealer - Group 8

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

API Architecture:

```mermaid
classDiagram
    class MealerApiClient {
        <<interface>>
        User user

        public User getUser()
        public bool signIn()
    }

    class FirebaseClient {
        <<signleton>>
        public FirebaseClient instance
    }

    MealerApiClient <-- FirebaseClient

    class MealerUser {
        String firstName
        String lastName
        String email
        String passwordHash
        Byte[] profilePicture
        Byte[] voidCheck
        String address
        String biography
        String creditCard
        MealerRole[] roles
    }

    class MealerRole {
        <<Enumeration>>
        CONSUMER,
        PRODUCER,
        ADMIN
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

Nous utilisons [Firebase](https://firebase.google.com/docs/reference/android/packages) comme base de données. Envoyer un message à quelqu'un de l'equipe pour avoir access.
