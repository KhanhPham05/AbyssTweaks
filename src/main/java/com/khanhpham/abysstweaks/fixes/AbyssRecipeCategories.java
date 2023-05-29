package com.khanhpham.abysstweaks.fixes;

import com.khanhpham.abysstweaks.AbyssTweaks;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yezon.theabyss.init.TheabyssModItems;
import net.yezon.theabyss.recipes.AbyssRecipeType;
import net.yezon.theabyss.recipes.AllRecipeTypes;

import java.util.List;

@Mod.EventBusSubscriber(modid = AbyssTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AbyssRecipeCategories {
    public static final RecipeBookType ARCANE_BOOK_TYPE = RecipeBookType.create("arcane_book_type");
    public static final RecipeBookType SOMNIUM_BOOK_TYPE = RecipeBookType.create("somnium_book_type");
    public static final RecipeBookType M_A_P_BOOK_TYPE = RecipeBookType.create("m_a_p_book_type");

    @SubscribeEvent
    public static void register(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(ARCANE_BOOK_TYPE, List.of(Categories.ARCANE));
        event.registerBookCategories(SOMNIUM_BOOK_TYPE, List.of(Categories.SOMNIUM));
        event.registerBookCategories(M_A_P_BOOK_TYPE, List.of(Categories.M_A_P));

        registerFinder(event, AllRecipeTypes.SOMNIUM_INFUSING, Categories.SOMNIUM);
        registerFinder(event, AllRecipeTypes.ARCANE_CRAFTING, Categories.ARCANE);
        registerFinder(event, AllRecipeTypes.MORTAR_AND_PESTLE, Categories.M_A_P);

    }

    private static void registerFinder(RegisterRecipeBookCategoriesEvent event, AbyssRecipeType recipeType, RecipeBookCategories categories) {
        event.registerRecipeCategoryFinder(recipeType.getVanillaType(), r -> categories);
    }

    //Because this can ONLY be loaded in client so a separate (inner) class can fix the server crash due to invalid dist
    private static final class Categories {
        public static final RecipeBookCategories ARCANE = RecipeBookCategories.create("arcane_station_lookup", new ItemStack(TheabyssModItems.ARCANE_WORKBENCH.get()));
        public static final RecipeBookCategories SOMNIUM = RecipeBookCategories.create("somnium_lookup", new ItemStack(TheabyssModItems.SOMNIUM.get()));
        public static final RecipeBookCategories M_A_P = RecipeBookCategories.create("m_a_p_lookup", new ItemStack(TheabyssModItems.MORTAR_AND_PESTLE.get()));

    }
}
