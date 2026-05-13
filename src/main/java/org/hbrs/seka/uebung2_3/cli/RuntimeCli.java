package org.hbrs.seka.uebung2_3.cli;

import org.hbrs.seka.uebung2_3.core.component.records.ComponentDeploymentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentStatus;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimePersistenceClient;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimeState;

import java.nio.file.Path;
import java.util.Scanner;

public class RuntimeCli {

    private static final RuntimePersistenceClient PERSISTENCE_CLIENT =
            new RuntimePersistenceClient(Path.of("runtime-snapshot.properties"));

    private static final RuntimeManager RUNTIME = new RuntimeManager(PERSISTENCE_CLIENT.loadSnapshot());

    /** Beispiel ↓

      start
      deploy product C:\components\product.jar
      start-component product-1 product
      start-component product-2 product
      dispatch product
      dispatch product
      dispatch product
      status
      stop-component product-1
      stop-component product-2
      delete product
      stop
      exit

     * -- oder --

      start
      deploy demo C:\Users\Yunus\Projects\components\demo-component-1.0-SNAPSHOT.jar
      start-component demo-1 demo
      start-component demo-2 demo
      status
      dispatch demo
      dispatch demo
      stop-component demo-1
      stop-component demo-2
      delete demo
      stop
      exit

     */
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                String[] parts = line.split("\\s+");

                try {
                    switch (parts[0]) {
                        /* FA1 */  case "start" -> RUNTIME.start();
                        /* FA2 */  case "stop" -> RUNTIME.stop();
                        /* FA3 */  case "deploy" -> RUNTIME.deploy(parts[1], Path.of(parts[2]));
                        /* FA4 - FA6 */ case "start-component" -> RUNTIME.startComponent(parts[1], parts[2]);
                        /* FA7 */  case "status" -> printStatus();
                        /* FA8 */  case "stop-component" -> RUNTIME.stopComponent(parts[1]);
                        /* FA9 */  case "delete" -> RUNTIME.delete(parts[1]);
                        /* TA01 */ case "dispatch" -> RUNTIME.dispatch(parts[1]);
                        case "help" -> System.out.println("Commands: start, stop, status, deploy <componentId> <jar>, delete <componentId>, start-component <instanceId> <componentId>, stop-component <instanceId>, exit");
                        default -> System.out.println("Unbekannter Befehl");
                    }
                } catch (Exception e) {
                    System.out.println("Fehler: " + e.getMessage());
                }
            }
        }
    }

    private static void printStatus() {
        System.out.println("Runtime: " + RUNTIME.state());

        if (RUNTIME.state().equals(RuntimeState.RUNNING)) {
            System.out.println();
            System.out.println("Aktive Instanzen:");
            for (ComponentStatus status : RUNTIME.instanceStatuses()) {
                System.out.println(
                        status.instanceId() + " | " +
                                status.name() + " | " +
                                status.state()
                );
            }

            System.out.println();
            System.out.println("Deployte Komponenten:");
            for (ComponentDeploymentStatus status : RUNTIME.deploymentStatuses()) {
                System.out.println(
                        status.componentId() + " | " +
                        status.jarPath() + " | " +
                        status.state()
                );
            }
        }
    }

}