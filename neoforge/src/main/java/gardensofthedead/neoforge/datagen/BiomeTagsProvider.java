package gardensofthedead.neoforge.datagen;

import gardensofthedead.GardensOfTheDead;
import gardensofthedead.registry.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BiomeTagsProvider extends net.minecraft.data.tags.BiomeTagsProvider {

    public BiomeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, GardensOfTheDead.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (ResourceKey<Biome> biome : List.of(
                ModBiomes.SOULBLIGHT_FOREST,
                ModBiomes.WHISTLING_WOODS
        )) {
            // used by has_nether_fortress, has_ruined_portal_nether
            tag(BiomeTags.IS_NETHER).add(biome);
            tag(BiomeTags.SNOW_GOLEM_MELTS);
            tag(Tags.Biomes.IS_DRY_NETHER).add(biome);
            tag(Tags.Biomes.IS_HOT_NETHER).add(biome);
        }

        tag(BiomeTags.HAS_BASTION_REMNANT).add(
                ModBiomes.SOULBLIGHT_FOREST,
                ModBiomes.WHISTLING_WOODS
        );

        tag(BiomeTags.HAS_NETHER_FOSSIL).add(
                ModBiomes.SOULBLIGHT_FOREST
        );

        tag(Tags.Biomes.IS_DENSE_VEGETATION_NETHER).add(
                ModBiomes.SOULBLIGHT_FOREST,
                ModBiomes.WHISTLING_WOODS
        );

        tag(Tags.Biomes.IS_NETHER_FOREST).add(
                ModBiomes.SOULBLIGHT_FOREST,
                ModBiomes.WHISTLING_WOODS
        );
    }
}
