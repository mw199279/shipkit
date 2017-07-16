package org.shipkit.internal.gradle.contributors;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.shipkit.gradle.ReleaseConfiguration;
import org.shipkit.internal.notes.contributors.AllContributorsSerializer;
import org.shipkit.internal.notes.contributors.ContributorsProvider;
import org.shipkit.internal.notes.contributors.ProjectContributorsSet;
import org.shipkit.internal.notes.util.IOUtil;

import java.io.File;

/**
 * Fetch data about all project contributors and store it in file.
 * It is used later in generation pom.xml.
 * It use GitHub repos/contributors endpoint: https://developer.github.com/v3/repos/#list-contributors
 * "Contributors data is cached for performance reasons. This endpoint may return information that is a few hours old."
 */
public abstract class AllContributorsFetcherTask extends DefaultTask {

    private static final Logger LOG = Logging.getLogger(AllContributorsFetcherTask.class);

    @Input private String apiUrl;
    @Input private String repository;
    @Input private String readOnlyAuthToken;

    @OutputFile private File outputFile;

    /**
     * See {@link ReleaseConfiguration.GitHub#getApiUrl()}
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * See {@link #getApiUrl()}
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * See {@link ReleaseConfiguration.GitHub#getRepository()}
     */
    public String getRepository() {
        return repository;
    }

    /**
     * See {@link #getRepository()}
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /**
     * See {@link ReleaseConfiguration.GitHub#getReadOnlyAuthToken()}
     */
    public String getReadOnlyAuthToken() {
        return readOnlyAuthToken;
    }

    /**
     * See {@link #getReadOnlyAuthToken()}
     */
    public void setReadOnlyAuthToken(String readOnlyAuthToken) {
        this.readOnlyAuthToken = readOnlyAuthToken;
    }

    /**
     * Where serialized information about contributors will be stored.
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * See {@link #getOutputFile()}
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    protected abstract ContributorsProvider getContributorsProvider();

    @TaskAction
    public void fetchContributors() {
        LOG.lifecycle("  Fetching all contributors for project");

        ContributorsProvider contributorsProvider = getContributorsProvider();
        ProjectContributorsSet contributors = contributorsProvider.getAllContributorsForProject();

        AllContributorsSerializer serializer = new AllContributorsSerializer();
        final String json = serializer.serialize(contributors);
        IOUtil.writeFile(outputFile, json);

        LOG.lifecycle("  Serialized all contributors into: {}", getProject().relativePath(outputFile));
    }
}
