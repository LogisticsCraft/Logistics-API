package com.logisticscraft.logisticsapi.excluded.energy.storage;

public enum EnergySharePriority {
    ///////////////////////////////////////////////////////////////////////////
    // Energy taking order:
    // [×] --> [] --> [] --> [] --> [] --> [] --> [] --> [] --> [✓]
    ///////////////////////////////////////////////////////////////////////////
    NEVER, LOWEST, LOWER, LOW, NORMAL, HIGH, HIGHER, HIGHEST, ALWAYS
}
