# Project Mealer - Group 8

## Login Architecture

### Api (Firebase)

User experience:
`FirebaseUI seems promising for Single Sign On using oAuth`

```mermaid
stateDiagram-v2
    direction LR

    sign_in : Single Sign On
    home : Home Screen

    [*] --> sign_in

    sign_in --> home

    home --> [*]
```

API Architecture:

```mermaid
classDiagram
    class MealerApi {
        <<interface>>
        List getRecipes()
        bool isSignedIn()
        bool signIn()
    }

    class FirebaseClient {
        <<Investigate FirebaseUI for single sign on>>
    }

    MealerApi <-- FirebaseClient


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
