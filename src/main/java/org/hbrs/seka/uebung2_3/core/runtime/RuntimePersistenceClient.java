package org.hbrs.seka.uebung2_3.core.runtime;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class RuntimePersistenceClient {

    private final Path snapshotFile;

    public RuntimePersistenceClient(Path snapshotFile) {
        this.snapshotFile = snapshotFile;
    }

    public void saveSnapshot(RuntimeSnapshot snapshot) {
        try {
            if (snapshotFile.getParent() != null) {
                Files.createDirectories(snapshotFile.getParent());
            }

            Files.writeString(snapshotFile, serialize(snapshot), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Snapshot konnte nicht gespeichert werden.", e);
        }
    }

    public RuntimeSnapshot loadSnapshot() {
        try {
            if (!Files.exists(snapshotFile)) {
                return new RuntimeSnapshot();
            }

            String content = Files.readString(snapshotFile, StandardCharsets.UTF_8);
            if (content.isBlank()) {
                return new RuntimeSnapshot();
            }

            return deserialize(content);
        } catch (IOException e) {
            throw new IllegalStateException("Snapshot konnte nicht geladen werden.", e);
        }
    }

    private String serialize(RuntimeSnapshot snapshot) throws IOException {
        Properties props = new Properties();

        if (snapshot.getState() != null) {
            props.setProperty("state", snapshot.getState().name());
        }

        snapshot.getDeployedComponents().forEach((componentId, jarPath) ->
                props.setProperty("deployed." + componentId, jarPath));

        snapshot.getRunningInstances().forEach((instanceId, componentId) ->
                props.setProperty("running." + instanceId, componentId));

        try (StringWriter writer = new StringWriter()) {
            props.store(writer, null);
            return writer.toString();
        }
    }

    private RuntimeSnapshot deserialize(String content) throws IOException {
        Properties props = new Properties();
        try (StringReader reader = new StringReader(content)) {
            props.load(reader);
        }

        RuntimeSnapshot snapshot = new RuntimeSnapshot();

        String state = props.getProperty("state");
        if (state != null && !state.isBlank()) {
            snapshot.setState(RuntimeState.valueOf(state));
        }

        Map<String, String> deployed = new LinkedHashMap<>();
        Map<String, String> running = new LinkedHashMap<>();

        for (String name : props.stringPropertyNames()) {
            if (name.startsWith("deployed.")) {
                deployed.put(name.substring("deployed.".length()), props.getProperty(name));
            } else if (name.startsWith("running.")) {
                running.put(name.substring("running.".length()), props.getProperty(name));
            }
        }

        snapshot.setDeployedComponents(deployed);
        snapshot.setRunningInstances(running);

        return snapshot;
    }
}