package com.khanhpham.abysstweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.yezon.theabyss.recipes.ModRecipeCategories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AbyssTweaks.MODID)
public class AbyssTweaks {
    public static final String MODID = "abysstweaks";
    public static final Logger LOGGER = LogManager.getLogger();

    public AbyssTweaks() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(this);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(AbyssTweaks::data);
    }

    public static void data(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeClient(),
                new LanguageProvider(event.getGenerator(), MODID, "en_us") {
                    @Override
                    protected void addTranslations() {
                        super.add("jei.theabyss.title.arcane_crafting", "Arcane Crafting");
                        super.add("jei.theabyss.title.somnium_infusing", "Somnium Infusing");
                    }
                }
        );
    }
}
