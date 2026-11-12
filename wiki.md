# 🚀 SpaceMystery
## Wiki del Progetto

---

## 📋 Indice

1. [Descrizione del Progetto](#1-descrizione-del-progetto)
2. [Funzionalità Implementate](#2-funzionalità-implementate)
3. [Architettura MVC](#3-architettura-mvc)
4. [Responsabilità Individuate](#4-responsabilità-individuate)
5. [Classi e Responsabilità](#5-classi-e-responsabilità)
6. [Interfacce](#6-interfacce)
7. [Persistenza dei Dati](#7-persistenza-dei-dati)
8. [Estensibilità](#8-estensibilità)
9. [Principi SOLID Applicati](#9-principi-solid-applicati)
10. [Altri Design Pattern Utilizzati](#10-altri-design-pattern-utilizzati)
11. [Verifica e Test](#11-verifica-e-test)
12. [Dichiarazione Uso AI](#12-dichiarazione-uso-ai)

---

## 1. Descrizione del Progetto

**SpaceMystery** è un gioco di ruolo investigativo ambientato in una stazione spaziale.

Il protagonista è **Alex**, un astronauta che deve esplorare gli ambienti della stazione, raccogliere indizi, interrogare i membri dell'equipaggio e pubblicare rapporti investigativi per aumentare la propria reputazione.

La missione consiste nel raggiungere **100 punti di reputazione entro 10 giorni**, evitando che il livello di sospetto raggiunga 100.

Il progetto è sviluppato in **Java 24**, con interfaccia grafica **JavaFX 24.0.1**, applicando i principi studiati durante il corso di Metodologie di Programmazione.

---

## 2. Funzionalità Implementate

- **Mappa interattiva** — esplorazione della stazione spaziale tramite WASD o frecce direzionali.
- **Command Pattern** — associazione dei tasti a comandi di movimento indipendenti.
- **Esplorazione** — visita degli ambienti della stazione e raccolta degli indizi.
- **Indizi esclusivi** — alcuni indizi si ottengono soltanto scegliendo determinate risposte nei dialoghi.
- **Taccuino investigativo** — visualizzazione degli indizi raccolti.
- **Rapporti investigativi** — creazione e pubblicazione di rapporti utilizzando gli indizi scoperti.
- **Reputazione** — progressione verso l'obiettivo finale di 100 punti.
- **Dialoghi a scelta multipla** — conversazioni con la Dott.ssa Vega, il Comandante Orion e l'Ingegnere Nova.
- **Requisiti di carisma** — alcune risposte sono disponibili soltanto quando Alex possiede il carisma necessario.
- **Sistema RPG** — intelligenza, carisma, furtività, esperienza e livelli.
- **Missioni** — obiettivi aggiornati automaticamente durante l'indagine.
- **Sospetto** — indicatore che aumenta con alcune azioni e può causare il fallimento della missione.
- **Tempo limitato** — 10 giorni per completare la missione.
- **Obiettivo attuale** — indicazioni sui progressi ancora necessari.
- **Registro Missione** — messaggi e aggiornamenti sulle azioni del giocatore.
- **Persistenza JSON** — salvataggio e caricamento dello stato di gioco.
- **Schermata introduttiva** — presentazione della storia e tutorial.

### Indizi esclusivi dei dialoghi

SpaceMystery distingue due tipi di indizi:

1. **Indizi esplorabili**, associati a un ambiente tramite `locationId`.
2. **Indizi esclusivi dei dialoghi**, con `locationId` impostato a `null`.

Gli indizi esclusivi vengono assegnati quando Alex seleziona una risposta con il requisito di carisma richiesto.

| NPC | Carisma richiesto | Indizio |
|---|---|---|
| Dott.ssa Vega | 7 | `indizio_007` |
| Comandante Orion | 8 | `indizio_008` |
| Ingegnere Nova | 7 | `indizio_009` |

In questo modo, parlare con l'equipaggio permette di ottenere informazioni che non possono essere trovate semplicemente esplorando le stanze.

---

## 3. Architettura MVC

SpaceMystery utilizza il pattern **Model-View-Controller (MVC)** per separare le responsabilità dell'applicazione.

### Model

Il Model contiene le classi che rappresentano gli elementi del gioco: personaggi, ambienti, indizi, rapporti, statistiche e missioni.

Gestisce lo stato e i comportamenti del dominio senza occuparsi della rappresentazione grafica.

### View

La View contiene le schermate JavaFX.

Mostra le informazioni del gioco e inoltra al Controller le azioni effettuate dal giocatore.

### Controller

Il Controller coordina il Model e la View.

Gestisce esplorazione, raccolta degli indizi, dialoghi, missioni, rapporti, sospetto, giorni disponibili e persistenza.

La logica di gioco è separata dai componenti grafici JavaFX: una diversa interfaccia può richiamare le operazioni pubbliche del controller senza riprodurre le regole del gioco.

La separazione MVC facilita la manutenzione e l'estensione del progetto.

---

## 4. Responsabilità Individuate

Prima di assegnare le responsabilità alle singole classi, sono state individuate le responsabilità principali che il sistema nel suo complesso deve garantire.

- **Gestione del personaggio giocante:** tracciare la progressione di Alex, le statistiche RPG, gli indizi raccolti, gli ambienti visitati e le missioni.
- **Gestione del mondo di gioco:** rappresentare gli ambienti esplorabili della stazione spaziale e le informazioni necessarie alla loro visualizzazione.
- **Gestione dei personaggi non giocanti:** gestire i membri dell'equipaggio, i dialoghi, le scelte con requisiti di carisma e gli indizi esclusivi.
- **Gestione degli indizi:** distinguere gli indizi esplorabili da quelli ottenibili tramite dialogo e registrarne la scoperta.
- **Gestione dei rapporti e della reputazione:** utilizzare gli indizi raccolti per creare rapporti investigativi e aggiornare la reputazione.
- **Gestione delle missioni:** tenere traccia degli obiettivi e verificarne il completamento.
- **Gestione dello stato di rischio:** controllare il sospetto e il tempo disponibile, determinando le condizioni di vittoria e sconfitta.
- **Gestione della persistenza:** salvare e caricare lo stato della partita attraverso un'interfaccia indipendente dall'implementazione concreta.
- **Gestione dei dati di configurazione:** caricare ambienti, indizi, NPC e missioni dai file JSON, separandoli dal codice Java.
- **Gestione dell'interfaccia utente:** visualizzare la mappa, le statistiche, il taccuino, i dialoghi e il registro della missione, raccogliendo le azioni del giocatore.

Queste responsabilità di alto livello sono state distribuite tra le classi descritte nella sezione successiva, seguendo il principio di singola responsabilità (**SRP**).

---

## 5. Classi e Responsabilità

### Package `model`

#### `GameCharacter` (abstract)

Classe astratta che rappresenta le caratteristiche comuni dei personaggi del gioco.

Fornisce una base condivisa per `Astronaut` e `NPC`.

#### `Astronaut`

Rappresenta Alex, il protagonista controllato dal giocatore.

Responsabilità:

- Gestire il taccuino investigativo.
- Conservare i rapporti.
- Tenere traccia degli ambienti visitati.
- Gestire missioni attive e completate.
- Memorizzare statistiche e reputazione.

#### `NPC`

Rappresenta i membri dell'equipaggio.

Contiene le informazioni dei personaggi, i dialoghi e le scelte disponibili.

Alcune scelte possono fornire indizi esclusivi.

#### `Location`

Rappresenta un ambiente esplorabile della stazione spaziale.

Contiene descrizione, identificativo, indizi associati e informazioni sullo stato di visita.

Implementa `Identifiable` e `Describable`.

#### `Clue`

Rappresenta un indizio investigativo.

Responsabilità:

- Conservare identificativo e descrizione.
- Registrare se l'indizio è stato scoperto.
- Associare eventualmente l'indizio a una stanza.

Implementa `Identifiable`, `Describable` e `Discoverable`.

Quando `locationId` è `null`, l'indizio può essere ottenuto tramite un dialogo specifico invece che attraverso l'esplorazione.

#### `Report`

Rappresenta un rapporto investigativo creato da Alex.

Contiene gli indizi utilizzati e contribuisce all'aumento della reputazione.

Implementa `Identifiable` e `Publishable`.

#### `PlayerStats`

Gestisce le statistiche RPG:

- Intelligenza.
- Carisma.
- Furtività.
- Livello.
- Esperienza.

Controlla anche i requisiti di carisma delle scelte di dialogo.

#### `Quest`

Rappresenta una missione con uno o più obiettivi.

Memorizza gli obiettivi completati e verifica il completamento della missione.

#### `DialogueChoice`

Rappresenta una scelta di dialogo.

Contiene:

- Testo della scelta.
- Risposta del personaggio.
- Requisito minimo di carisma.
- Ricompensa in esperienza.
- Eventuale identificativo dell'indizio assegnato.

#### `GameState`

Rappresenta lo stato da salvare.

Contiene i dati dell'astronauta, l'identificativo della posizione corrente, il livello di sospetto, i giorni rimanenti e i personaggi già contattati.

#### `GameStats`

Fornisce informazioni statistiche sullo stato del gioco e sui progressi di Alex.

### Package `controller`

#### `GameController`

È il controller principale di SpaceMystery.

Responsabilità:

- Caricare i dati iniziali dai file JSON.
- Gestire gli spostamenti tra gli ambienti.
- Coordinare la raccolta degli indizi.
- Gestire i dialoghi e i requisiti di carisma.
- Assegnare gli indizi esclusivi dei dialoghi.
- Aggiornare gli obiettivi delle missioni.
- Gestire i rapporti investigativi.
- Aggiornare sospetto e giorni rimanenti.
- Salvare e caricare la partita.
- Fornire l'obiettivo corrente tramite `getCurrentObjective()`.

Il controller mantiene una mappa degli indizi:

```java
private final Map<String, Clue> clues;
```

Questa struttura permette di cercare un indizio tramite identificativo anche quando non appartiene a una stanza.

Gli NPC vengono inoltre indicizzati per ambiente durante il caricamento iniziale tramite una struttura `Map<String, List<NPC>>`. Il metodo `getNpcsInCurrentLocation()` restituisce i personaggi presenti nella stanza corrente, evitando di cercarli ogni volta tra tutti gli NPC o di rileggere `npcs.json`.

Il metodo:

```java
private Clue findClueById(String clueId) {
    return clues.get(clueId);
}
```

consente quindi di recuperare sia gli indizi esplorabili sia quelli esclusivi dei dialoghi.

### Package `repository`

#### `JsonGameRepository`

Implementa `GameRepository` utilizzando Gson.

Gestisce la scrittura e la lettura dello stato di gioco in formato JSON.

#### `GameDataLoader`

Carica i dati iniziali da:

- `locations.json`
- `clues.json`
- `npcs.json`
- `quests.json`

Utilizza Gson per la deserializzazione dei dati.

### Package `view`

#### `GameView`

Rappresenta la schermata principale del gioco.

Mostra:

- Ambiente corrente.
- Taccuino investigativo.
- Missioni.
- Statistiche RPG.
- Reputazione.
- Sospetto.
- Giorni rimanenti.
- Obiettivo attuale.
- Registro Missione.

Il pulsante **RACCOGLI INDIZI** compare soltanto quando nell'ambiente corrente sono presenti indizi ancora da scoprire.

Il pulsante **CREA RAPPORTO** viene disabilitato automaticamente quando il taccuino è vuoto, utilizzando un binding JavaFX.

L'interfaccia utilizza il foglio di stile condiviso `style.css`, applicato anche alle finestre di dialogo.

#### `MapView`

Gestisce la rappresentazione della stazione spaziale e il movimento di Alex.

Utilizza JavaFX Canvas e `AnimationTimer`.

Il movimento tramite WASD e frecce direzionali è organizzato attraverso il Command Pattern.

#### `WelcomeView`

Rappresenta la schermata introduttiva.

Presenta l'ambientazione di SpaceMystery e un tutorial sulle principali meccaniche.

Utilizza un effetto di scrittura animata e una rappresentazione grafica spaziale.

---

## 6. Interfacce

SpaceMystery utilizza interfacce per definire contratti e separare le responsabilità.

| Interfaccia | Responsabilità | Utilizzo |
|---|---|---|
| `GameRepository` | Salvataggio e caricamento dello stato | `JsonGameRepository` |
| `Identifiable` | Identificativo univoco | `Clue`, `Location`, `Report` |
| `Describable` | Descrizione di un elemento | `Clue`, `Location` |
| `Discoverable` | Gestione dello stato di scoperta | `Clue` |
| `Publishable` | Pubblicazione di un elemento | `Report` |
| `ReputationCalculator` | Calcolo della reputazione | Funzione lambda |

Le interfacce permettono di mantenere separati i contratti dalle implementazioni concrete.

---

## 7. Persistenza dei Dati

SpaceMystery utilizza **JSON** e la libreria **Gson**.

### Dati di configurazione

I dati iniziali si trovano in `src/main/resources/`.

| File | Contenuto |
|---|---|
| `locations.json` | Ambienti della stazione |
| `clues.json` | 9 indizi: 6 esplorabili e 3 esclusivi dei dialoghi |
| `npcs.json` | 3 NPC con dialoghi e scelte |
| `quests.json` | Missioni e relativi obiettivi |

Il caricamento è gestito da `GameDataLoader`.

### Stato di gioco

Lo stato dinamico viene salvato nel file `savegame.json` attraverso `JsonGameRepository`.

Il controller crea un oggetto `GameState` che contiene:

- L'oggetto `Astronaut`.
- L'identificativo dell'ambiente corrente.
- Il livello di sospetto.
- I giorni rimanenti.
- L'insieme degli NPC già contattati.

Il metodo `saveGame()` passa questo stato al repository.

Il metodo `loadGame()` ripristina le informazioni salvate e riallinea gli indizi scoperti, gli ambienti visitati e gli obiettivi delle missioni.

Il salvataggio viene richiamato durante diverse azioni di gioco, tra cui spostamenti, raccolta degli indizi, dialoghi e pubblicazione dei rapporti.

### Indizi esclusivi e persistenza

Gli indizi ottenibili tramite dialogo hanno `locationId` uguale a `null`.

Durante il caricamento iniziale vengono registrati nella mappa `clues`, senza essere aggiunti a una stanza.

Quando il giocatore seleziona un dialogo che assegna un indizio, `GameController` lo recupera tramite identificativo e lo aggiunge al taccuino.

Il salvataggio conserva l'indizio raccolto insieme agli altri progressi del giocatore.

---

## 8. Estensibilità

Il progetto è organizzato per facilitare l'aggiunta di nuovi contenuti e per rendere possibile, in futuro, l'adattamento a dispositivi diversi.

### Estendere SpaceMystery su più dispositivi

L'architettura **Model-View-Controller (MVC)** separa la logica del gioco dall'interfaccia grafica. Nell'implementazione attuale, le schermate sono realizzate con **JavaFX**; il Model e il Controller gestiscono invece dati, regole e azioni di gioco senza dipendere direttamente dai controlli grafici JavaFX.

Questa organizzazione rende possibile progettare altre interfacce mantenendo il comportamento di base del gioco. Non significa, però, che SpaceMystery sia già disponibile su Android o sul web: queste sarebbero **estensioni future**.

#### Componenti riutilizzabili

In una futura versione per un'altra piattaforma potrebbero essere riutilizzati:

- **Model:** le classi di dominio, tra cui `Astronaut`, `NPC`, `Location`, `Clue`, `Report`, `Quest`, `PlayerStats` e `GameState`.
- **Controller:** `GameController`, che coordina spostamenti, raccolta degli indizi, dialoghi, missioni e rapporti investigativi.
- **Dati di configurazione:** `locations.json`, `clues.json`, `npcs.json` e `quests.json`.
- **Astrazione della persistenza:** `GameRepository`, che permette di sostituire il meccanismo concreto di salvataggio. L'implementazione JSON potrebbe richiedere adattamenti per l'accesso ai file sulla nuova piattaforma.

Le classi di `view`, come `GameView`, `MapView` e `WelcomeView`, sono invece legate alla presentazione JavaFX e dovrebbero essere sostituite o riprogettate.

#### Versione mobile

Una possibile versione Android richiederebbe nuove schermate costruite con i componenti grafici della piattaforma e adatte all'interazione touchscreen. La nuova interfaccia potrebbe utilizzare le operazioni del `GameController` per gestire l'esplorazione, i dialoghi e gli indizi, anziché duplicare le regole del gioco.

Per esempio, `getNpcsInCurrentLocation()` permetterebbe di mostrare i membri dell'equipaggio presenti nell'ambiente corrente. Sarebbero comunque necessari adattamenti per la navigazione, l'avvio dell'applicazione e la gestione dei salvataggi sul dispositivo.

#### Versione web

Una versione web potrebbe utilizzare un'interfaccia HTML, CSS e JavaScript, con un backend Java che esegue la logica di gioco. In questa soluzione, il browser invierebbe le azioni del giocatore al server, che le elaborerebbe attraverso il controller e restituirebbe lo stato aggiornato.

Sarebbe necessario introdurre un livello di comunicazione tra client e server, oltre a gestire le sessioni dei giocatori e la persistenza dei rispettivi salvataggi. Il controller non potrebbe essere richiamato direttamente dal browser senza questa infrastruttura.

#### Vantaggi della separazione MVC

- **Riutilizzabilità:** le regole del gioco non devono essere riscritte per ogni interfaccia.
- **Manutenibilità:** gli interventi sulla grafica restano distinti da quelli sulla logica.
- **Estendibilità:** nuove piattaforme possono utilizzare gli stessi concetti e servizi del dominio.
- **Testabilità:** la logica può essere verificata senza avviare necessariamente le schermate JavaFX.

Il punto di contatto tra una futura interfaccia e il gioco rimarrebbero le operazioni pubbliche offerte dal `GameController`, eventualmente esposte tramite un livello di adattamento specifico per la piattaforma.

### Aggiungere nuovi ambienti

È possibile definire nuovi ambienti in `locations.json`.

Il controller li carica automaticamente tramite `GameDataLoader`.

L'eventuale rappresentazione sulla mappa dipende dai dati e dalla logica prevista da `MapView`.

### Aggiungere nuovi NPC e dialoghi

È possibile aggiungere personaggi e scelte in `npcs.json`.

Ogni scelta può definire un requisito di carisma e una ricompensa.

### Aggiungere indizi esclusivi

Per creare un indizio ottenibile soltanto attraverso un dialogo:

1. Aggiungere l'indizio in `clues.json`.
2. Impostare `locationId` a `null`.
3. Inserire il suo identificativo nel campo `clueId` della scelta in `npcs.json`.

Il controller potrà recuperarlo dalla mappa generale degli indizi.

### Aggiungere nuove missioni

È possibile definire missioni in `quests.json`.

Il sistema carica le missioni dal file; per gli obiettivi che richiedono nuovi eventi di gioco può essere necessario estendere la logica di aggiornamento del controller.

### Aggiungere nuovi comandi di movimento

Il Command Pattern permette di associare un nuovo tasto a un comando di movimento senza duplicare la logica di aggiornamento del personaggio.

### Modificare il sistema dei giorni

Il numero massimo di giorni è definito in `GameController`:

```java
private static final int MAX_DAYS = 10;
```

La costante può essere modificata per cambiare il limite temporale della missione.

### Aggiungere una nuova persistenza

Una nuova classe può implementare `GameRepository`, ad esempio `DatabaseGameRepository`.

Il controller utilizza l'interfaccia, senza dipendere direttamente da `JsonGameRepository`.

### Modificare il calcolo della reputazione

L'interfaccia funzionale `ReputationCalculator` consente di rappresentare un algoritmo di calcolo tramite una lambda.

---

## 9. Principi SOLID Applicati

### S — Single Responsibility Principle

Le responsabilità sono distribuite tra classi differenti:

- `Astronaut` gestisce i dati e le attività del protagonista.
- `PlayerStats` gestisce le statistiche RPG.
- `Quest` gestisce gli obiettivi di una missione.
- `GameDataLoader` carica i dati iniziali.
- `JsonGameRepository` gestisce la persistenza.
- `GameView` si occupa della rappresentazione grafica.

### O — Open/Closed Principle

Il progetto può essere esteso attraverso interfacce e dati di configurazione.

Esempi:

- Nuovi NPC e dialoghi possono essere definiti nei file JSON.
- Nuovi indizi possono essere aggiunti senza cambiare la struttura delle classi.
- Nuove implementazioni di persistenza possono utilizzare `GameRepository`.
- Nuovi comandi di movimento possono essere registrati tramite il Command Pattern.

### L — Liskov Substitution Principle

Le sottoclassi possono essere utilizzate attraverso il tipo della classe base.

`Astronaut` e `NPC` estendono `GameCharacter`.

Le implementazioni di `GameRepository` possono essere utilizzate tramite il contratto comune.

### I — Interface Segregation Principle

Il progetto utilizza interfacce specifiche per responsabilità distinte:

- `Identifiable`
- `Describable`
- `Discoverable`
- `Publishable`
- `GameRepository`
- `ReputationCalculator`

Le classi implementano soltanto i contratti necessari.

### D — Dependency Inversion Principle

`GameController` riceve un `GameRepository` tramite il costruttore.

```java
public GameController(GameRepository repository) {
    // inizializzazione del controller
}
```

Questo permette di cambiare l'implementazione della persistenza senza modificare il contratto utilizzato dal controller.

---

## 10. Altri Design Pattern Utilizzati

### Command Pattern

Il Command Pattern è utilizzato in `MapView` per organizzare il movimento di Alex.

Ogni tasto supportato viene associato a un comando che rappresenta un'azione di movimento.

I comandi sono registrati in una mappa e possono essere richiamati dalla gestione degli input.

Questa soluzione separa l'interpretazione del tasto dall'azione eseguita e riduce la necessità di condizioni ripetute.

### MVC

Il pattern MVC separa:

- Model: dati e comportamenti del dominio.
- View: rappresentazione grafica JavaFX.
- Controller: coordinamento delle azioni del giocatore.

### Repository e Dependency Injection

La persistenza è accessibile attraverso `GameRepository`.

L'implementazione concreta viene fornita al controller tramite il costruttore.

Questa scelta riduce l'accoppiamento tra logica di gioco e salvataggio dei dati.

### Espressioni lambda

Il progetto utilizza le espressioni lambda, ad esempio nella gestione degli eventi JavaFX e nell'interfaccia funzionale `ReputationCalculator`.

Non viene utilizzato un `FontRegistry` Singleton: l'aspetto grafico delle schermate è gestito attraverso il CSS e gli stili JavaFX.

---

## 11. Verifica e Test

Durante lo sviluppo è possibile verificare il progetto utilizzando il Gradle Wrapper.

### Compilazione e verifica

Su Windows:

```powershell
.\gradlew.bat build
```

Su Linux e macOS:

```bash
./gradlew build
```

### Avvio del gioco

Su Windows:

```powershell
.\gradlew.bat run
```

Su Linux e macOS:

```bash
./gradlew run
```

### Verifiche funzionali

Le principali funzionalità da controllare sono:

- Movimento di Alex tramite WASD e frecce.
- Spostamento tra gli ambienti della stazione.
- Raccolta degli indizi disponibili.
- Visualizzazione degli indizi nel taccuino.
- Disponibilità delle scelte di dialogo in base al carisma.
- Assegnazione degli indizi esclusivi dopo il dialogo corretto.
- Aggiornamento delle missioni.
- Pubblicazione dei rapporti.
- Aggiornamento di reputazione, sospetto e giorni.
- Salvataggio e caricamento dei progressi.

Questa sezione descrive le verifiche eseguibili sul progetto; non presuppone la presenza di una suite di test JUnit.

---

## 12. Dichiarazione Uso AI

Durante lo sviluppo di SpaceMystery è stato utilizzato **ChatGPT (OpenAI)** come assistente AI.

### Attività supportate dall'AI

**Assimilazione di concetti teorici:** ChatGPT è stato utilizzato per chiarire argomenti trattati a lezione, come i principi SOLID, il pattern MVC, le Stream API e le interfacce funzionali, prima di applicarli al codice.

**Automatizzazione del codice ripetitivo:** supporto nella scrittura di porzioni di codice simili tra loro, come getter, setter, costruttori e registrazione dei comandi di movimento in `MapView`.

**Suggerimenti sulla struttura del codice:** indicazioni sull'organizzazione dei package e delle classi, in particolare sulla separazione tra `model`, `view`, `controller` e `repository`, adattate alle esigenze di SpaceMystery.

**Altre attività di supporto:** assistenza nella configurazione di Gradle e JavaFX, individuazione di errori di compilazione, revisione della leggibilità del codice, documentazione JavaDoc/README/Wiki e confronto con il progetto di riferimento per valutare le modifiche pertinenti.

### Personalizzazione del progetto

Il progetto di riferimento è stato adattato all'ambientazione fantascientifica di SpaceMystery.

La storia è ambientata in una stazione spaziale e il protagonista è Alex, un astronauta impegnato in un'indagine.

Sono stati adattati i personaggi, gli ambienti, gli indizi, i dialoghi e i rapporti investigativi.

### Comprensione del codice

Gli aspetti principali da conoscere e saper spiegare sono:

- Architettura MVC e responsabilità dei componenti.
- Principi SOLID e interfacce.
- Ereditarietà e polimorfismo.
- Command Pattern per il movimento.
- Sistema RPG e requisiti di carisma.
- Gestione degli indizi esplorabili ed esclusivi.
- Missioni e obiettivi.
- Reputazione, sospetto e giorni disponibili.
- Stream API ed espressioni lambda.
- Persistenza JSON con Gson.
- Funzionamento della mappa JavaFX.
- Utilizzo degli strumenti di AI durante lo sviluppo.
- Separazione tra logica di gioco e interfaccia grafica per una futura estensione mobile o web.
