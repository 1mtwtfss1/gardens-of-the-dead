package gardensofthedead.neoforge.datagen;

import gardensofthedead.GardensOfTheDead;
import gardensofthedead.block.StandingSignBlock;
import gardensofthedead.block.WallHangingSignBlock;
import gardensofthedead.block.WallSignBlock;
import gardensofthedead.registry.ModBlocks;
import gardensofthedead.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BlockTagsProvider extends net.neoforged.neoforge.common.data.BlockTagsProvider {

    public BlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, GardensOfTheDead.MOD_ID, existingFileHelper);
    }

    protected static final List<Block> NON_FLAMMABLE_WOOD_ITEMS = List.of(
            ModBlocks.SOULBLIGHT_PLANKS.get(),
            ModBlocks.SOULBLIGHT_SLAB.get(),
            ModBlocks.SOULBLIGHT_STAIRS.get(),
            ModBlocks.SOULBLIGHT_FENCE.get(),
            ModBlocks.SOULBLIGHT_FENCE_GATE.get(),
            ModBlocks.SOULBLIGHT_BUTTON.get(),
            ModBlocks.SOULBLIGHT_PRESSURE_PLATE.get(),
            ModBlocks.SOULBLIGHT_DOOR.get(),
            ModBlocks.SOULBLIGHT_TRAPDOOR.get(),
            ModBlocks.SOULBLIGHT_SIGN.get(),
            ModBlocks.SOULBLIGHT_HANGING_SIGN.get(),
            ModBlocks.WHISTLECANE_PLANKS.get(),
            ModBlocks.WHISTLECANE_SLAB.get(),
            ModBlocks.WHISTLECANE_STAIRS.get(),
            ModBlocks.WHISTLECANE_FENCE.get(),
            ModBlocks.WHISTLECANE_FENCE_GATE.get(),
            ModBlocks.WHISTLECANE_BUTTON.get(),
            ModBlocks.WHISTLECANE_PRESSURE_PLATE.get(),
            ModBlocks.WHISTLECANE_DOOR.get(),
            ModBlocks.WHISTLECANE_TRAPDOOR.get(),
            ModBlocks.WHISTLECANE_SIGN.get(),
            ModBlocks.WHISTLECANE_HANGING_SIGN.get(),
            ModBlocks.WHISTLECANE_MOSAIC.get(),
            ModBlocks.WHISTLECANE_MOSAIC_SLAB.get(),
            ModBlocks.WHISTLECANE_MOSAIC_STAIRS.get()
    );

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getNamespace().equals(GardensOfTheDead.MOD_ID))
                .forEach(block -> {
                    switch (block) {
                        case FlowerPotBlock flowerPotBlock ->
                                tag(BlockTags.FLOWER_POTS).add(flowerPotBlock);
                        case StandingSignBlock standingSignBlock ->
                                tag(BlockTags.STANDING_SIGNS).add(standingSignBlock);
                        case WallSignBlock wallSignBlock ->
                                tag(BlockTags.WALL_SIGNS).add(wallSignBlock);
                        case CeilingHangingSignBlock ceilingHangingSignBlock ->
                                tag(BlockTags.CEILING_HANGING_SIGNS).add(ceilingHangingSignBlock);
                        case WallHangingSignBlock wallHangingSignBlock ->
                                tag(BlockTags.WALL_HANGING_SIGNS).add(wallHangingSignBlock);
                        case FenceGateBlock fenceGateBlock ->
                                tag(BlockTags.FENCE_GATES).add(fenceGateBlock);
                        default -> { }
                    }
                });

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(ModTags.Blocks.SOULBLIGHT_STEMS)
                .addTag(ModTags.Blocks.WHISTLECANE_BLOCKS)
                .add(NON_FLAMMABLE_WOOD_ITEMS.toArray(new Block[]{}));

        tag(BlockTags.MINEABLE_WITH_HOE).add(
                ModBlocks.BLIGHTWART_BLOCK.get()
        );

        tag(BlockTags.ENDERMAN_HOLDABLE).add(
                ModBlocks.SOULBLIGHT_FUNGUS.get(),
                ModBlocks.SOULBLIGHT_SPROUTS.get(),
                ModBlocks.BLISTERCROWN.get()
        );

        tag(ModTags.Blocks.SOULBLIGHT_STEMS).add(
                ModBlocks.SOULBLIGHT_STEM.get(),
                ModBlocks.STRIPPED_SOULBLIGHT_STEM.get(),
                ModBlocks.SOULBLIGHT_HYPHAE.get(),
                ModBlocks.STRIPPED_SOULBLIGHT_HYPHAE.get()
        );

        tag(ModTags.Blocks.WHISTLECANE_BLOCKS).add(
                ModBlocks.WHISTLECANE_BLOCK.get()
        );

        tag(BlockTags.LOGS).addTag(
                ModTags.Blocks.SOULBLIGHT_STEMS
        );

        tag(BlockTags.WART_BLOCKS).add(
                ModBlocks.BLIGHTWART_BLOCK.get()
        );

        tag(BlockTags.WOODEN_FENCES).add(
                ModBlocks.SOULBLIGHT_FENCE.get(),
                ModBlocks.WHISTLECANE_FENCE.get()
        );

        tag(BlockTags.WOODEN_BUTTONS).add(
                ModBlocks.SOULBLIGHT_BUTTON.get(),
                ModBlocks.WHISTLECANE_BUTTON.get()
        );

        tag(BlockTags.PLANKS).add(
                ModBlocks.SOULBLIGHT_PLANKS.get(),
                ModBlocks.WHISTLECANE_PLANKS.get()
        );

        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(
                ModBlocks.SOULBLIGHT_PRESSURE_PLATE.get(),
                ModBlocks.WHISTLECANE_PRESSURE_PLATE.get()
        );

        tag(BlockTags.WOODEN_SLABS).add(
                ModBlocks.SOULBLIGHT_SLAB.get(),
                ModBlocks.WHISTLECANE_SLAB.get()
        );

        tag(BlockTags.WOODEN_DOORS).add(
                ModBlocks.SOULBLIGHT_DOOR.get(),
                ModBlocks.WHISTLECANE_DOOR.get()
        );

        tag(BlockTags.WOODEN_TRAPDOORS).add(
                ModBlocks.SOULBLIGHT_TRAPDOOR.get(),
                ModBlocks.WHISTLECANE_TRAPDOOR.get()
        );

        tag(BlockTags.WOODEN_STAIRS).add(
                ModBlocks.SOULBLIGHT_STAIRS.get(),
                ModBlocks.WHISTLECANE_STAIRS.get()
        );
    }
}
