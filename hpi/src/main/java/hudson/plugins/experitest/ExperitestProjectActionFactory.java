package hudson.plugins.experitest;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ariel Hazan
 */
@Extension
public class ExperitestProjectActionFactory extends TransientProjectActionFactory {
    @Override
    public Collection<? extends Action> createFor(AbstractProject target) {
        return Collections.singleton(new ExperitestProjectAction(target));
    }
}
