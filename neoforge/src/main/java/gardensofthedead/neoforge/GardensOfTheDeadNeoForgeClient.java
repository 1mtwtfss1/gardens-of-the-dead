package gardensofthedead.neoforge;

import gardensofthedead.GardensOfTheDeadClient;
import gardensofthedead.client.particle.SoulblightSporeProvider;
import gardensofthedead.client.particle.WhistlecaneSmokeParticle;
import gardensofthedead.registry.ModBlockEntityTypes;
import gardensofthedead.registry.ModParticleTypes;
import gardensofthedead.registry.ModWoodTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class GardensOfTheDeadNeoForgeClient {

    public GardensOfTheDeadNeoForgeClient(IEventBus modBus) {
        GardensOfTheDeadClient.init();

        modBus.addListener(this::onClientSetup);
        modBus.addListener(this::onRegisterRenderers);
        modBus.addListener(this::onRegisterParticleProviders);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(ModBlockEntityTypes.SIGN.get(), SignRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.HANGING_SIGN.get(), HangingSignRenderer::new);
    }

    private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModWoodTypes.register();
    }

    private void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.SOULBLIGHT_SPORE.get(), SoulblightSporeProvider::new);
        event.registerSpriteSet(ModParticleTypes.WHISTLECANE_SMOKE.get(), WhistlecaneSmokeParticle.Provider::new);
    }
}
