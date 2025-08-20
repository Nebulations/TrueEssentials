package me.nebu.trueEssentials.extensions;

public interface Extension {

    ExtensionName id();
    boolean enabled();
    void reload();

}
