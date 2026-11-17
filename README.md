
# 🚀 SpaceMystery

**SpaceMystery** è un gioco di ruolo investigativo ambientato in una stazione spaziale.

Il protagonista è **Alex**, un astronauta che deve esplorare i diversi ambienti della stazione, raccogliere indizi, parlare con l'equipaggio e pubblicare rapporti investigativi per aumentare la propria reputazione e risolvere il mistero **prima che il tempo scada**.

**Package:** `it.unicam.cs.mpgc.rpg126604`  
**Matricola:** 126604

---

## 🚀 Come eseguire il progetto

### Prerequisiti

- Java 24
- Gradle (tramite il Gradle Wrapper incluso nel progetto)

### Istruzioni

Clonare il repository da GitHub e aprire la cartella del progetto:

```bash
git clone https://github.com/rosmery21/SpaceMystery.git
cd SpaceMystery
```

### Build del progetto

Su Windows:

```powershell
.\gradlew.bat build
```

Su Linux e macOS:

```bash
./gradlew build
```

### Esecuzione

Su Windows:

```powershell
.\gradlew.bat run
```

Su Linux e macOS:

```bash
./gradlew run
```

---

## 🎮 Come si gioca

- **WASD o frecce direzionali** — Muovi Alex sulla mappa della stazione.
- **MAPPA DELLA STAZIONE** — Visualizza gli ambienti ed esplora la stazione spaziale.
- **RACCOGLI INDIZI** — Il pulsante è disponibile quando nella stanza corrente ci sono indizi ancora da raccogliere.
- **PARLA CON L'EQUIPAGGIO** — Interagisci con i personaggi della stazione e scegli le risposte disponibili. Alcune richiedono un determinato livello di carisma e permettono di ottenere indizi esclusivi, non trovabili durante l'esplorazione.
- **TACCUINO INVESTIGATIVO** — Consulta tutti gli indizi scoperti durante la missione.
- **MISSIONI** — Segui gli obiettivi e controlla i progressi delle missioni.
- **OBIETTIVO ATTUALE** — Consulta le indicazioni che ti guidano nel corso dell'indagine.
- **CREA RAPPORTO** — Pubblica un rapporto investigativo utilizzando gli indizi raccolti per aumentare la reputazione.
- **STATISTICHE RPG** — Migliora le capacità di Alex e accumula esperienza per avanzare di livello.
- **SOSPETTO** — Tieni sotto controllo il livello di sospetto, che aumenta con alcune azioni.
- **TEMPO LIMITATO** — Hai **10 giorni** per completare la missione.
- **REGISTRO MISSIONE** — Leggi gli aggiornamenti sulle azioni svolte e gli avvisi importanti.
- **CONTINUA PARTITA** — Se è presente un salvataggio, puoi riprendere la missione senza perdere i progressi.
- **Obiettivo finale** — Raggiungi **100 punti di reputazione** entro il limite di tempo, evitando che il sospetto raggiunga il livello massimo.

---

## ✨ Funzionalità presenti

- Mappa esplorabile con movimento tramite tastiera.
- Gestione del movimento attraverso il Command Pattern.
- Esplorazione delle stanze della stazione spaziale.
- Raccolta degli indizi e taccuino investigativo.
- Sistema di dialoghi con NPC e scelte vincolate al carisma.
- Indizi esclusivi ottenibili attraverso dialoghi specifici, distinti da quelli esplorabili.
- Sistema di missioni con obiettivi tracciati automaticamente.
- Creazione e pubblicazione di rapporti investigativi.
- Sistema di reputazione, sospetto e tempo limitato.
- Progressione RPG con livelli, esperienza e statistiche.
- Salvataggio e caricamento della partita tramite file JSON.
- Interfaccia grafica JavaFX realizzata programmaticamente, senza FXML.
- Registro della missione con aggiornamenti e avvisi.

---

## 💡 Idee per sviluppi futuri

- Consentire al giocatore di scegliere tra missioni differenti.
- Introdurre una specializzazione delle statistiche, invece di aumentarle tutte insieme a ogni livello.
- Aggiungere nuovi ambienti e personaggi alla stazione spaziale.
- Inserire eventi casuali durante l'esplorazione.
- Ampliare i dialoghi con nuove scelte e conseguenze narrative.
- Introdurre finali alternativi in base agli indizi raccolti e alle decisioni di Alex.

---

## 🛠️ Note tecniche

SpaceMystery segue il pattern architetturale **MVC (Model-View-Controller)**, separando la logica di gioco, la gestione dei dati e l'interfaccia grafica.

La persistenza utilizza **Gson** per il salvataggio e il caricamento dei dati in formato JSON.

L'accesso ai dati è organizzato attraverso l'interfaccia `GameRepository`, che permette di separare il controller dall'implementazione concreta del repository, applicando il principio di **Dependency Inversion (DIP)**.

Il progetto utilizza **Gradle** per la gestione delle dipendenze e delle operazioni di build ed esecuzione.

Il movimento del personaggio sulla mappa utilizza il **Command Pattern**, che separa i comandi di movimento dalla gestione degli input da tastiera.

Per maggiori dettagli sull'architettura, sulle classi, sulle interfacce e sui principi SOLID applicati, consulta la documentazione del progetto.

---

## 📦 Tecnologie utilizzate

- Java 24
- JavaFX 24.0.1
- Gson 2.13.2
- Gradle
- Git e GitHub

---

## 🤖 Uso di strumenti di AI

Durante lo sviluppo di **SpaceMystery** è stato utilizzato **ChatGPT (OpenAI)** come assistente IA per:

- Supporto nella progettazione dell'architettura MVC
- Spiegazione e applicazione dei principi SOLID
- Supporto nella configurazione di Gradle e JavaFX
- Assimilazione di concetti teorici
- Automatizzazione di codice ripetitivo
- Suggerimenti sulla struttura del codice

Per una descrizione dettagliata consultare la **[Wiki ufficiale del repository](https://github.com/rosmery21/SpaceMystery/wiki)**.
