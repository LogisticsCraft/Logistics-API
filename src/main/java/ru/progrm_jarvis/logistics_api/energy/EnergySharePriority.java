package ru.progrm_jarvis.logistics_api.energy;

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
