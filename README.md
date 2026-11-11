
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

Clonare il repository da GitHub e aprire la cartella del progetto.

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
- **PARLA CON L'EQUIPAGGIO** — Interagisci con i personaggi della stazione e scegli le risposte disponibili; alcune richiedono un determinato livello di carisma.
- **TACCUINO INVESTIGATIVO** — Consulta gli indizi scoperti durante la missione.
- **MISSIONI** — Segui gli obiettivi e controlla i progressi delle missioni.
- **OBIETTIVO ATTUALE** — Consulta le indicazioni che ti guidano nel corso dell'indagine.
- **CREA RAPPORTO** — Pubblica un rapporto investigativo utilizzando gli indizi raccolti per aumentare la reputazione.
- **STATISTICHE RPG** — Migliora le capacità di Alex e accumula esperienza per avanzare di livello.
- **SOSPETTO** — Tieni sotto controllo il livello di sospetto, che aumenta con alcune azioni.
- **TEMPO LIMITATO** — Hai **10 giorni** per completare la missione.
- **REGISTRO MISSIONE** — Leggi gli aggiornamenti sulle azioni svolte e gli avvisi importanti.
- **Obiettivo finale** — Raggiungi **100 punti di reputazione** entro il limite di tempo, evitando che il sospetto raggiunga il livello massimo.

---

## 📦 Tecnologie utilizzate

- Java 24
- JavaFX 24.0.1
- Gson 2.13.2
- Gradle
- Git e GitHub

---

## 🤖 Uso di strumenti di AI

Durante lo sviluppo è stato utilizzato **ChatGPT (OpenAI)** come assistente AI per:

- Supporto nella progettazione dell'architettura MVC.
- Suggerimenti sulla struttura delle classi e delle interfacce.
- Spiegazione e applicazione dei principi SOLID e Clean Code.
- Supporto nella configurazione di Gradle e JavaFX.
- Supporto nella scrittura, comprensione e adattamento di porzioni di codice.
- Assistenza nella documentazione JavaDoc e nella Wiki.
- Revisione e miglioramento dello stile del codice per renderlo più leggibile e naturale.

Il codice è stato adattato al progetto SpaceMystery e verificato durante lo sviluppo.

Per una descrizione più dettagliata dell'architettura, delle funzionalità e dell'utilizzo degli strumenti di AI, consulta il file **[wiki.md](wiki.md)**.
