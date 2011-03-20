package de.funky_clan.mc.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class RunScript implements Event {
    private String fileName;
    private boolean useClasspath;

    public RunScript(String fileName, boolean useClasspath) {
        this.fileName = fileName;
        this.useClasspath = useClasspath;
    }

    public RunScript(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isUseClasspath() {
        return useClasspath;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
