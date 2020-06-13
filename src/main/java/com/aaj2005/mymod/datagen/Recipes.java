package com.aaj2005.mymod.datagen;

import com.aaj2005.mymod.blocks.ModBlocks;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn){
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer){
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.POWERGEN)
        .patternLine("TTT")
        .patternLine("cMc")
        .patternLine("TTT")
        .key('c', Items.REDSTONE)
        .key('T', Items.IRON_INGOT)
        .key('M', ModBlocks.BLOCKFRAME)
        .setGroup("mymod")
        .addCriterion("blockframe", InventoryChangeTrigger.Instance.forItems(ModBlocks.BLOCKFRAME))
        .build(consumer);
    }
}
