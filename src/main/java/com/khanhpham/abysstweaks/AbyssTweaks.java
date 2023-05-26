package com.khanhpham.abysstweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AbyssTweaks.MODID)
public class AbyssTweaks {
    public static final String MODID ="abysstweaks";
    public static final Logger LOGGER = LogManager.getLogger();

    public AbyssTweaks() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(this);
    }
}
