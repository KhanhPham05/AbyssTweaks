package com.khanhpham.abysstweaks.ct.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.khanhpham.abysstweaks.ModUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.yezon.theabyss.recipes.AllRecipeTypes;
import net.yezon.theabyss.recipes.impl.MortarAndPestleRecipe;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nonnull;

@ZenRegister
@SuppressWarnings("unused")
@ZenCodeType.Name("net.yezon.theabyss.recipes.MortarAndPestleRecipe")
public enum MortarManager implements IRecipeManager<Recipe<Container>> {
    @ZenCodeGlobals.Global(ModUtils.MAP)
    INSTANCE;

    @ZenCodeType.Method
    public void addTwoIngredientsRecipe(String recipeName, IItemStack result, IIngredient bottle, IIngredient[] ingredients, @ZenCodeType.OptionalBoolean(true) boolean mirror) {
        ModUtils.checkArray(ingredients, 2);
        addRecipe(recipeName, result, bottle, new IIngredient[]{ingredients[0], ingredients[1], ingredients[0], ingredients[1]});
        if (mirror)
            addRecipe(recipeName + "_mirrored", result, bottle, new IIngredient[]{ingredients[1], ingredients[0], ingredients[1], ingredients[0]});
    }

    @ZenCodeType.Method
    public void addSingleIngredientRecipe(String recipeName, IItemStack result, IIngredient bottle, IIngredient ingredient) {
        addRecipe(recipeName, result, bottle, new IIngredient[]{ingredient, ingredient, ingredient, ingredient});
    }

    @ZenCodeType.Method
    public void addRecipe(String recipeName, IItemStack result, IIngredient bottle, IIngredient[] array) {
        ModUtils.checkArray(array, 4);
        NonNullList<Ingredient> ingredients = NonNullList.withSize(5, Ingredient.EMPTY);
        ingredients.set(0, array[0].asVanillaIngredient());
        ingredients.set(1, array[1].asVanillaIngredient());
        ingredients.set(2, bottle.asVanillaIngredient());
        ingredients.set(3, array[2].asVanillaIngredient());
        ingredients.set(4, array[3].asVanillaIngredient());

        apply(recipeName, result, ingredients);
    }

    private void apply(String recipeName, IItemStack result, @Nonnull NonNullList<Ingredient> ingredients) {
        ResourceLocation recipeId = CraftTweakerConstants.rl(fixRecipeName(recipeName));
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new MortarAndPestleRecipe(recipeId, result.getInternal(), ingredients)));
    }

    @Override
    public RecipeType<Recipe<Container>> getRecipeType() {
        return AllRecipeTypes.MORTAR_AND_PESTLE.getVanillaType();
    }
}
