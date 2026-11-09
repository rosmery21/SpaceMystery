# SpaceMystery
## Wiki del Progetto

---

## 📋 Indice

1. [Descrizione del Progetto](#1-descrizione-del-progetto)
2. [Funzionalità Implementate](#2-funzionalità-implementate)
3. [Architettura MVC](#3-architettura-mvc)
4. [Classi e Responsabilità](#4-classi-e-responsabilità)
5. [Interfacce](#5-interfacce)
6. [Persistenza dei Dati](#6-persistenza-dei-dati)
7. [Estensibilità](#7-estensibilità)
8. [Principi SOLID](#8-principi-solid-applicati)
9. [Dichiarazione Uso AI](#9-dichiarazione-uso-ai)

---

## 1. Descrizione del Progetto

**SpaceMystery** è un gioco di ruolo investigativo ambientato in una stazione spaziale.

Il protagonista è **Alex**, un astronauta che deve esplorare i diversi ambienti della stazione, raccogliere indizi e pubblicare rapporti investigativi per aumentare la propria reputazione e scoprire i misteri della missione.

Il progetto è sviluppato in **Java**, con interfaccia grafica **JavaFX**, applicando i principi e gli strumenti studiati durante il corso di Metodologie di Programmazione.

---

## 2. Funzionalità Implementate

- **Mappa interattiva** — esplorazione della stazione spaziale con movimento del personaggio tramite tasti WASD o frecce direzionali.
- **Sistema di esplorazione** — il giocatore visita i diversi ambienti della stazione e raccoglie indizi.
- **Taccuino investigativo** — raccolta e visualizzazione degli indizi scoperti durante la missione.
- **Sistema di rapporti** — creazione e pubblicazione di rapporti investigativi utilizzando gli indizi raccolti.
- **Sistema di reputazione** — la reputazione di Alex aumenta in base agli indizi utilizzati nei rapporti.
- **Persistenza dei dati** — salvataggio e caricamento dello stato di gioco in formato JSON.
- **Schermata introduttiva** — presentazione del gioco con tutorial ed effetto di scrittura animata.

---

## 3. Architettura MVC

Il progetto segue il pattern architetturale **Model-View-Controller (MVC)**, che permette di separare i dati, l'interfaccia grafica e la logica del gioco.

### Model

Contiene le classi che rappresentano gli elementi del gioco, come l'astronauta, gli indizi, i rapporti investigativi e gli ambienti della stazione spaziale.

Non dipende dalla View né dal Controller.

### View

Contiene le classi JavaFX che gestiscono l'interfaccia grafica, la schermata iniziale e la mappa interattiva.

Comunica con il Controller per ottenere e aggiornare le informazioni del gioco.

### Controller

Collega il Model e la View.

Gestisce le azioni del giocatore, come l'esplorazione, la raccolta degli indizi e la pubblicazione dei rapporti investigativi, coordinando anche il salvataggio dei dati.


---

## 4. Classi e Responsabilità

### Package `model`

#### `GameCharacter` (abstract)

Classe astratta che rappresenta i personaggi del gioco.

- Gestisce le informazioni comuni dei personaggi.
- Definisce il comportamento condiviso da `Astronaut` e `NPC`.

#### `Astronaut`

Rappresenta Alex, l'astronauta controllato dal giocatore.

- Gestisce il taccuino degli indizi raccolti.
- Tiene traccia dei rapporti investigativi pubblicati.
- Memorizza gli ambienti visitati.
- Aggiorna la reputazione quando viene pubblicato un rapporto.

#### `NPC`

Rappresenta i personaggi non giocanti presenti nella stazione spaziale.

- Contiene le informazioni e i dialoghi dei personaggi.
- Gestisce gli indizi associati agli NPC.
- Estende `GameCharacter`.

#### `Location`

Rappresenta un ambiente esplorabile della stazione spaziale.

- Contiene gli indizi presenti nell'ambiente.
- Tiene traccia degli ambienti visitati.
- Implementa `Identifiable` e `Describable`.

#### `Clue`

Rappresenta un indizio che il giocatore può raccogliere.

- Contiene la descrizione dell'indizio.
- Tiene traccia della sua scoperta.
- Implementa `Identifiable`, `Describable` e `Discoverable`.

#### `Report`

Rappresenta un rapporto investigativo creato da Alex.

- Contiene gli indizi utilizzati nel rapporto.
- Contribuisce al calcolo della reputazione.
- Implementa `Identifiable` e `Publishable`.

#### `GameState`

Rappresenta lo stato corrente del gioco.

- Contiene le informazioni sul giocatore e sulla posizione corrente.
- Permette il salvataggio e il caricamento dello stato tramite JSON.

### Package `controller`

#### `GameController`

Controller principale del gioco.

- Inizializza il gioco caricando i dati dai file JSON.
- Gestisce lo spostamento tra gli ambienti della stazione.
- Coordina la raccolta degli indizi.
- Gestisce la creazione e la pubblicazione dei rapporti investigativi.
- Coordina il salvataggio e il caricamento dello stato di gioco.

### Package `repository`

#### `JsonGameRepository`

Implementazione concreta dell'interfaccia `GameRepository`.

- Salva lo stato del gioco in formato JSON.
- Carica lo stato di gioco da un file JSON.
- Utilizza la libreria Gson.

#### `GameDataLoader`

Classe responsabile del caricamento dei dati iniziali del gioco.

- Carica gli ambienti della stazione.
- Carica gli indizi disponibili.
- Carica i personaggi non giocanti (NPC).

### Package `view`

#### `GameView`

Interfaccia grafica principale del gioco.

- Visualizza le informazioni dell'ambiente corrente.
- Permette di consultare il taccuino investigativo.
- Gestisce le azioni del giocatore e la pubblicazione dei rapporti.

#### `MapView`

Vista della mappa interattiva della stazione spaziale.

- Visualizza gli ambienti esplorabili.
- Gestisce il movimento del personaggio tramite WASD o frecce direzionali.
- Permette di spostarsi tra gli ambienti della stazione.

#### `WelcomeView`

Schermata introduttiva del gioco.

- Presenta SpaceMystery e la sua ambientazione.
- Mostra un tutorial sulle principali meccaniche.
- Utilizza un effetto di scrittura animata per il testo.

### Classe `GameStats`

Classe dedicata alle statistiche di gioco.

- Utilizza le Stream API di Java.
- Calcola statistiche relative agli indizi raccolti.
- Fornisce informazioni sulla reputazione e sui progressi del giocatore.

---

## 5. Interfacce

Il progetto applica il principio **Interface Segregation Principle (ISP)** attraverso interfacce piccole e specifiche, ognuna con una responsabilità precisa.

| Interfaccia | Responsabilità | Implementata da |
|---|---|---|
| `GameRepository` | Gestisce il salvataggio e il caricamento dello stato di gioco | `JsonGameRepository` |
| `Identifiable` | Fornisce un identificativo univoco | `Clue`, `Location`, `Report` |
| `Describable` | Fornisce una descrizione | `Clue`, `Location` |
| `Discoverable` | Definisce il comportamento degli elementi che possono essere scoperti | `Clue` |
| `Publishable` | Definisce il comportamento degli elementi pubblicabili | `Report` |
| `ReputationCalculator` | Calcola la reputazione attraverso una funzione lambda | Implementata tramite lambda |

---

## 6. Persistenza dei Dati

SpaceMystery utilizza il formato **JSON** e la libreria **Gson** per gestire i dati del gioco.

### Dati di configurazione (statici)

Le informazioni iniziali del gioco sono definite in file JSON presenti nella cartella `src/main/resources/`:

- `locations.json` — contiene gli ambienti della stazione spaziale.
- `clues.json` — contiene gli indizi disponibili.
- `npcs.json` — contiene i personaggi non giocanti.

Questi dati vengono caricati all'avvio attraverso la classe `GameDataLoader`.

### Stato di gioco (dinamico)

Lo stato del gioco viene salvato in formato JSON nel file `savegame.json`.

Il salvataggio permette di conservare le informazioni sul giocatore, gli indizi raccolti e i progressi della missione.

La classe `JsonGameRepository` implementa l'interfaccia `GameRepository` e utilizza **Gson** per gestire il salvataggio e il caricamento dei dati.

---

## 7. Estensibilità

SpaceMystery è progettato per permettere l'aggiunta di nuovi contenuti e funzionalità senza modificare l'intera struttura del gioco.

### Aggiungere nuovi ambienti

È possibile aggiungere nuovi ambienti della stazione spaziale modificando i dati presenti nel file `locations.json`.

### Aggiungere nuovi NPC

È possibile introdurre nuovi personaggi non giocanti aggiungendo le relative informazioni al file `npcs.json`.

### Aggiungere nuovi indizi

Nuovi indizi possono essere definiti nel file `clues.json`, ampliando le possibilità investigative del giocatore.

### Aggiungere un nuovo sistema di persistenza

È possibile creare una nuova classe che implementa l'interfaccia `GameRepository`, ad esempio `DatabaseGameRepository`, per utilizzare un sistema di salvataggio diverso da JSON.

### Aggiungere una nuova interfaccia grafica

Grazie alla separazione MVC, è possibile sviluppare una nuova View mantenendo separate la logica del gioco e la rappresentazione grafica.

### Modificare il calcolo della reputazione

L'interfaccia funzionale `ReputationCalculator` permette di modificare la formula utilizzata per calcolare la reputazione attraverso una funzione lambda.

---

## 8. Principi SOLID Applicati

### S — Single Responsibility Principle

Ogni classe ha una responsabilità specifica:

- `Astronaut` gestisce le informazioni e le attività del protagonista.
- `GameDataLoader` si occupa del caricamento dei dati iniziali.
- `JsonGameRepository` gestisce il salvataggio e il caricamento dello stato di gioco.

### O — Open/Closed Principle

Il progetto permette di aggiungere nuove funzionalità senza modificare necessariamente le classi esistenti.

- È possibile aggiungere nuovi sistemi di persistenza implementando `GameRepository`.
- Le interfacce consentono di estendere il comportamento del gioco.

### L — Liskov Substitution Principle

Le classi derivate possono essere utilizzate al posto delle rispettive classi base, rispettandone il comportamento.

- `Astronaut` e `NPC` estendono `GameCharacter`.
- `JsonGameRepository` può essere utilizzato attraverso l'interfaccia `GameRepository`.

### I — Interface Segregation Principle

Il progetto utilizza interfacce piccole e specifiche.

- `Clue` implementa soltanto le interfacce necessarie al suo comportamento.
- Le interfacce `Identifiable`, `Describable`, `Discoverable` e `Publishable` definiscono responsabilità distinte.

### D — Dependency Inversion Principle

Le classi principali utilizzano astrazioni per ridurre le dipendenze dalle implementazioni concrete.

- `GameRepository` definisce il contratto per la persistenza.
- `JsonGameRepository` fornisce un'implementazione concreta del contratto.

La struttura permette di utilizzare implementazioni alternative del sistema di salvataggio.

---

## 9. Dichiarazione Uso AI

Durante lo sviluppo di SpaceMystery è stato utilizzato **ChatGPT (OpenAI)** come assistente AI.

### Attività supportate dall'AI

- **Progettazione architetturale** — supporto nell'organizzazione del progetto secondo il pattern MVC.
- **Spiegazione dei concetti** — chiarimenti sui principi SOLID, sulle interfacce, sulle Stream API e sulle espressioni lambda.
- **Supporto al codice** — suggerimenti per la scrittura, la comprensione e l'adattamento di porzioni di codice Java.
- **Debug** — assistenza nell'individuazione e nella risoluzione degli errori.
- **Configurazione** — supporto nell'utilizzo di Gradle, JavaFX e delle dipendenze.
- **Documentazione** — assistenza nella scrittura dei JavaDoc, del README e della Wiki.

### Personalizzazione del progetto

Il progetto originale è stato adattato all'ambientazione fantascientifica di SpaceMystery, con una stazione spaziale, un astronauta protagonista e rapporti investigativi.

Il codice è stato modificato e verificato durante lo sviluppo per adattarlo alle caratteristiche del gioco.

### Comprensione del codice

Gli aspetti principali del progetto da conoscere e saper spiegare sono:

- La struttura MVC e le responsabilità dei diversi componenti.
- L'applicazione dei principi SOLID.
- L'utilizzo delle interfacce e dell'ereditarietà.
- Il funzionamento delle Stream API e delle espressioni lambda.
- Il salvataggio e il caricamento dei dati JSON tramite Gson.
- Il funzionamento della mappa interattiva realizzata con JavaFX.