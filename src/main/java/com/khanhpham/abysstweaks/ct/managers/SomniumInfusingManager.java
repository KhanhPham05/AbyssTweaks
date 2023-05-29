package com.khanhpham.abysstweaks.ct.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.google.common.base.Preconditions;
import com.khanhpham.abysstweaks.NameUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.yezon.theabyss.recipes.AllRecipeTypes;
import net.yezon.theabyss.recipes.impl.SomniumInfusingRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@SuppressWarnings("unused")
@ZenRegister
@ZenCodeType.Name("net.yezon.theabyss.recipes.SomniumInfusingRecipe")
public enum SomniumInfusingManager implements IRecipeManager<Recipe<Container>> {

    @ZenCodeGlobals.Global(NameUtils.SOMNIUM_INFUSER)
    INSTANCE;

    private static final int defaultTime = 200;

    SomniumInfusingManager() {}

    @Override
    public RecipeType<Recipe<Container>> getRecipeType() {
        return AllRecipeTypes.SOMNIUM_INFUSING.getVanillaType();
    }

    @ZenCodeType.Method
    public void addHalfHalfRecipe(String recipeName, IItemStack result, IIngredient[] ingredients,  @ZenCodeType.OptionalBoolean(true) boolean mirror) {
        addHalfHalfRecipe(recipeName, result, ingredients, defaultTime, mirror);
    }

    @ZenCodeType.Method
    public void addHalfHalfRecipe(String recipeName, IItemStack result, IIngredient[] ingredients, @ZenCodeType.OptionalInt(defaultTime) int processTime, @ZenCodeType.OptionalBoolean(true) boolean mirror) {
        if (processTime <= 20) processTime = defaultTime;
        Preconditions.checkArgument(ingredients.length == 2, "Ingredients array must only sized 2, but %s found".formatted(ingredients.length));
        apply(recipeName, result, new IIngredient[]{ingredients[0], ingredients[0], ingredients[1], ingredients[1]}, processTime);
        if (mirror) {
            apply(recipeName + "_mirrored", result, new IIngredient[]{ingredients[1], ingredients[1], ingredients[0], ingredients[0]}, processTime);
        }
    }

    @ZenCodeType.Method
    public void addTwoIngredientsRecipe(String recipeName, IItemStack result, IIngredient[] ingredients,  @ZenCodeType.OptionalBoolean(true) boolean mirror) {
        addTwoIngredientsRecipe(recipeName, result, ingredients, defaultTime, mirror);
    }

    @ZenCodeType.Method
    public void addTwoIngredientsRecipe(String recipeName, IItemStack result, IIngredient[] ingredients, @ZenCodeType.OptionalInt(defaultTime) int processTime, @ZenCodeType.OptionalBoolean(true) boolean mirror) {
        if (processTime <= 20) processTime = defaultTime;
        Preconditions.checkArgument(ingredients.length == 2, "Ingredients array must only sized 2, but %s found".formatted(ingredients.length));
        apply(recipeName, result, new IIngredient[]{ingredients[0], ingredients[1], ingredients[0], ingredients[1]}, processTime);
        if (mirror) {
            apply(recipeName + "_mirrored", result, new IIngredient[]{ingredients[1], ingredients[0], ingredients[1], ingredients[0]}, processTime);
        }
    }

    @ZenCodeType.Method
    public void addSingleIngredientRecipe(String recipeName, IItemStack result, IIngredient ingredient,  @ZenCodeType.OptionalInt(defaultTime) int processTime) {
        apply(recipeName, result, new IIngredient[]{ingredient, ingredient, ingredient, ingredient}, processTime);
    }

    @ZenCodeType.Method
    public void addRecipe(String recipeName, IItemStack result, IIngredient[] ingredients, @ZenCodeType.OptionalInt(defaultTime) int processTime) {
        this.apply(recipeName, result, ingredients, processTime);
    }

    private void apply(String recipeName, IItemStack result, IIngredient[] ingredients, int time) {
        Preconditions.checkArgument(ingredients.length == 4);
        ResourceLocation recipeId = CraftTweakerConstants.rl(fixRecipeName(recipeName));
        final NonNullList<Ingredient> list = NonNullList.withSize(6, Ingredient.EMPTY);
        list.set(0, SomniumInfusingRecipe.SOMNIUM_FUEL);
        list.set(1, SomniumInfusingRecipe.LORAN_FUEL);
        for (int i = 0; i < ingredients.length; i++) {
            list.set(i + 2, ingredients[i].asVanillaIngredient());
        }

        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new SomniumInfusingRecipe(recipeId, result.getInternal(), list, time)));
    }
}