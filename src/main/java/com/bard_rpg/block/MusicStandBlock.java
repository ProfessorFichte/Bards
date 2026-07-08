package com.bard_rpg.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.EnumMap;
import java.util.Map;

public class MusicStandBlock extends HorizontalFacingBlock {

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.NORTH, VoxelShapes.union(
            Block.createCuboidShape(6,   0,  5,   10,   12,  9),
            Block.createCuboidShape(1.5, 11, 4,   14.5, 13, 10)
        ));
        SHAPES.put(Direction.EAST, VoxelShapes.union(
            Block.createCuboidShape(7,   0,  6,   11,   12, 10),
            Block.createCuboidShape(6,   11, 1.5, 12,   13, 14.5)
        ));
        SHAPES.put(Direction.SOUTH, VoxelShapes.union(
            Block.createCuboidShape(6,   0,  7,   10,   12, 11),
            Block.createCuboidShape(1.5, 11, 6,   14.5, 13, 12)
        ));
        SHAPES.put(Direction.WEST, VoxelShapes.union(
            Block.createCuboidShape(5,   0,  6,   9,    12, 10),
            Block.createCuboidShape(4,   11, 1.5, 10,   13, 14.5)
        ));
    }

    public MusicStandBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
