package org.hbrs.seka.uebung2_3.core.component;

import org.hbrs.seka.uebung2_3.core.component.records.Component;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentDeploymentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.RunningComponent;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimeState;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentManager {

    private final ComponentRunner runner = new ComponentRunner();
    private final Map<String, RunningComponent> runningComponents = new ConcurrentHashMap<>();
    private final Map<String, Path> deployedComponents = new ConcurrentHashMap<>();
    private final Map<String, String> instanceToComponentId = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> dispatchCounters = new ConcurrentHashMap<>();

    public void deploy(String componentId, Path jarPath) {
        runner.validateComponent(new Component(jarPath));
        deployedComponents.put(componentId, jarPath);
    }

    public void delete(String componentId) {
        if (instanceToComponentId.containsValue(componentId)) {
            throw new IllegalStateException("Komponente läuft noch: " + componentId);
        }

        deployedComponents.remove(componentId);
        dispatchCounters.remove(componentId);
    }

    public void start(String instanceId, String componentId) {
        if (runningComponents.containsKey(instanceId)) {
            throw new IllegalStateException("Instanz läuft bereits: " + instanceId);
        }

        Path jarPath = deployedComponents.get(componentId);
        if (jarPath == null) {
            throw new IllegalStateException("Komponente nicht deployed: " + componentId);
        }

        RunningComponent runningComponent = runner.startComponent(new Component(jarPath));
        runningComponents.put(instanceId, runningComponent);
        instanceToComponentId.put(instanceId, componentId);
    }

    public void stop(String instanceId) {
        RunningComponent runningComponent = runningComponents.remove(instanceId);
        if (runningComponent == null) {
            throw new IllegalStateException("Keine laufende Instanz: " + instanceId);
        }

        instanceToComponentId.remove(instanceId);
        runner.stopComponent(runningComponent);
    }

    public void stopAll() {
        for (String instanceId : runningComponents.keySet()) {
            stop(instanceId);
        }
    }

    // TA01
    public String dispatch(String componentId) {
        List<String> instances = instanceToComponentId.entrySet().stream()
                .filter(entry -> entry.getValue().equals(componentId))
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        if (instances.isEmpty()) {
            throw new IllegalStateException("Keine laufende Instanz für Komponente: " + componentId);
        }

        AtomicInteger counter = dispatchCounters.computeIfAbsent(componentId, key -> new AtomicInteger(0));
        int index = Math.abs(counter.getAndIncrement() % instances.size());

        return instances.get(index);
    }

    public List<ComponentStatus> statuses() {
        return runningComponents.keySet().stream()
                .map(instanceId -> new ComponentStatus(
                        instanceId,
                        instanceToComponentId.get(instanceId),
                        RuntimeState.ACTIVE
                ))
                .toList();
    }

    public List<ComponentDeploymentStatus> deploymentStatuses() {
        return deployedComponents.entrySet().stream()
                .map(entry -> {
                    String componentId = entry.getKey();
                    boolean running = instanceToComponentId.containsValue(componentId);
                    return new ComponentDeploymentStatus(
                            componentId,
                            entry.getValue().toString(),
                            running ? RuntimeState.ACTIVE : RuntimeState.STOPPED
                    );
                })
                .toList();
    }

    public Map<String, String> snapshotDeployedComponents() {
        Map<String, String> snapshot = new LinkedHashMap<>();
        deployedComponents.forEach((componentId, path) -> snapshot.put(componentId, path.toString()));
        return snapshot;
    }

    public Map<String, String> snapshotRunningInstances() {
        return new LinkedHashMap<>(instanceToComponentId);
    }

    public void restoreFromSnapshot(Map<String, String> deployed, Map<String, String> running) {
        deployedComponents.clear();
        runningComponents.clear();
        instanceToComponentId.clear();
        dispatchCounters.clear();

        deployed.forEach((componentId, jarPath) -> deploy(componentId, Path.of(jarPath)));
        running.forEach(this::start);
    }

}