package org.hbrs.seka.uebung2_3.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeSnapshot {

    private RuntimeState state;
    private Map<String, String> deployedComponents = new LinkedHashMap<>();
    private Map<String, String> runningInstances = new LinkedHashMap<>();

    public RuntimeSnapshot() {
    }

    public RuntimeSnapshot(RuntimeState state,
                           Map<String, String> deployedComponents,
                           Map<String, String> runningInstances) {
        this.state = state;
        this.deployedComponents = new LinkedHashMap<>(deployedComponents);
        this.runningInstances = new LinkedHashMap<>(runningInstances);
    }

    public RuntimeState getState() {
        return state;
    }

    public void setState(RuntimeState state) {
        this.state = state;
    }

    public Map<String, String> getDeployedComponents() {
        return deployedComponents;
    }

    public void setDeployedComponents(Map<String, String> deployedComponents) {
        this.deployedComponents = new LinkedHashMap<>(deployedComponents);
    }

    public Map<String, String> getRunningInstances() {
        return runningInstances;
    }

    public void setRunningInstances(Map<String, String> runningInstances) {
        this.runningInstances = new LinkedHashMap<>(runningInstances);
    }
}