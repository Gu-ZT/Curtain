package dev.dubhe.curtain.utils;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.fakes.IPistonBlock;
import net.minecraft.block.*;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;


public class BlockRotator {
    public static boolean flipBlockWithCactus(BlockState state, World world, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.abilities.mayBuild || !CurtainRules.flippingCactus || !playerHoldsCactusMainhand(player)) {
            return false;
        }
        CurtainRules.impendingFillSkipUpdates.set(true);
        boolean retval = flipBlock(state, world, player, hand, hit);
        CurtainRules.impendingFillSkipUpdates.set(false);
        return retval;
    }

    private static Direction rotateClockwise(Direction direction, Direction.Axis direction$Axis_1) {
        switch (direction$Axis_1) {
            case X:
                if (direction != Direction.WEST && direction != Direction.EAST) {
                    return rotateXClockwise(direction);
                }

                return direction;
            case Y:
                if (direction != Direction.UP && direction != Direction.DOWN) {
                    return rotateYClockwise(direction);
                }

                return direction;
            case Z:
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    return rotateZClockwise(direction);
                }

                return direction;
            default:
                throw new IllegalStateException("Unable to get CW facing for axis " + direction$Axis_1);
        }
    }

    private static Direction rotateYClockwise(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.EAST;
            case EAST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.WEST;
            case WEST:
                return Direction.NORTH;
            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + dir);
        }
    }

    private static Direction rotateXClockwise(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.DOWN;
            case EAST:
            case WEST:
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of " + dir);
            case SOUTH:
                return Direction.UP;
            case UP:
                return Direction.NORTH;
            case DOWN:
                return Direction.SOUTH;
        }
    }

    private static Direction rotateZClockwise(Direction dir) {
        switch (dir) {
            case EAST:
                return Direction.DOWN;
            case SOUTH:
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of " + dir);
            case WEST:
                return Direction.UP;
            case UP:
                return Direction.EAST;
            case DOWN:
                return Direction.WEST;
        }
    }

    public static ItemStack dispenserRotate(IBlockSource source, ItemStack stack) {
        Direction sourceFace = source.getBlockState().getValue(DispenserBlock.FACING);
        World world = source.getLevel();
        BlockPos blockpos = source.getPos().relative(sourceFace);
        BlockState blockstate = world.getBlockState(blockpos);
        Block block = blockstate.getBlock();

        // Block rotation for blocks that can be placed in all 6 or 4 rotations.
        if (block instanceof DirectionalBlock || block instanceof DispenserBlock) {
            Direction face = blockstate.getValue(DirectionalBlock.FACING);
            if (block instanceof PistonBlock
                    && (blockstate.getValue(PistonBlock.EXTENDED)
                    || (((IPistonBlock) block).publicShouldExtend(world, blockpos, face)
                    && (new PistonBlockStructureHelper(world, blockpos, face, true)).resolve()))) {
                return stack;
            }

            Direction rotatedFace = rotateClockwise(face, sourceFace.getAxis());
            if (sourceFace.get3DDataValue() % 2 == 0 || rotatedFace == face) {
                // Flip to make blocks always rotate clockwise relative to the dispenser
                // when index is equal to zero. when index is equal to zero the dispenser is in the opposite direction.
                rotatedFace = rotatedFace.getOpposite();
            }
            world.setBlock(blockpos, blockstate.setValue(DirectionalBlock.FACING, rotatedFace), 3);
        } else if (block instanceof HorizontalBlock) {
            // Block rotation for blocks that can be placed in only 4 horizontal rotations.
            if (block instanceof BedBlock) {
                return stack;
            }
            Direction face = blockstate.getValue(HorizontalBlock.FACING);
            if (sourceFace == Direction.DOWN) {
                face = face.getOpposite();
            }
            world.setBlock(blockpos, blockstate.setValue(HorizontalBlock.FACING, face), 3);
        } else if (block == Blocks.HOPPER) {
            Direction face = blockstate.getValue(HopperBlock.FACING);
            if (face != Direction.DOWN) {
                face = rotateClockwise(face, Direction.Axis.Y);
                world.setBlock(blockpos, blockstate.setValue(HopperBlock.FACING, face), 3);
            }
        }
        world.neighborChanged(blockpos, block, source.getPos());
        return stack;
    }

    public static boolean flipBlock(BlockState state, World world, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        Block block = state.getBlock();
        BlockPos pos = hit.getBlockPos();
        Vector3d hitVec = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
        Direction facing = hit.getDirection();
        BlockState newState = null;

        if ((block instanceof HorizontalBlock || block instanceof RailBlock) && !(block instanceof BedBlock)) {
            newState = state.rotate(Rotation.CLOCKWISE_90);
        } else if (block instanceof ObserverBlock || block instanceof EndRodBlock) {
            newState = state.setValue(DirectionalBlock.FACING, state.getValue(DirectionalBlock.FACING).getOpposite());
        } else if (block instanceof DispenserBlock) {
            newState = state.setValue(DispenserBlock.FACING, state.getValue(DispenserBlock.FACING).getOpposite());
        } else if (block instanceof PistonBlock) {
            if (!(state.getValue(PistonBlock.EXTENDED))) {
                newState = state.setValue(DirectionalBlock.FACING, state.getValue(DirectionalBlock.FACING).getOpposite());
            }
        } else if (block instanceof SlabBlock) {
            if (((SlabBlock) block).useShapeForLightOcclusion(state)) {
                newState = state.setValue(SlabBlock.TYPE, state.getValue(SlabBlock.TYPE) == SlabType.TOP ? SlabType.BOTTOM : SlabType.TOP);
            }
        } else if (block instanceof HopperBlock) {
            if (state.getValue(HopperBlock.FACING) != Direction.DOWN) {
                newState = state.setValue(HopperBlock.FACING, state.getValue(HopperBlock.FACING).getClockWise());
            }
        } else if (block instanceof StairsBlock) {
            if ((facing == Direction.UP && hitVec.y == 1.0f) || (facing == Direction.DOWN && hitVec.y == 0.0f)) {
                newState = state.setValue(StairsBlock.HALF, state.getValue(StairsBlock.HALF) == Half.TOP ? Half.BOTTOM : Half.TOP);
            } else {
                boolean turnCounterClockwise;
                switch (facing) {
                    case NORTH: {
                        turnCounterClockwise = hitVec.x <= 0.5;
                        break;
                    }
                    case SOUTH: {
                        turnCounterClockwise = !(hitVec.x <= 0.5);
                        break;
                    }
                    case EAST: {
                        turnCounterClockwise = hitVec.z <= 0.5;
                        break;
                    }
                    case WEST: {
                        turnCounterClockwise = !(hitVec.z <= 0.5);
                        break;
                    }
                    default: {
                        turnCounterClockwise = false;
                        break;
                    }
                }
                newState = state.rotate(turnCounterClockwise ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90);
            }
        } else if (block instanceof RotatedPillarBlock) {
            Direction.Axis axis;
            switch (state.getValue(RotatedPillarBlock.AXIS)) {
                case X: {
                    axis = Direction.Axis.Z;
                    break;
                }
                case Y: {
                    axis = Direction.Axis.X;
                    break;
                }
                default: {
                    axis = Direction.Axis.Y;
                    break;
                }
            }
            newState = state.setValue(RotatedPillarBlock.AXIS, axis);
        }
        if (newState != null) {
            world.setBlock(pos, newState, 2 | 1024); // no constant matching 1024 in Block, what does this do?
            world.setBlocksDirty(pos, state, newState);
            return true;
        }
        return false;
    }


    private static boolean playerHoldsCactusMainhand(PlayerEntity playerIn) {
        return playerIn.getMainHandItem().getItem() == Items.CACTUS;
    }

    public static boolean flippinEligibility(Entity entity) {
        return CurtainRules.flippingCactus && entity instanceof PlayerEntity && ((PlayerEntity) entity).getOffhandItem().getItem() == Items.CACTUS;
    }

    public static class CactusDispenserBehaviour extends OptionalDispenseBehavior implements IDispenseItemBehavior {
        @SuppressWarnings("NullableProblems")
        @Override
        protected ItemStack execute(IBlockSource source, ItemStack stack) {
            if (CurtainRules.rotatorBlock) {
                return BlockRotator.dispenserRotate(source, stack);
            } else {
                return super.execute(source, stack);
            }
        }
    }

}
