package com.tatakae.admin.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tatakae.admin.core.Exceptions.CannotCreateFileException;
import com.tatakae.admin.core.Exceptions.FailedLoadingPluginException;
import com.tatakae.admin.core.Exceptions.PluginNotFoundException;

public class PluginManager {
    public List<PluginEnvironment> environmentsActive = new ArrayList<>();
    public List<PluginEnvironment> environmentsInactive = new ArrayList<>();
    public Map<PluginEnvironment, String> environments = new HashMap<>();

    private static PluginManager instance = null;

    public PluginManager() {
        this.loadPlugins();
    }

    public static PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    public void startPlugins() {
        this.environmentsActive.forEach(env -> env.getPlugin().start());
    }

    private void loadPlugins() {
        this.environmentsActive = getEnvironments("active");
        this.environmentsInactive = getEnvironments("inactive");

        initEnvironments();
    }

    private void initEnvironments() {
        environments.clear();
        for (final var env : environmentsActive) {
            environments.put(env, "Disable");
        }

        for (final var env : environmentsInactive) {
            environments.put(env, "Enable");
        }
    }

    private void loadAllClasses(URLClassLoader loader, ArrayList<String> classes) {
        try {
            for(final var item : classes) {
                loader.loadClass(item);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private PluginEnvironment loadPlugin(final Path path) throws FailedLoadingPluginException {
        try {
            final var url = new URL[]{new URL("file:///" + path)};
            final var loader = new URLClassLoader(url, PluginManager.class.getClassLoader());

            final var pluginClass = Class.forName("Extension", true, loader);

            final var plugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();

            loadAllClasses(loader, plugin.getClassesName(path));

            loader.close();
            return new PluginEnvironment(loader, plugin);

        } catch (Exception e) {
            throw new FailedLoadingPluginException(
                    "Failed to load the plugin at path '" + path + "': " + e.getMessage());
        }
    }

    private List<PluginEnvironment> getEnvironments(final String name) {
        try {
            final var pluginsDirectory = LocalDataManager.getDirectory("plugins/" + name);

            Stream<Path> paths = Files.walk(pluginsDirectory.toPath());

            final var environments = paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains(".jar"))
                    .map((path) -> {
                        try {
                            return this.loadPlugin(path);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                            e.printStackTrace();

                            return null;
                        }
                    });

            final var res = environments.filter(Objects::nonNull).collect(Collectors.toList());
            paths.close();

            return res;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean importPlugin(final File file) {
        try {
            final var env = loadPlugin(file.toPath());

            for (final var environment : environments.entrySet()) {
                if (environment.getKey().getPlugin().getName().equals(env.getPlugin().getName())) {
                    return false;
                }
            }
            env.getLoader().close();

            final var pluginDirectory = LocalDataManager.getDirectory("plugins/inactive").toPath();
            final var destination = pluginDirectory.resolve(file.getName());

            Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            final var environment = loadPlugin(destination);
            environmentsInactive.add(environment);
            environments.put(environment, "Enable");

            return Files.exists(destination);

        } catch (CannotCreateFileException | IOException | FailedLoadingPluginException e) {
            return false;
        }
    }

    public boolean findAndMovePlugin(final String name, final String action) {
        try {
            final var activePluginDirectory = LocalDataManager.getDirectory("plugins/active");
            final var inactivePluginDirectory = LocalDataManager.getDirectory("plugins/inactive");

            Stream<Path> activePaths = Files.walk(activePluginDirectory.toPath());

            final var activeFilesList = activePaths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains(".jar"))
                    .collect(Collectors.toList());
            activePaths.close();

            Stream<Path> inactivePaths = Files.walk(inactivePluginDirectory.toPath());

            final var inactiveFilesList = inactivePaths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains(".jar"))
                    .collect(Collectors.toList());
            inactivePaths.close();

            boolean hasBeenMoved = false;

            final var pluginEnv = getEnvironmentByPluginName(name);

            if (action.equals("Enable")) {
                final var sourceFile = getJarFilenameFromPluginName(name, inactiveFilesList);
                if (sourceFile.isPresent()) {
                    removePluginFromEnvironment(pluginEnv, true);
                    hasBeenMoved = moveToActive(pluginEnv, sourceFile.get().toPath());
                }
            } else {
                final var sourceFile = getJarFilenameFromPluginName(name, activeFilesList);
                if (sourceFile.isPresent()) {
                    removePluginFromEnvironment(pluginEnv, false);
                    hasBeenMoved = moveToInactive(pluginEnv, sourceFile.get().toPath());
                }
            }

            return hasBeenMoved;

        } catch (CannotCreateFileException | PluginNotFoundException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean moveToActive(final PluginEnvironment pluginEnv,
                                 final Path source) {
        try {
            Path destination = LocalDataManager.getDirectory("plugins/active").toPath();
            String extractDirName = pluginEnv.getPlugin().getExtractionDirectoryName();
            Path fileDestination = destination.resolve(source.getFileName());

            Files.move(source, fileDestination, StandardCopyOption.REPLACE_EXISTING);

            File extractDirectory = new File(destination.resolve(extractDirName).toString());
            extractDirectory.mkdir();

            pluginEnv.getPlugin().autoExtractResources(fileDestination, extractDirectory.toPath());

            pluginEnv.getPlugin().start();

            environmentsActive.add(pluginEnv);

            environments.put(pluginEnv, "Disable");

            return true;

        } catch (CannotCreateFileException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean moveToInactive(final PluginEnvironment pluginEnv,
                                 final Path source) {
        try {

            Path destination = LocalDataManager.getDirectory("plugins/inactive").toPath();

            Path fileDestination = destination.resolve(source.getFileName());

            Files.move(source, fileDestination, StandardCopyOption.REPLACE_EXISTING);

            String extractDirName = pluginEnv.getPlugin().getExtractionDirectoryName();

            environmentsInactive.add(pluginEnv);

            removePluginDirectory(source.getParent().resolve(extractDirName));

            environments.put(pluginEnv, "Enable");

            return true;

        } catch (CannotCreateFileException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removePluginFromEnvironment(final PluginEnvironment pluginEnvironment, boolean activeEnv)
            throws IOException {

        pluginEnvironment.getLoader().close();

        if (!activeEnv) {
            environmentsInactive.remove(pluginEnvironment);
        } else {
            environmentsActive.remove(pluginEnvironment);
        }

        environments.remove(pluginEnvironment);
    }

    public PluginEnvironment getEnvironmentByPluginName(String pluginName) throws PluginNotFoundException {
        for (var env : environments.entrySet()) {
            if (env.getKey().getPlugin().getName().equals(pluginName)) {
                return env.getKey();
            }
        }
        throw new PluginNotFoundException("Plugin named: " + pluginName + " does not exist.");
    }

    public Optional<File> getJarFilenameFromPluginName(String name, List<Path> sourceDirectory) {
        try {
            for (final var file : sourceDirectory) {
                final var env = loadPlugin(file);

                if (env.getPlugin().getName().equals(name)) {
                    env.getLoader().close();

                    return Optional.of(file.toFile());
                }

                env.getLoader().close();
            }
        } catch (FailedLoadingPluginException | IOException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    public String getPluginView(final String name) {
        try {
            final var env = getEnvironmentByPluginName(name);
            String extractDirectory = env.getPlugin().getExtractionDirectoryName();
            final var sourceDir = LocalDataManager.getDirectory("plugins/active/" + extractDirectory);

            Stream<Path> paths = Files.walk(sourceDir.toPath());

            final var view = paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains(env.getPlugin().getMainViewName()))
                    .collect(Collectors.toList());

            paths.close();

            return view.get(0).toString();

        } catch (IOException | PluginNotFoundException | CannotCreateFileException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void removePluginDirectory(final Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}