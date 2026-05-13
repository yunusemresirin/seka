Die Laufzeitumgebung befindet sich im Paket `org.hbrs.seka.uebung2_3`.

Sie besteht aus folgenden zentralen Bereichen:

| Bereich | Paket | Aufgabe |
|---|---|---|
| CLI | `org.hbrs.seka.uebung2_3.cli` | Einstiegspunkt für Benutzerinteraktion |
| Runtime | `org.hbrs.seka.uebung2_3.core.runtime` | Verwaltung des Laufzeitzustands und Persistenz |
| Komponentenverwaltung | `org.hbrs.seka.uebung2_3.core.component` | Verwaltung und Ausführung von Komponenten |
| Komponentenmodell | `org.hbrs.seka.uebung2_3.core.component.records` | Datenstrukturen für Komponenten und Status |
| Annotationen | `org.hbrs.seka.uebung2_3.core.component.annotations` | Markierung von Start-/Stop-Operationen |

Die zentrale Koordination übernimmt `RuntimeManager`.  
Die Verwaltung von Komponenten erfolgt über `ComponentManager`.  
Die Ausführung einzelner Komponenten wird über `ComponentRunner` modelliert.  
Der aktuelle Zustand wird über `RuntimeState` beschrieben und kann über `RuntimeSnapshot` persistiert werden.  
Die technische Persistenz ist über `RuntimePersistenceClient` angebunden.