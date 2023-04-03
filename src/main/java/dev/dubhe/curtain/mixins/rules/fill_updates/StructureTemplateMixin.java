package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin {
    @Redirect(
            method = "placeInWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ServerLevelAccessor;blockUpdated(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;)V"
            )
    )
    private void skipUpdateNeighbours(ServerLevelAccessor serverWorldAccessor, BlockPos blockPos, Block block) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            serverWorldAccessor.blockUpdated(blockPos, block);
        }
    }

    @Redirect(
            method = "placeInWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;getKnownShape()Z"
            )
    )
    private boolean skipPostprocess(StructurePlaceSettings structurePlacementData) {
        return structurePlacementData.getKnownShape() || CurtainRules.impendingFillSkipUpdates.get();
    }
}
