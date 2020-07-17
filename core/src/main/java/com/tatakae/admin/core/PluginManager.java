package com.tatakae.admin.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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

    private PluginEnvironment loadPlugin(final Path path) throws FailedLoadingPluginException {
        try {
            final var url = new URL[]{new URL("file:///" + path)};
            final var loader = new URLClassLoader(url, PluginManager.class.getClassLoader());

            final var pluginClass = Class.forName("Extension", true, loader);
            final var plugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();

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

            boolean hasBeenMoved;

            if (action.equals("Disable")) {
                hasBeenMoved = movePlugin(name, activeFilesList, inactivePluginDirectory.toPath(), false);
            } else {
                hasBeenMoved = movePlugin(name, inactiveFilesList, activePluginDirectory.toPath(), true);
            }

            return hasBeenMoved;

        } catch (CannotCreateFileException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean move(final PluginEnvironment pluginEnv,
                         final Path source,
                         final Path destination,
                         final boolean moveToActiveDirectory) {
        try {
            removePluginFromEnvironment(pluginEnv, moveToActiveDirectory);

            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);

            final var env = loadPlugin(destination);

            if (moveToActiveDirectory) {
                env.getPlugin().start();
                environmentsActive.add(env);
                extractResources(destination);
            } else {
                environmentsInactive.add(env);

                final var inactivePath = LocalDataManager.getDirectory("plugins/inactive").toPath();

                Stream<Path> paths = Files.walk(inactivePath);

                final var pluginDirectories = paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().contains(".jar"))
                        .collect(Collectors.toList());

                paths.close();

                var jarName = getJarFilenameFromPluginName(env.getPlugin().getName(), pluginDirectories);

                if (jarName.isPresent()) {
                    final var activePath = LocalDataManager.getDirectory("plugins/active").toPath();
                    final var dirname = jarName.get().getName().replaceFirst("[.][^.]+$", "");
                    removePluginDirectory(activePath.resolve(dirname));
                }
            }

            final var action = moveToActiveDirectory ? "Disable" : "Enable";
            environments.put(env, action);

            return true;

        } catch (IOException | FailedLoadingPluginException | CannotCreateFileException e) {
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

    private boolean movePlugin(String pluginName, List<Path> pluginFiles, Path destinationDirectory, boolean activate) {
        try {
            final var sourceFile = getJarFilenameFromPluginName(pluginName, pluginFiles);

            if (sourceFile.isPresent()) {
                final var destinationPath = destinationDirectory.resolve(sourceFile.get().getName());
                final var pluginEnv = getEnvironmentByPluginName(pluginName);

                return move(pluginEnv, sourceFile.get().toPath(), destinationPath, activate);

            } else {
                return false;
            }
        } catch (PluginNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
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

    private void extractResources(final Path plugin) {
        try {
            final var dirname = plugin.toFile().getName().replaceFirst("[.][^.]+$", "");
            final var pluginDir = LocalDataManager.getDirectory("plugins/active/".concat(dirname));

            JarFile jar = new JarFile(plugin.toFile());
            Enumeration<JarEntry> enumEntries = jar.entries();

            while (enumEntries.hasMoreElements()) {
                JarEntry file = enumEntries.nextElement();
                File f = new File(pluginDir.toString().concat("/" + file.getName()));

                if (file.isDirectory()) {
                    f.mkdir();
                    continue;
                }

                InputStream is = jar.getInputStream(file);
                FileOutputStream fos = new FileOutputStream(f);

                while (is.available() > 0) {
                    fos.write(is.read());
                }

                fos.close();
                is.close();
            }

            jar.close();

        } catch (CannotCreateFileException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getPluginView(final String name) {
        try {
            final var env = getEnvironmentByPluginName(name);
            final var sourceDir = LocalDataManager.getDirectory("plugins/active");
            Stream<Path> paths = Files.walk(sourceDir.toPath());

            final var views = paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains(".jar"))
                    .collect(Collectors.toList());

            paths.close();

            final var jarName = getJarFilenameFromPluginName(name, views);

            if (jarName.isPresent()) {
                final var viewDir = jarName.get().toString().replaceFirst("[.][^.]+$", "");

                return viewDir + "\\" + env.getPlugin().getMainViewName();
            }

            return "";

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