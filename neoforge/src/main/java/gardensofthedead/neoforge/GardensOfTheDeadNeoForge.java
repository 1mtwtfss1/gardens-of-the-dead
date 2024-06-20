package gardensofthedead.neoforge;

import gardensofthedead.GardensOfTheDead;
import gardensofthedead.neoforge.region.GardensOfTheDeadNeoForgeRegion;
import gardensofthedead.registry.*;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

@Mod(GardensOfTheDead.MOD_ID)
public class GardensOfTheDeadNeoForge {

    public GardensOfTheDeadNeoForge(IEventBus modBus) {
        GardensOfTheDead.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new GardensOfTheDeadNeoForgeClient(modBus);
        }

        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Regions.register(new GardensOfTheDeadNeoForgeRegion());
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, GardensOfTheDead.MOD_ID, ModSurfaceRules.makeRules());
            ModItems.addCompostables((k, v) -> ComposterBlock.COMPOSTABLES.put(k, (float) v));
        });
    }
}
