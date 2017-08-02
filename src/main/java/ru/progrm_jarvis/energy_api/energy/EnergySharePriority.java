package ru.progrm_jarvis.energy_api.energy;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public enum EnergySharePriority {
    ///////////////////////////////////////////////////////////////////////////
    // Energy taking order:
    // [×] --> [] --> [] --> [] --> [] --> [] --> [] --> [] --> [✓]
    ///////////////////////////////////////////////////////////////////////////
    NEVER, LOWEST, LOWER, LOW, NORMAL, HIGH, HIGHER, HIGHEST, ALWAYS
}
