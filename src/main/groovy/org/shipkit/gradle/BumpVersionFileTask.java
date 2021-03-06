package org.shipkit.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;
import org.shipkit.internal.version.Version;
import org.shipkit.internal.version.VersionInfo;

import java.io.File;

/**
 * Increments version in specified file.
 * The file is expected to have a version property declared, for example: "version=0.0.1"
 */
public class BumpVersionFileTask extends DefaultTask {

    private final static Logger LOG = Logging.getLogger(BumpVersionFileTask.class);

    private File versionFile;

    /**
     * File that contains version number information, for example: "version=0.0.1"
     */
    @InputFile
    public File getVersionFile() {
        return versionFile;
    }

    /**
     * See {@link #getVersionFile()}
     */
    public void setVersionFile(File versionFile) {
        this.versionFile = versionFile;
    }

    /**
     * See {@link BumpVersionFileTask}
     */
    @TaskAction
    public VersionInfo bumpVersionFile() {
        VersionInfo versionInfo = Version.versionInfo(this.versionFile);
        VersionInfo newVersion = versionInfo.bumpVersion();
        LOG.lifecycle(versionMessage(this, newVersion));
        return newVersion;
    }

    static String versionMessage(BumpVersionFileTask task, VersionInfo newVersion) {
        String relativePath = task.getProject().relativePath(task.getVersionFile());
        return task.getPath() + " - updated version file '" + relativePath + "'\n" +
                "  - new version: " + newVersion.getVersion() + "\n" +
                "  - previous version: " + newVersion.getPreviousVersion();
    }
}
