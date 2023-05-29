package com.khanhpham.abysstweaks.ct.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.IngredientUtil;
import com.blamejared.crafttweaker.api.util.ItemStackUtil;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.khanhpham.abysstweaks.ModUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.yezon.theabyss.recipes.impl.ArcaneStationRecipe;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@IRecipeHandler.For(ArcaneStationRecipe.class)
public class ArcaneRecipeHandler implements IRecipeHandler<ArcaneStationRecipe> {

    @Override
    public String dumpToCommandString(IRecipeManager<? super ArcaneStationRecipe> manager, ArcaneStationRecipe recipe) {
        final var pair = this.getMode(recipe);

        return String.format("%s.%s(%s, %s, %s, %s);",
                ModUtils.ARCANE_STATION,
                pair.getFirst(),
                StringUtil.quoteAndEscape(recipe.getId()),
                ItemStackUtil.getCommandString(recipe.getResultItem()),
                pair.getSecond(),
                IIngredient.fromIngredient(recipe.getCenterIngredient())
        );
    }

    private Pair<String, String> getMode(ArcaneStationRecipe recipe) {

        final List<String> ingredients = recipe.getIngredients().stream().map(IIngredient::fromIngredient).map(IIngredient::getCommandString).toList();

        String ingredient0 = ingredients.get(0);
        for (int i = 1; i < 8; i++) {
            if (ingredients.get(i).equals(ingredient0)) {
                if (i == 7) {
                    return Pair.of("addFullCircleRecipe", ingredient0);
                }
            }
        }

        String ingredient1 = ingredients.get(1);
        for (int i = 2; i < 8; i++) {
            if (Math.floorMod(i, 2) == 0) {
                if (!ingredients.get(i).equals(ingredient0)) {
                    break;
                }
            } else {
                if (ingredient1.equals(ingredients.get(i))) {
                    if (i == 7) {
                        return Pair.of("addTwoIngredientsRecipe", ModUtils.toArrayString(List.of(ingredient0, ingredient1), false));
                    }
                }
            }
        }

        final String ingredient4 = ingredients.get(4);
        for (int i = 1; i < 8; i++) {
            if (i <= 3) {
                if (!ingredient0.equals(ingredients.get(i))) break;
            } else {
                if (ingredient4.equals(ingredients.get(i))) {
                    if (i == 7) {
                        return Pair.of("addHalfHalfRecipe", ModUtils.toArrayString(List.of(ingredient0, ingredient4), false));
                    }
                }
            }
        }

        return Pair.of("addRecipe", ModUtils.toArrayString(ingredients, true));
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super ArcaneStationRecipe> manager, ArcaneStationRecipe firstRecipe, U secondRecipe) {
        for (int i = 0; i < 9; i++) {
            if (IngredientUtil.canConflict(firstRecipe.getIngredients().get(i), secondRecipe.getIngredients().get(i))) {
                if (i == 8) return true;
            } else break;
        }

        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super ArcaneStationRecipe> manager, ArcaneStationRecipe recipe) {
        final IIngredient center = IIngredient.fromIngredient(recipe.getCenterIngredient());
        final List<IIngredient> ingredients = recipe.getIngredients().stream().map(IIngredient::fromIngredient).filter(ingredient -> !center.getCommandString().equals(ingredient.getCommandString())).toList();
        return Optional.of(IDecomposedRecipe.builder()
                .with(BuiltinRecipeComponents.Input.INGREDIENTS, ingredients)
                .with(BuiltinRecipeComponents.Input.INGREDIENTS, center)
                .with(BuiltinRecipeComponents.Output.ITEMS, IItemStack.of(recipe.getResultItem()))
                .build());
    }

    @Override
    public Optional<ArcaneStationRecipe> recompose(IRecipeManager<? super ArcaneStationRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        final List<IIngredient> ingredients = recipe.getOrThrow(BuiltinRecipeComponents.Input.INGREDIENTS);
        final IIngredient center = recipe.getOrThrow(BuiltinRecipeComponents.Input.INGREDIENTS).get(0);
        final IItemStack output = recipe.getOrThrowSingle(BuiltinRecipeComponents.Output.ITEMS);

        final NonNullList<Ingredient> recipeIngredients = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            recipeIngredients.set(i, ingredients.get(i).asVanillaIngredient());
        }

        return Optional.of(new ArcaneStationRecipe(name, recipeIngredients, center.asVanillaIngredient(), output.getInternal()));
    }

}
