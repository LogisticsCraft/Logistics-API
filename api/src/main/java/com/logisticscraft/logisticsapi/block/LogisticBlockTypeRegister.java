package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LogisticBlockTypeRegister {

    private Map<LogisticKey, Class<? extends LogisticBlock>> blockTypes = new HashMap<>();

}
