package gardensofthedead.neoforge;

import gardensofthedead.GardensOfTheDead;
import gardensofthedead.neoforge.datagen.*;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = GardensOfTheDead.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class GardensOfTheDeadData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        BlockTagsProvider blockTagsProvider = new BlockTagsProvider(packOutput, lookupProvider, existingFileHelper);

        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeClient(), new BlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new ItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new RecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeClient(), new SoundDefinitionsProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new DataMapProvider(packOutput, lookupProvider));

        RegistrySetBuilder levelProvider = createLevelProvider();
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(generator.getPackOutput(), event.getLookupProvider(), levelProvider, Set.of(GardensOfTheDead.MOD_ID)));
        lookupProvider = lookupProvider.thenApply(r -> constructRegistries(r, levelProvider));

        generator.addProvider(event.includeServer(), new BiomeTagsProvider(packOutput, lookupProvider, existingFileHelper));
    }

    @SuppressWarnings("UnstableApiUsage")
    private static HolderLookup.Provider constructRegistries(HolderLookup.Provider original, RegistrySetBuilder datapackEntriesBuilder) {
        var builderKeys = new HashSet<>(datapackEntriesBuilder.getEntryKeys());
        DataPackRegistriesHooks.getDataPackRegistriesWithDimensions()
                .filter(data -> !builderKeys.contains(data.key()))
                .forEach(data -> datapackEntriesBuilder.add(data.key(), context -> {}));
        Cloner.Factory factory = new Cloner.Factory();
        DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(data -> data.runWithArguments(factory::addCodec));
        return datapackEntriesBuilder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original, factory).full();
    }


    public static RegistrySetBuilder createLevelProvider() {
        RegistrySetBuilder builder = new RegistrySetBuilder();
        builder.add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureProvider::create);
        builder.add(Registries.PLACED_FEATURE, PlacedFeatureProvider::create);
        builder.add(Registries.BIOME, BiomeProvider::create);
        return builder;
    }
}
