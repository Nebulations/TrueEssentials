package me.nebu.trueEssentials.extensions;

public class BaseExtension implements Extension {
    private final ExtensionName id;
    private final boolean enabled;

    public BaseExtension(ExtensionName id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    @Override
    public void reload() {}

    @Override
    public ExtensionName id() {
        return id;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }
}
