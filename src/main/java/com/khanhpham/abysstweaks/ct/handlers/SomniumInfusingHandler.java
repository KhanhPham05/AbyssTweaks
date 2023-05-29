package com.khanhpham.abysstweaks.ct.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.IngredientUtil;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.khanhpham.abysstweaks.NameUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.yezon.theabyss.recipes.impl.SomniumInfusingRecipe;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@IRecipeHandler.For(SomniumInfusingRecipe.class)
public class SomniumInfusingHandler implements IRecipeHandler<SomniumInfusingRecipe> {
    @Override
    public String dumpToCommandString(IRecipeManager<? super SomniumInfusingRecipe> manager, SomniumInfusingRecipe recipe) {
        final Pair<String, String> recipeFormat = getRecipeFormat(recipe);

        return String.format("%s.%s(%s, %s, %s, %s);",
                NameUtils.SOMNIUM_INFUSER,
                recipeFormat.getFirst(),
                StringUtil.quoteAndEscape(recipe.getId()),
                IItemStack.of(recipe.getResultItem()).getCommandString(),
                recipeFormat.getSecond(),
                recipe.getProcessDuration());
    }

    private static Pair<String, String> getRecipeFormat(SomniumInfusingRecipe recipe) {

        final String[] ingredients = getIngredientStrings(recipe.getIngredients());
        final String ingredient0 = ingredients[0];

        //one ingredient

        for (int i = 1; i < ingredients.length; i++) {
            if (ingredient0.equals(ingredients[i])) {
                if (i == ingredients.length - 1) {
                    return Pair.of("addSingleIngredientRecipe", ingredient0);
                }
            } else break;
        }

        //two ingredients

        final String ingredient1 = ingredients[1];
        if (ingredient0.equals(ingredients[2]) && ingredient1.equals(ingredients[3])) {
            return Pair.of("addTwoIngredientsRecipe", NameUtils.toArrayString(new String[]{ingredient0, ingredient1}, false));
        }

        //half-half
        if (ingredient0.equals(ingredient1) && ingredients[2].equals(ingredients[3])) {
            return Pair.of("addHalfHalfRecipe", NameUtils.toArrayString(new String[]{ingredient0, ingredients[2]}, false));
        }

        //four ingredients
        return Pair.of("addRecipe", NameUtils.toArrayString(ingredients, false));
    }

    private static String[] getIngredientStrings(NonNullList<Ingredient> ingredients) {
        final String[] strings = new String[4];

        for (int i = 2; i < 6; i++) {
            strings[i-2] = IIngredient.fromIngredient(ingredients.get(i)).getCommandString();
        }

        return strings;
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super SomniumInfusingRecipe> manager, SomniumInfusingRecipe firstRecipe, U secondRecipe) {
        for (int i = 0; i < firstRecipe.getIngredients().size(); i++) {
            if (IngredientUtil.canConflict(firstRecipe.getIngredients().get(i), secondRecipe.getIngredients().get(i))) {
                if (i == firstRecipe.getIngredients().size() - 1) {
                    return true;
                }
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super SomniumInfusingRecipe> manager, SomniumInfusingRecipe recipe) {
        final List<IIngredient> ingredients = recipe.getIngredients().stream().map(IIngredient::fromIngredient).toList();
        final IDecomposedRecipe decomposedRecipe = IDecomposedRecipe.builder()
                .with(BuiltinRecipeComponents.Input.INGREDIENTS, ingredients)
                .with(BuiltinRecipeComponents.Processing.TIME, recipe.getProcessDuration())
                .with(BuiltinRecipeComponents.Output.ITEMS, IItemStack.of(recipe.getResultItem()))
                .build();

        return Optional.of(decomposedRecipe);
    }

    @Override
    public Optional<SomniumInfusingRecipe> recompose(IRecipeManager<? super SomniumInfusingRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        final NonNullList<Ingredient> ingredients = recipe.getOrThrow(BuiltinRecipeComponents.Input.INGREDIENTS).stream().map(IIngredient::asVanillaIngredient).collect(NonNullList::create, NonNullList::add, NonNullList::addAll);
        final int time = recipe.getOrThrowSingle(BuiltinRecipeComponents.Processing.TIME);
        final ItemStack result = recipe.getOrThrowSingle(BuiltinRecipeComponents.Output.ITEMS).getInternal();
        return Optional.of(new SomniumInfusingRecipe(name, result, ingredients, time));
    }
}
