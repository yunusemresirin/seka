# Funktionale Anforderungen aus Sicht des Component Assemblers

Diese Tabelle beschreibt die funktionalen Anforderungen an eine JVM-basierte Software-Komponente aus der Perspektive eines Component Assemblers, inklusive Priorisierung nach MoSCoW, Aufwandsschätzung und notwendiger Entwurfsmuster.

| Anforderung                        | Beschreibung                                                                                               | Priorität (MoSCoW) | Aufwand | Notwendige Pattern / Mechanismen                      |
|:-----------------------------------|:-----------------------------------------------------------------------------------------------------------|:-------------------|:--------|:------------------------------------------------------|
| **Dependency Injection**           | Die Komponente muss Abhängigkeiten von außen empfangen, statt sie selbst zu instanziieren.                 | **Must-have**      | Mittel  | Inversion of Control (IoC), Dependency Injection (DI) |
| **Externalisierte Konfiguration**  | Parameter (Ports, URLs, Timeouts) müssen via `.properties`, `.yaml` oder Umgebungsvariablen änderbar sein. | **Must-have**      | Klein   | Configuration Provider, Factory Pattern               |
| **Lifecycle Management**           | Die Komponente muss definierte Start-/Stop-Hooks bereitstellen (z.B. `@PostConstruct`, `@PreDestroy`).     | **Must-have**      | Mittel  | Template Method, Lifecycle Callback                   |
| **Standardisierte Schnittstellen** | Kommunikation über bekannte Protokolle oder Interfaces (z.B. REST, gRPC oder Java Interfaces).             | **Must-have**      | Groß    | Facade Pattern, Adapter Pattern                       |
| **Health & Readiness Checks**      | Die Laufzeitumgebung muss den Status der Komponente abfragen können (Liveness/Readiness).                  | **Should-have**    | Mittel  | Observer Pattern, Health Check API                    |
| **Logging & Tracing**              | Einheitliche Log-Ausgabe (SLF4J) und Weitergabe von Korrelations-IDs für verteiltes Tracing.               | **Should-have**    | Mittel  | Decorator Pattern (für Tracing Context), Facade       |
| **Metriken (Monitoring)**          | Bereitstellung von Telemetriedaten (z.B. Micrometer, Prometheus) zur Überwachung der Performance.          | **Should-have**    | Mittel  | Strategy Pattern, Proxy Pattern                       |
| **Hot Swapping / Reloading**       | Austausch oder Neukonfiguration der Komponente ohne vollständigen Systemneustart.                          | **Could-have**     | Groß    | Dynamic Proxy, OSGi Services oder Spring Cloud Bus    |

## Legende

### Priorität (MoSCoW)
* **Must-have:** Zwingend erforderlich für den Betrieb.
* **Should-have:** Wichtig, aber nicht kritisch für den ersten Release.
* **Could-have:** Wünschenswert, wenn Ressourcen vorhanden sind.
* **Won't-have:** Wird in diesem Zyklus nicht implementiert.

### Aufwand
* **Klein (k):** Standard-Features, meist durch Frameworks abgedeckt.
* **Mittel (m):** Erfordert manuelle Implementierung oder Anpassung.
* **Groß (g):** Komplexe Architekturintegration notwendig.

## Kriterien nach Crnkovic, die das Komponentenmodell erfüllt

Das entwickelte Komponentenmodell erfüllt mehrere zentrale Kriterien aus dem Rahmenwerk nach Crnkovic:

- **Explizite Schnittstelle / Provided Interface**  
  Die Komponente stellt ein klar definiertes, nach außen sichtbares Interface bereit.

- **Kapselung / Black-Box-Prinzip**  
  Die interne Umsetzung bleibt verborgen; nach außen ist nur die definierte Schnittstelle relevant.

- **Deploybarkeit als Einheit**  
  Die Komponente ist als eigenständiges `.jar`-File paketierbar und kann separat eingesetzt werden.

- **Lifecycle-Management**  
  Start- und Stop-Verhalten sind explizit vorgesehen und über Annotationen modelliert.

- **Isolierbarkeit durch ClassLoader**  
  Komponenten werden über einen eigenen ClassLoader geladen und damit voneinander isoliert.

- **Mehrfachinstanziierbarkeit**  
  Dieselbe deployte Komponente kann mehrfach parallel gestartet werden.

- **Wiederverwendbarkeit**  
  Die deploybare Komponente kann unabhängig von der Laufzeitumgebung wiederverwendet werden.

- **Unterstützung externer Abhängigkeiten**  
  Benötigte Libraries können über den Classpath der Laufzeitumgebung bereitgestellt werden.