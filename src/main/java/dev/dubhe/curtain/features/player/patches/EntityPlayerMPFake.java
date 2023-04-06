package dev.dubhe.curtain.features.player.patches;

import com.mojang.authlib.GameProfile;
import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.utils.Messenger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.server.SEntityHeadLookPacket;
import net.minecraft.network.play.server.SEntityTeleportPacket;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EntityPlayerMPFake extends ServerPlayerEntity {
    public Runnable fixStartingPosition = () -> {
    };
    public boolean isAShadow;

    public static EntityPlayerMPFake createFake(String username, MinecraftServer server, double d0, double d1, double d2, double yaw, double pitch, RegistryKey<World> dimensionId, GameType gamemode, boolean flying) {
        //prolly half of that crap is not necessary, but it works
        ServerWorld worldIn = server.getLevel(dimensionId);
        PlayerInteractionManager interactionManagerIn = new PlayerInteractionManager(worldIn);
        PlayerProfileCache.setUsesAuthentication(false);
        GameProfile gameprofile;
        try {
            gameprofile = server.getProfileCache().get(username); //findByName  .orElse(null)
        } finally {
            PlayerProfileCache.setUsesAuthentication(server.isDedicatedServer() && server.usesAuthentication());
        }
        if (gameprofile == null) {
            if (!CurtainRules.allowSpawningOfflinePlayers) {
                return null;
            } else {
                gameprofile = new GameProfile(PlayerEntity.createPlayerUUID(username), username);
            }
        }
        if (gameprofile.getProperties().containsKey("textures")) {
            gameprofile = SkullTileEntity.updateGameprofile(gameprofile);
        }
        EntityPlayerMPFake instance = new EntityPlayerMPFake(server, worldIn, gameprofile, interactionManagerIn, false);
        instance.fixStartingPosition = () -> instance.moveTo(d0, d1, d2, (float) yaw, (float) pitch);
        server.getPlayerList().placeNewPlayer(new FakeClientConnection(PacketDirection.SERVERBOUND), instance);
        instance.teleportTo(worldIn, d0, d1, d2, (float) yaw, (float) pitch);
        instance.setHealth(20.0F);
        instance.revive();
        instance.maxUpStep = 0.6F;
        instance.gameMode.updateGameMode(gamemode);
        server.getPlayerList().broadcastAll(new SEntityHeadLookPacket(instance, (byte) (instance.yHeadRot * 256 / 360)), dimensionId);//instance.dimension);
        server.getPlayerList().broadcastAll(new SEntityTeleportPacket(instance), dimensionId);//instance.dimension);
        //instance.world.getChunkManager(). updatePosition(instance);
        instance.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0x7f); // show all model layers (incl. capes)
        instance.abilities.flying = flying;
        return instance;
    }

    public static EntityPlayerMPFake createShadow(MinecraftServer server, ServerPlayerEntity player) {
        player.getServer().getPlayerList().remove(player);
        player.connection.disconnect(new TranslationTextComponent("multiplayer.disconnect.duplicate_login"));
        ServerWorld worldIn = player.getLevel();//.getWorld(player.dimension);
        PlayerInteractionManager interactionManagerIn = new PlayerInteractionManager(worldIn);
        GameProfile gameprofile = player.getGameProfile();
        EntityPlayerMPFake playerShadow = new EntityPlayerMPFake(server, worldIn, gameprofile, interactionManagerIn, true);
        server.getPlayerList().placeNewPlayer(new FakeClientConnection(PacketDirection.SERVERBOUND), playerShadow);

        playerShadow.setHealth(player.getHealth());
        playerShadow.connection.teleport(player.getX(), player.getY(), player.getZ(), player.yRot, player.xRot);
        playerShadow.gameMode.updateGameMode(player.gameMode.getGameModeForPlayer());
        ((IServerPlayer) playerShadow).getActionPack().copyFrom(((IServerPlayer) player).getActionPack());
        playerShadow.maxUpStep = 0.6F;
        playerShadow.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, player.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));


        server.getPlayerList().broadcastAll(new SEntityHeadLookPacket(playerShadow, (byte) (player.yHeadRot * 256 / 360)), playerShadow.level.dimension());
        server.getPlayerList().broadcastAll(new SPlayerListItemPacket(SPlayerListItemPacket.Action.ADD_PLAYER, playerShadow));
        //player.world.getChunkManager().updatePosition(playerShadow);
        playerShadow.abilities.flying = player.abilities.flying;
        return playerShadow;
    }

    private EntityPlayerMPFake(MinecraftServer server, ServerWorld worldIn, GameProfile profile, PlayerInteractionManager playerInteractionManager, boolean shadow) {
        super(server, worldIn, profile, playerInteractionManager);
        isAShadow = shadow;
    }

    @Override
    protected void playEquipSound(ItemStack itemStack) {
        if (!isUsingItem()) super.playEquipSound(itemStack);
    }

    @Override
    public void kill() {
        kill(Messenger.s("Killed"));
    }

    public void kill(ITextComponent reason) {
        shakeOff();
        this.server.tell(new TickDelayedTask(this.server.getTickCount(), () -> {
            this.connection.onDisconnect(reason);
        }));
    }

    @Override
    public void tick() {
        if (this.getServer().getTickCount() % 10 == 0) {
            this.connection.resetPosition();
            this.getLevel().getChunkSource().move(this);
            hasChangedDimension(); //<- causes hard crash but would need to be done to enable portals // not as of 1.17
        }
        try {
            super.tick();
            this.doTick();
        } catch (NullPointerException ignored) {
            // happens with that paper port thingy - not sure what that would fix, but hey
            // the game not gonna crash violently.
        }


    }

    private void shakeOff() {
        if (getVehicle() instanceof PlayerEntity) stopRiding();
        for (Entity passenger : getIndirectPassengers()) {
            if (passenger instanceof PlayerEntity) passenger.stopRiding();
        }
    }

    @Override
    public void die(DamageSource cause) {
        shakeOff();
        super.die(cause);
        setHealth(20);
        this.foodData = new FoodStats();
        kill(this.getCombatTracker().getDeathMessage());
    }

    @Override
    public String getIpAddress() {
        return "127.0.0.1";
    }
}