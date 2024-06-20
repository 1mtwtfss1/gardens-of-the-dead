package gardensofthedead.neoforge.datagen;

import gardensofthedead.neoforge.datagen.registry.ModBlockFamilies;
import gardensofthedead.registry.ModBlocks;
import gardensofthedead.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        generateForEnabledBlockFamilies(output, FeatureFlags.VANILLA_SET);
        addCraftingRecipes(output);
    }

    @Override
    protected void generateForEnabledBlockFamilies(RecipeOutput consumer, FeatureFlagSet enabledFeatures) {
        ModBlockFamilies.getAllFamilies()
                .filter(BlockFamily::shouldGenerateRecipe)
                .forEach(family -> generateRecipes(consumer, family, enabledFeatures));
    }

    private void addCraftingRecipes(RecipeOutput consumer) {
        woodFromLogs(consumer, ModBlocks.SOULBLIGHT_HYPHAE.get(), ModBlocks.SOULBLIGHT_STEM.get());
        woodFromLogs(consumer, ModBlocks.STRIPPED_SOULBLIGHT_HYPHAE.get(), ModBlocks.STRIPPED_SOULBLIGHT_STEM.get());

        planksFromLogs(consumer, ModBlocks.SOULBLIGHT_PLANKS.get(), ModTags.Items.SOULBLIGHT_STEMS, 4);
        planksFromLogs(consumer, ModBlocks.WHISTLECANE_PLANKS.get(), ModTags.Items.WHISTLECANE_BLOCKS, 2);

        whistlecaneBlock(consumer);
        blistercrown(consumer);

        mosaicBuilder(consumer, RecipeCategory.DECORATIONS, ModBlocks.WHISTLECANE_MOSAIC.get(), ModBlocks.WHISTLECANE_SLAB.get());
        hangingSign(consumer, ModBlocks.SOULBLIGHT_HANGING_SIGN.get(), ModBlocks.STRIPPED_SOULBLIGHT_STEM.get());
        hangingSign(consumer, ModBlocks.WHISTLECANE_HANGING_SIGN.get(), ModBlocks.WHISTLECANE_BLOCK.get());
    }

    protected void whistlecaneBlock(RecipeOutput output) {
        Block whistlecane = ModBlocks.WHISTLECANE.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WHISTLECANE_BLOCK.get())
                .define('#', whistlecane)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_whistlecane", has(whistlecane))
                .save(output);
    }

    protected static void blistercrown(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.BLISTERCROWN.get(), 3)
                .requires(ModBlocks.TALL_BLISTERCROWN.get())
                .unlockedBy("has_blistercrown", has(ModBlocks.TALL_BLISTERCROWN.get()))
                .save(output);
    }
}
