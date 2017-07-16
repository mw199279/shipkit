package org.shipkit.internal.gradle.contributors;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.shipkit.gradle.ReleaseConfiguration;
import org.shipkit.internal.gradle.configuration.ReleaseConfigurationPlugin;
import org.shipkit.internal.gradle.util.TaskMaker;

import static org.shipkit.internal.gradle.util.BuildConventions.contributorsFile;

/**
 * Adds and configures tasks for getting contributor git user to GitHub user mappings.
 * Useful for release notes and pom.xml generation. Adds tasks:
 * <ul>
 *     <li>fetchAllContributors - {@link AllContributorsFetcherTask}</li>
 * </ul>
 */
public abstract class ContributorsPlugin implements Plugin<Project> {

    public final static String FETCH_ALL_CONTRIBUTORS_TASK = "fetchAllContributors";

    public void apply(final Project project) {
        final ReleaseConfiguration conf = project.getPlugins().apply(ReleaseConfigurationPlugin.class).getConfiguration();
        fetchAllTask(project, conf);
    }

    protected abstract void fetchAllTask(Project project, ReleaseConfiguration conf);

}


