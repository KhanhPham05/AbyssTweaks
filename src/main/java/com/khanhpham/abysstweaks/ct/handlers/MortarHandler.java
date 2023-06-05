package com.khanhpham.abysstweaks.ct.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.IngredientUtil;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.khanhpham.abysstweaks.ModUtils;
import com.khanhpham.abysstweaks.TriHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.yezon.theabyss.recipes.impl.MortarAndPestleRecipe;

import java.util.Optional;

@SuppressWarnings("unused")
@IRecipeHandler.For(MortarAndPestleRecipe.class)
public class MortarHandler implements IRecipeHandler<MortarAndPestleRecipe> {
    @Override
    public String dumpToCommandString(IRecipeManager<? super MortarAndPestleRecipe> manager, MortarAndPestleRecipe recipe) {
        TriHolder<String, String, String> holder = recipeToString(recipe.getIngredients());
        return String.format("%s.%s(%s, %s, %s, %s);",
                ModUtils.MAP,
                holder.first(),
                StringUtil.quoteAndEscape(recipe.getId()),
                IItemStack.of(recipe.getResultItem()).getCommandString(),
                holder.third(),
                holder.second());
    }

    private TriHolder<String, String, String> recipeToString(NonNullList<Ingredient> ingredients) {
        String[] strings = ingredients.stream().map(IIngredient::fromIngredient).map(IIngredient::getCommandString).toArray(String[]::new);
        String bottle = strings[2];
        String ingredient0 = strings[0];

        for (int i = 1; i < strings.length; i++) {
            if (i != 2)
                if (strings[i].equals(ingredient0)) {
                    if (i == strings.length - 1) {
                        return TriHolder.of("addSingleIngredientRecipe", ingredient0, bottle);
                    }
                } else break;
        }

        String ingredient1 = strings[1];
        if (ingredient0.equals(strings[4]) && ingredient1.equals(strings[3]))
            return TriHolder.of("addTwoIngredientsRecipe", ModUtils.toArrayString(new String[]{ingredient0, ingredient1}, false), bottle);

        if (ingredient0.equals(strings[3]) && ingredient1.equals(strings[4])) {
            return TriHolder.of("addSidedIngredientsRecipe", ModUtils.toArrayString(new String[]{ingredient0, ingredient1}, false), bottle);
        }

        return TriHolder.of("addRecipe", ModUtils.toArrayString(new String[]{strings[0], strings[1], strings[3], strings[4]}, false), bottle);
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super MortarAndPestleRecipe> manager, MortarAndPestleRecipe firstRecipe, U secondRecipe) {

        for (int i = 0; i < firstRecipe.getIngredients().size(); i++) {
            if (IngredientUtil.canConflict(firstRecipe.getIngredients().get(i), secondRecipe.getIngredients().get(i))) {
                if (i == firstRecipe.getIngredients().size() - 1) {
                    return true;
                }
            } else break;
        }

        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super MortarAndPestleRecipe> manager, MortarAndPestleRecipe recipe) {
        return Optional.of(IDecomposedRecipe.builder().with(BuiltinRecipeComponents.Output.ITEMS, IItemStack.of(recipe.getResultItem())).with(BuiltinRecipeComponents.Input.INGREDIENTS, recipe.getIngredients().stream().map(IIngredient::fromIngredient).toList()).build());
    }

    @Override
    public Optional<MortarAndPestleRecipe> recompose(IRecipeManager<? super MortarAndPestleRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        return Optional.of(new MortarAndPestleRecipe(name, recipe.getOrThrowSingle(BuiltinRecipeComponents.Output.ITEMS).getInternal(), recipe.getOrThrow(BuiltinRecipeComponents.Input.INGREDIENTS).stream().map(IIngredient::asVanillaIngredient).collect(NonNullList::create, NonNullList::add, NonNullList::addAll)));
    }
}