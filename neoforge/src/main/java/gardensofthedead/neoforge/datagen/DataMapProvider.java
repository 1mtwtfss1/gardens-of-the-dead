package gardensofthedead.neoforge.datagen;

import gardensofthedead.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class DataMapProvider extends net.neoforged.neoforge.common.data.DataMapProvider {

    public DataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        ModItems.addCompostables((item, p) -> builder(NeoForgeDataMaps.COMPOSTABLES).add(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), new Compostable(p), false));
    }
}
