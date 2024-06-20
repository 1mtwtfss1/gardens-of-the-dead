package gardensofthedead.neoforge.datagen;

import gardensofthedead.GardensOfTheDead;
import gardensofthedead.block.WallHangingSignBlock;
import gardensofthedead.block.WallSignBlock;
import gardensofthedead.loot.MatchShears;
import gardensofthedead.registry.ModBlocks;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class LootTableProvider extends net.minecraft.data.loot.LootTableProvider {

    private final List<SubProviderEntry> lootTables = new ArrayList<>();

    private final Set<Block> blocksWithLootAdded = new HashSet<>();

    public LootTableProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, Set.of(), List.of(), provider);
    }

    @Override
    public List<SubProviderEntry> getTables() {
        lootTables.clear();
        blocksWithLootAdded.clear();

        BuiltInRegistries.BLOCK.keySet()
                .stream()
                .filter(k -> k.getNamespace().equals(GardensOfTheDead.MOD_ID))
                .map(BuiltInRegistries.BLOCK::get)
                .filter(block -> block instanceof WallSignBlock || block instanceof WallHangingSignBlock)
                .forEach(this::noLoot);

        addShearHarvestables(
                ModBlocks.SOUL_SPORE.get(),
                ModBlocks.GLOWING_SOUL_SPORE.get(),
                ModBlocks.SOULBLIGHT_SPROUTS.get(),
                ModBlocks.BLISTERCROWN.get(),
                ModBlocks.TALL_BLISTERCROWN.get()
        );

        addDefaultDrops(ModBlocks.WHISTLECANE.get());

        for (Block block : BuiltInRegistries.BLOCK.stream().toList()) {
            // noinspection ConstantConditions
            if (!blocksWithLootAdded.contains(block) && BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(GardensOfTheDead.MOD_ID)) {
                switch (block) {
                    case FlowerPotBlock pottedPlant -> addPottedPlants(pottedPlant);
                    case DoorBlock doorBlock -> addDoor(doorBlock);
                    case SlabBlock slabBlock -> addSlab(slabBlock);
                    default -> addDefaultDrops(block);
                }
            }
        }

        return lootTables;
    }

    private void addShearHarvestables(Block... blocks) {
        for (Block block : blocks) {
            addBlockLootTable(block, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(block)
                                    .when(MatchShears.matchShears())
                            )
                    )
            );
        }
    }

    private void addPottedPlants(FlowerPotBlock... blocks) {
        for (FlowerPotBlock block : blocks) {
            Block emptyPot = block.getEmptyPot();
            Block content = block.getPotted();

            addBlockLootTable(block, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(emptyPot))
                            .when(ExplosionCondition.survivesExplosion())
                    ).withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(content))
                            .when(ExplosionCondition.survivesExplosion())
                    )
            );
        }
    }

    private void addDoor(DoorBlock block) {
        addBlockLootTable(block, LootTable
                .lootTable()
                .withPool(defaultDrops(block)
                        .when(LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder
                                        .properties()
                                        .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                                )
                        )
                )
        );
    }

    private void addSlab(SlabBlock block) {
        addBlockLootTable(block, LootTable
                .lootTable()
                .withPool(defaultDrops(block)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)
                                        )
                                )
                        )
                )
        );
    }

    private void addDefaultDrops(Block block) {
        addDefaultDrops(block, block);
    }

    private void addDefaultDrops(Block block, ItemLike loot) {
        addBlockLootTable(block, LootTable.lootTable().withPool(defaultDrops(loot)));
    }

    private LootPool.Builder defaultDrops(ItemLike itemProvider) {
        return LootPool.lootPool()
                .when(ExplosionCondition.survivesExplosion())
                .add(LootItem.lootTableItem(itemProvider));
    }

    private void addBlockLootTable(Block block, LootTable.Builder lootTable) {
        blocksWithLootAdded.add(block);
        lootTables.add(new SubProviderEntry(provider -> lootBuilder -> lootBuilder.accept(block.getLootTable(), lootTable), LootContextParamSets.BLOCK));
    }

    private void noLoot(Block... blocks) {
        blocksWithLootAdded.addAll(Set.of(blocks));
    }
}
