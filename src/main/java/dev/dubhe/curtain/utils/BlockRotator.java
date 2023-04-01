package dev.dubhe.curtain.utils;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockRotator {
    public static boolean flipBlockWithCactus(BlockState state, Level world, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getAbilities().mayBuild || !CurtainRules.flippinCactus || !playerHoldsCactusMainhand(player)) {
            return false;
        }
        //TODO: unfinished
        return true;
    }

    private static boolean playerHoldsCactusMainhand(Player playerIn)
    {
        return playerIn.getMainHandItem().getItem() == Items.CACTUS;
    }
}
