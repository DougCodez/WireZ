/**
 * MIT License
 *
 * Copyright (c) 2022-2023 Douglas (dougcodez)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.dougcodez.wirezbungee;

import io.github.dougcodez.wirez.WireZPluginExtension;
import io.github.dougcodez.wirez.commands.sub.SubCommandRegistry;
import io.github.dougcodez.wirez.commands.sub.types.impl.database.DumpTable;
import io.github.dougcodez.wirez.files.types.lang.LangFile;
import io.github.dougcodez.wirez.files.types.settings.WireZSettingsFile;
import io.github.dougcodez.wirez.monitors.MonitorManager;
import io.github.dougcodez.wirez.monitors.task.SystemsThreadExecutor;
import io.github.dougcodez.wirez.platform.PlatformInfo;
import io.github.dougcodez.wirez.platform.PlatformType;
import io.github.dougcodez.wirez.promise.PromiseGlobalExecutor;
import io.github.dougcodez.wirez.websocket.WireZDataTransfer;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Getter
public class WireZPlugin extends Plugin implements WireZPluginExtension, PlatformInfo {

    private static WireZPlugin plugin;
    private WireZSettingsFile settingsFile;
    private BungeeLibraryManager libraryManager;
    private BungeeLibrarySetup bungeeLibrarySetup;


    public void onEnable() {
        plugin = this;
        this.getLogger().log(Level.INFO, getPlatformInfo());
        settingsFile = new WireZSettingsFile();
        settingsFile.initConfigFile(getDataFolder());


        MonitorManager.registerMonitors();
        SystemsThreadExecutor.call();
        registerInstantiations();
        registerLibraries();
        registerFiles();
        registerCommands();

        if (settingsFile.getConfigFile().getBoolean(("website-graph.enable"))) {
            String host = settingsFile.getConfigFile().getString("website-graph.host");
            int port = settingsFile.getConfigFile().getInt("website-graph.port");
            WireZDataTransfer dataTransfer = new WireZDataTransfer(host, port);
            this.getProxy().getScheduler().schedule(this, dataTransfer::sendData, 20, TimeUnit.SECONDS);
        }

        if (settingsFile.getConfigFile().getBoolean("database-module.enabled")) {
            if (SubCommandRegistry.getSubCommandMap().containsKey(DumpTable.class.getSimpleName())) {
                DumpTable dumpTable = (DumpTable) SubCommandRegistry.getSubCommandMap().get(DumpTable.class.getSimpleName());
                dumpTable.setDataFolder(this::getDataFolder);
            }
        }
    }

    public void onDisable() {
        this.getLogger().log(Level.INFO, "WireZ is shutting down...");
        SystemsThreadExecutor.close();
        PromiseGlobalExecutor.close();
    }

    @Override
    public void registerInstantiations() {
        libraryManager = new BungeeLibraryManager(this);
        bungeeLibrarySetup = new BungeeLibrarySetup();
    }

    @Override
    public void registerLibraries() {
        bungeeLibrarySetup.loadLibraries();
    }

    @Override
    public void registerFiles() {
        LangFile.getInstance().initLangFile(getDataFolder());
    }

    @Override
    public void registerCommands() {
        SubCommandRegistry.registerCommands(getSettingsFile());
        getProxy().getPluginManager().registerCommand(plugin, new WireZCommandImpl());
    }

    public static WireZPlugin getInstance() {
        return plugin;
    }

    @Override
    public String getID() {
        return "WireZ Bungee";
    }

    @Override
    public PlatformType getType() {
        return PlatformType.PROXY;
    }

    @Override
    public String getVersion() {
        return "1.0.1";
    }
}
