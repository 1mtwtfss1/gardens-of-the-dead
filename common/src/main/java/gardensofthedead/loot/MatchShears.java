package gardensofthedead.loot;

import com.mojang.serialization.MapCodec;
import gardensofthedead.platform.PlatformServices;
import gardensofthedead.registry.ModLootConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class MatchShears implements LootItemCondition {

    public static final MapCodec<MatchShears> CODEC = MapCodec.unit(new MatchShears());

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.MATCH_SHEARS.get();
    }

    public static Builder matchShears() {
        return MatchShears::new;
    }

    @Override
    public boolean test(LootContext context) {
        if (context.hasParam(LootContextParams.TOOL)) {
            return test(context.getParam(LootContextParams.TOOL));
        }
        return false;
    }

    public static boolean test(ItemStack tool) {
        return PlatformServices.platformHelper.isShears(tool);
    }
}
