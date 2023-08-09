package dev.dubhe.curtain.features.player.patches;

import com.mojang.authlib.GameProfile;
import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.utils.Messenger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class EntityPlayerMPFake extends ServerPlayer {
    public boolean isAShadow;
    public Runnable fixStartingPosition = () -> {
    };


    public static EntityPlayerMPFake createFakePlayer(String username, MinecraftServer server, double x, double y, double z, double yaw, double pitch, ResourceKey<Level> dimensionId, GameType gamemode, boolean isflying) {
        ServerLevel worldIn = server.getLevel(dimensionId);
        GameProfileCache.setUsesAuthentication(false);
        @Nullable
        GameProfile gameProfile;
        try {
            gameProfile = server.getProfileCache().get(username).orElse(null);
        } finally {
            GameProfileCache.setUsesAuthentication(server.isDedicatedServer() && server.usesAuthentication());
        }
        try {
            if (gameProfile == null) {
                if (!CurtainRules.allowSpawningOfflinePlayers) {
                    return null;
                } else {
                    gameProfile = new GameProfile(UUIDUtil.createOfflinePlayerUUID(username), username);
                }
            }
            if (gameProfile.getProperties().containsKey("textures")) {
                AtomicReference<GameProfile> result = new AtomicReference<>();
                SkullBlockEntity.updateGameprofile(gameProfile, result::set);
                gameProfile = result.get();
            }
            EntityPlayerMPFake instance = new EntityPlayerMPFake(server, worldIn, gameProfile, false);
            instance.fixStartingPosition = () -> instance.moveTo(x, y, z, (float) yaw, (float) pitch);
            server.getPlayerList().placeNewPlayer(new FakeClientConnection(PacketFlow.SERVERBOUND), instance);
            instance.teleportTo(worldIn, x, y, z, (float) yaw, (float) pitch);
            instance.setHealth(20.0f);
            instance.unsetRemoved();
            instance.setMaxUpStep(0.6F);//I dont know why set this
            instance.gameMode.changeGameModeForPlayer(gamemode);
            server.getPlayerList().broadcastAll(new ClientboundRotateHeadPacket(instance, (byte) (instance.yHeadRot * 256 / 360)), dimensionId);
            server.getPlayerList().broadcastAll(new ClientboundTeleportEntityPacket(instance), dimensionId);

            instance.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0x7f);
            instance.getAbilities().flying = isflying;
            return instance;
        } catch (Exception exception) {
            Messenger.print_server_message(server, exception.getMessage());
            return null;
        }
    }

    public static EntityPlayerMPFake createShadow(MinecraftServer server, ServerPlayer player) {
        player.getServer().getPlayerList().remove(player);
        player.connection.disconnect(Component.translatable("multiplayer.disconnect.duplicate_login"));
        ServerLevel worldIn = (ServerLevel) player.level();
        GameProfile gameprofile = player.getGameProfile();
        EntityPlayerMPFake playerShadow = new EntityPlayerMPFake(server, worldIn, gameprofile, true);
        //playerShadow.setChatSession(player.getChatSession());
        server.getPlayerList().placeNewPlayer(new FakeClientConnection(PacketFlow.SERVERBOUND), playerShadow);

        playerShadow.setHealth(player.getHealth());
        playerShadow.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        playerShadow.gameMode.changeGameModeForPlayer(player.gameMode.getGameModeForPlayer());
        ((IServerPlayer) playerShadow).getActionPack().copyFrom(((IServerPlayer) player).getActionPack());
        playerShadow.setMaxUpStep(0.6F);
        playerShadow.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, player.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));


        server.getPlayerList().broadcastAll(new ClientboundRotateHeadPacket(playerShadow, (byte) (player.yHeadRot * 256 / 360)), playerShadow.level().dimension());
        server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, playerShadow));
        //player.world.getChunkManager().updatePosition(playerShadow);
        playerShadow.getAbilities().flying = player.getAbilities().flying;
        return playerShadow;
    }

    private EntityPlayerMPFake(MinecraftServer minecraftServer, ServerLevel level, GameProfile gameProfile, boolean isShadow) {
        super(minecraftServer, level, gameProfile);
        isAShadow = isShadow;
    }

    @Override
    public void onEquipItem(EquipmentSlot p_238393_, ItemStack p_238394_, ItemStack p_238395_) {
        if (!isUsingItem())
            super.onEquipItem(p_238393_, p_238394_, p_238395_);
    }

    @Override
    public void kill() {
        kill(Messenger.s("Killed"));
    }

    public void kill(Component reason) {
        shakeOff();
        this.server.tell(new TickTask(this.server.getTickCount(), () -> {
            this.connection.onDisconnect(reason);
        }));
    }

    @Override
    public void tick() {
        if (this.getServer().getTickCount() % 10 == 0) {
            this.connection.resetPosition();
            ((ServerLevel) this.level()).getChunkSource().move(this);
        }
        try {
            super.tick();
            this.doTick();
        } catch (NullPointerException exception) {
            //
        }

    }

    private void shakeOff() {
        if (getVehicle() instanceof Player) stopRiding();
        for (Entity passenger : getIndirectPassengers()) {
            if (passenger instanceof Player) passenger.stopRiding();
        }
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        shakeOff();
        super.die(damageSource);
        setHealth(20);
        this.foodData = new FoodData();
        kill(this.getCombatTracker().getDeathMessage());
    }

    @Override
    public String getIpAddress() {
        return "127.0.0.1";
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        doCheckFallDamage(this.getX(), y, this.getZ(), onGround);
    }

    @Override
    public Entity changeDimension(@NotNull ServerLevel level) {
        super.changeDimension(level);
        if (wonGame) {
            ServerboundClientCommandPacket packet = new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN);
            connection.handleClientCommand(packet);
        }

        if (connection.player.isChangingDimension()) {
            connection.player.hasChangedDimension();
        }
        return connection.player;
    }
}
