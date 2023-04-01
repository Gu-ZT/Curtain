package dev.dubhe.curtain.mixins.rule.fill_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StructureBlockEntity.class)
public class StructureBlockEntityMixin {
    @Redirect(
            method = "loadStructure(Lnet/minecraft/server/level/ServerLevel;ZLnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/util/RandomSource;I)Z"
            )
    )
    private boolean onStructurePlace(StructureTemplate structure, ServerLevelAccessor serverWorldAccess, BlockPos pos, BlockPos blockPos, StructurePlaceSettings placementData, RandomSource random, int i) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
        try {
            return structure.placeInWorld(serverWorldAccess, pos, blockPos, placementData, random, i);
        } finally {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }
}
