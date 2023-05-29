package com.khanhpham.abysstweaks.ct.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.khanhpham.abysstweaks.AbyssTweaks;
import com.khanhpham.abysstweaks.ModUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.yezon.theabyss.recipes.AllRecipeTypes;
import net.yezon.theabyss.recipes.impl.ArcaneStationRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@ZenRegister
@ZenCodeType.Name("net.yezon.theabyss.recipes.ArcaneStationRecipe")
public enum ArcaneRecipeManager implements IRecipeManager<Recipe<Container>> {

    @ZenCodeGlobals.Global("arcaneStation")
    INSTANCE;

    ArcaneRecipeManager() {
        AbyssTweaks.LOGGER.info("initializing arcane recipe manager");
    }

    @Override
    public RecipeType<Recipe<Container>> getRecipeType() {
        return AllRecipeTypes.ARCANE_CRAFTING.getVanillaType();
    }

    @ZenCodeType.Method
    public void addFullCircleRecipe(String recipeName, IItemStack result, IIngredient ingredient, IIngredient core) {
        final NonNullList<Ingredient> ingredients = NonNullList.withSize(9, ingredient.asVanillaIngredient());
        this.apply(recipeName, result, ingredients, core);
    }

    @ZenCodeType.Method
    public void addTwoIngredientsRecipe(String recipeName, IItemStack result, IIngredient[] ingredients, IIngredient center, @ZenCodeType.OptionalBoolean(true) boolean createMirror) {
        ModUtils.checkArray(ingredients, 2);
        final NonNullList<Ingredient> list = NonNullList.withSize(9, Ingredient.EMPTY);
        final @Nullable NonNullList<Ingredient> mirrorList = createMirror ? NonNullList.withSize(9, Ingredient.EMPTY) : null;

        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0, 2, 4, 6 -> {
                    list.set(i, ingredients[0].asVanillaIngredient());
                    if (mirrorList != null) mirrorList.set(i, ingredients[1].asVanillaIngredient());
                }
                case 1, 3, 5, 7 -> {
                    list.set(i, ingredients[1].asVanillaIngredient());
                    if (mirrorList != null) mirrorList.set(i, ingredients[0].asVanillaIngredient());
                }
            }
        }

        this.apply(recipeName, result, list, center);

        //mirror
        this.apply(recipeName + "_mirrored", result, mirrorList, center);
    }

    @ZenCodeType.Method
    public void addHalfHalfRecipe(String recipeName, IItemStack result, IIngredient[] ingredients, IIngredient core, @ZenCodeType.OptionalBoolean(true) boolean createMirror) {
        ModUtils.checkArray(ingredients, 2);
        final NonNullList<Ingredient> list = NonNullList.withSize(9, Ingredient.EMPTY);
        final @Nullable NonNullList<Ingredient> mirrorList = createMirror ? NonNullList.withSize(9, Ingredient.EMPTY) : null;
        for (int i = 0; i < 8; i++) {
            if (i <= 3) {
                list.set(i, ingredients[0].asVanillaIngredient());
                if (mirrorList != null) mirrorList.set(i, ingredients[1].asVanillaIngredient());
            } else {
                list.set(i, ingredients[1].asVanillaIngredient());
                if (mirrorList != null) mirrorList.set(i, ingredients[0].asVanillaIngredient());
            }
        }

        this.apply(recipeName, result, list, core);

        //mirrored
        this.apply(recipeName + "_mirrored", result, mirrorList, core);
    }

    private void apply(String recipeName, IItemStack result, NonNullList<Ingredient> ingredients, IIngredient core) {
        if (ingredients != null) {
            ResourceLocation resourceLocation = CraftTweakerConstants.rl(fixRecipeName(recipeName));
            AbyssTweaks.LOGGER.info("Creating Arcane Recipe [{}]", resourceLocation);
            CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
                    new ArcaneStationRecipe(
                            resourceLocation,
                            ingredients,
                            core.asVanillaIngredient(),
                            result.getInternal()
                    )));
        }
    }
}
