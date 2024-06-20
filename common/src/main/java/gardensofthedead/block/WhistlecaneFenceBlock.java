package gardensofthedead.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WhistlecaneFenceBlock extends CrossCollisionBlock {

    private final VoxelShape[] occlusionByIndex;

    public WhistlecaneFenceBlock(BlockBehaviour.Properties properties) {
        super(3, 2, 16, 16, 24, properties);
        this.registerDefaultState(stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(WATERLOGGED, false)
        );
        occlusionByIndex = makeShapes(3, 1, 16, 6, 15);
    }

    @Override
    protected MapCodec<? extends CrossCollisionBlock> codec() {
        return MapCodec.unit(this);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return this.occlusionByIndex[this.getAABBIndex(state)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getShape(state, level, pos, context);
    }

    public boolean connectsTo(BlockState state, boolean isFaceSturdy, Direction direction) {
        Block block = state.getBlock();
        boolean isSame = isSameFence(state);
        boolean isFenceGate = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction);
        return !isExceptionForConnection(state) && isFaceSturdy || isSame || isFenceGate;
    }

    private boolean isSameFence(BlockState state) {
        return state.is(BlockTags.FENCES) && state.is(BlockTags.WOODEN_FENCES) == defaultBlockState().is(BlockTags.WOODEN_FENCES);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(hand).is(Items.LEAD)) {
            return ItemInteractionResult.FAIL;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, blockHitResult);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockPos northPos = pos.north();
        BlockPos eastPos = pos.east();
        BlockPos southPos = pos.south();
        BlockPos westPos = pos.west();
        BlockState northState = level.getBlockState(northPos);
        BlockState eastState = level.getBlockState(eastPos);
        BlockState southState = level.getBlockState(southPos);
        BlockState westState = level.getBlockState(westPos);
        // noinspection ConstantConditions
        return super.getStateForPlacement(context)
                .setValue(NORTH, connectsTo(northState, northState.isFaceSturdy(level, northPos, Direction.SOUTH), Direction.SOUTH))
                .setValue(EAST, connectsTo(eastState, eastState.isFaceSturdy(level, eastPos, Direction.WEST), Direction.WEST))
                .setValue(SOUTH, connectsTo(southState, southState.isFaceSturdy(level, southPos, Direction.NORTH), Direction.NORTH))
                .setValue(WEST, connectsTo(westState, westState.isFaceSturdy(level, westPos, Direction.EAST), Direction.EAST))
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos updatedPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
            return state.setValue(PROPERTY_BY_DIRECTION.get(direction), connectsTo(newState, newState.isFaceSturdy(level, updatedPos, direction.getOpposite()), direction.getOpposite()));
        }
        return super.updateShape(state, direction, newState, level, pos, updatedPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
}
