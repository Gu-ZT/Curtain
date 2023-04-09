package dev.dubhe.curtain.features.player.patches;

import com.mojang.authlib.GameProfile;
import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.utils.Messenger;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLMCRegisterPacketHandler;

import javax.annotation.Nonnull;
import java.net.SocketAddress;

public class EntityPlayerMPFake extends ServerPlayerEntity {
    public Runnable fixStartingPosition = () -> {};
    public boolean isAShadow;

    public static EntityPlayerMPFake createFake(String username, MinecraftServer server, double d0, double d1, double d2, double yaw, double pitch, RegistryKey<World> dimensionId, GameType gamemode, boolean flying)
    {
        //prolly half of that crap is not necessary, but it works
        ServerWorld worldIn = server.getLevel(dimensionId);
        PlayerInteractionManager interactionManagerIn = new PlayerInteractionManager(worldIn);
        PlayerProfileCache.setUsesAuthentication(false);
        GameProfile gameprofile;
        try {
            gameprofile = server.getProfileCache().get(username);
        }
        finally {
            PlayerProfileCache.setUsesAuthentication(server.isDedicatedServer() && server.usesAuthentication());
        }
        if (gameprofile == null)
        {
            gameprofile = new GameProfile(PlayerEntity.createPlayerUUID(username), username);
        }
        if (gameprofile.getProperties().containsKey("textures"))
        {
            gameprofile = SkullTileEntity.updateGameprofile(gameprofile);
        }
        EntityPlayerMPFake instance = new EntityPlayerMPFake(server, worldIn, gameprofile, interactionManagerIn, false);
        instance.fixStartingPosition = () -> instance.absMoveTo(d0, d1, d2, (float) yaw, (float) pitch);
        NetworkManagerFake networkManager = new NetworkManagerFake(PacketDirection.SERVERBOUND);
        try {
            networkManager.channelActive(new ChannelHandlerContext() {
                @Override
                public Channel channel() {
                    return new Channel() {
                        @Override
                        public ChannelId id() {
                            return null;
                        }

                        @Override
                        public EventLoop eventLoop() {
                            return null;
                        }

                        @Override
                        public Channel parent() {
                            return null;
                        }

                        @Override
                        public ChannelConfig config() {
                            return new DefaultChannelConfig(this);
                        }

                        @Override
                        public boolean isOpen() {
                            return false;
                        }

                        @Override
                        public boolean isRegistered() {
                            return false;
                        }

                        @Override
                        public boolean isActive() {
                            return false;
                        }

                        @Override
                        public ChannelMetadata metadata() {
                            return new ChannelMetadata(false);
                        }

                        @Override
                        public SocketAddress localAddress() {
                            return null;
                        }

                        @Override
                        public SocketAddress remoteAddress() {
                            return null;
                        }

                        @Override
                        public ChannelFuture closeFuture() {
                            return null;
                        }

                        @Override
                        public boolean isWritable() {
                            return false;
                        }

                        @Override
                        public long bytesBeforeUnwritable() {
                            return 0;
                        }

                        @Override
                        public long bytesBeforeWritable() {
                            return 0;
                        }

                        @Override
                        public Unsafe unsafe() {
                            return null;
                        }

                        @Override
                        public ChannelPipeline pipeline() {
                            return null;
                        }

                        @Override
                        public ByteBufAllocator alloc() {
                            return null;
                        }

                        @Override
                        public Channel read() {
                            return null;
                        }

                        @Override
                        public Channel flush() {
                            return null;
                        }

                        @Override
                        public ChannelFuture bind(SocketAddress localAddress) {
                            return null;
                        }

                        @Override
                        public ChannelFuture connect(SocketAddress remoteAddress) {
                            return null;
                        }

                        @Override
                        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
                            return null;
                        }

                        @Override
                        public ChannelFuture disconnect() {
                            return null;
                        }

                        @Override
                        public ChannelFuture close() {
                            return null;
                        }

                        @Override
                        public ChannelFuture deregister() {
                            return null;
                        }

                        @Override
                        public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture disconnect(ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture close(ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture deregister(ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture write(Object msg) {
                            return null;
                        }

                        @Override
                        public ChannelFuture write(Object msg, ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
                            return null;
                        }

                        @Override
                        public ChannelFuture writeAndFlush(Object msg) {
                            return null;
                        }

                        @Override
                        public ChannelPromise newPromise() {
                            return null;
                        }

                        @Override
                        public ChannelProgressivePromise newProgressivePromise() {
                            return null;
                        }

                        @Override
                        public ChannelFuture newSucceededFuture() {
                            return null;
                        }

                        @Override
                        public ChannelFuture newFailedFuture(Throwable cause) {
                            return null;
                        }

                        @Override
                        public ChannelPromise voidPromise() {
                            return null;
                        }

                        @Override
                        public <T> Attribute<T> attr(AttributeKey<T> key) {
                            return new Attribute<T>() {
                                @Override
                                public AttributeKey<T> key() {
                                    return null;
                                }

                                @Override
                                public T get() {
                                    String name = key.name();
                                    if ("fml:netversion".equals(name)) return (T)"FML***";
                                    else if ("minecraft:netregistry".equals(name)) return (T)new FMLMCRegisterPacketHandler.ChannelList();
                                    else return null;
                                }

                                @Override
                                public void set(T value) {

                                }

                                @Override
                                public T getAndSet(T value) {
                                    return null;
                                }

                                @Override
                                public T setIfAbsent(T value) {
                                    return null;
                                }

                                @Override
                                public T getAndRemove() {
                                    return null;
                                }

                                @Override
                                public boolean compareAndSet(T oldValue, T newValue) {
                                    return false;
                                }

                                @Override
                                public void remove() {

                                }
                            };
                        }

                        @Override
                        public <T> boolean hasAttr(AttributeKey<T> key) {
                            return false;
                        }

                        @Override
                        public int compareTo(Channel o) {
                            return 0;
                        }
                    };
                }

                @Override
                public EventExecutor executor() {
                    return null;
                }

                @Override
                public String name() {
                    return null;
                }

                @Override
                public ChannelHandler handler() {
                    return null;
                }

                @Override
                public boolean isRemoved() {
                    return false;
                }

                @Override
                public ChannelHandlerContext fireChannelRegistered() {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireChannelUnregistered() {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireChannelActive() {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireChannelInactive() {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireExceptionCaught(Throwable cause) {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireUserEventTriggered(Object evt) {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireChannelRead(Object msg) {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireChannelReadComplete() {
                    return null;
                }

                @Override
                public ChannelHandlerContext fireChannelWritabilityChanged() {
                    return null;
                }

                @Override
                public ChannelHandlerContext read() {
                    return null;
                }

                @Override
                public ChannelHandlerContext flush() {
                    return null;
                }

                @Override
                public ChannelPipeline pipeline() {
                    return null;
                }

                @Override
                public ByteBufAllocator alloc() {
                    return null;
                }

                @Override
                public <T> Attribute<T> attr(AttributeKey<T> key) {
                    return null;
                }

                @Override
                public <T> boolean hasAttr(AttributeKey<T> key) {
                    return false;
                }

                @Override
                public ChannelFuture bind(SocketAddress localAddress) {
                    return null;
                }

                @Override
                public ChannelFuture connect(SocketAddress remoteAddress) {
                    return null;
                }

                @Override
                public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
                    return null;
                }

                @Override
                public ChannelFuture disconnect() {
                    return null;
                }

                @Override
                public ChannelFuture close() {
                    return null;
                }

                @Override
                public ChannelFuture deregister() {
                    return null;
                }

                @Override
                public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture disconnect(ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture close(ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture deregister(ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture write(Object msg) {
                    return null;
                }

                @Override
                public ChannelFuture write(Object msg, ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
                    return null;
                }

                @Override
                public ChannelFuture writeAndFlush(Object msg) {
                    return null;
                }

                @Override
                public ChannelPromise newPromise() {
                    return null;
                }

                @Override
                public ChannelProgressivePromise newProgressivePromise() {
                    return null;
                }

                @Override
                public ChannelFuture newSucceededFuture() {
                    return null;
                }

                @Override
                public ChannelFuture newFailedFuture(Throwable cause) {
                    return null;
                }

                @Override
                public ChannelPromise voidPromise() {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.getPlayerList().placeNewPlayer(networkManager, instance);
        instance.teleportTo(worldIn, d0, d1, d2, (float)yaw, (float)pitch);
        instance.setHealth(20.0F);
        instance.removed = false;
        instance.maxUpStep = 0.6F;
        interactionManagerIn.setGameModeForPlayer(gamemode);
        server.getPlayerList().broadcastAll(new SEntityHeadLookPacket(instance, (byte) (instance.yHeadRot * 256 / 360)), dimensionId);//instance.dimension);
        server.getPlayerList().broadcastAll(new SEntityTeleportPacket(instance), dimensionId);//instance.dimension);
        instance.getLevel().getChunkSource().move(instance);
        instance.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0x7f); // show all model layers (incl. capes)
        instance.abilities.flying = flying;
        return instance;
    }

    public static EntityPlayerMPFake createShadow(MinecraftServer server, ServerPlayerEntity player)
    {
        player.getServer().getPlayerList().remove(player);
        player.connection.disconnect(new TranslationTextComponent("multiplayer.disconnect.duplicate_login"));
        ServerWorld worldIn = player.getLevel();//.getWorld(player.dimension);
        PlayerInteractionManager interactionManagerIn = new PlayerInteractionManager(worldIn);
        GameProfile gameprofile = player.getGameProfile();
        EntityPlayerMPFake playerShadow = new EntityPlayerMPFake(server, worldIn, gameprofile, interactionManagerIn, true);
        server.getPlayerList().placeNewPlayer(new NetworkManagerFake(PacketDirection.SERVERBOUND), playerShadow);

        playerShadow.setHealth(player.getHealth());
        playerShadow.connection.teleport(player.getX(), player.getY(), player.getZ(), player.yRot, player.xRot);
        interactionManagerIn.setGameModeForPlayer(player.gameMode.getGameModeForPlayer());
        ((IServerPlayer) playerShadow).getActionPack().copyFrom(((IServerPlayer) player).getActionPack());
        playerShadow.maxUpStep = 0.6F;
        playerShadow.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, player.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));


        server.getPlayerList().broadcastAll(new SEntityHeadLookPacket(playerShadow, (byte) (player.yHeadRot * 256 / 360)), playerShadow.level.dimension());
        server.getPlayerList().broadcastAll(new SPlayerListItemPacket(SPlayerListItemPacket.Action.ADD_PLAYER, playerShadow));
        player.getLevel().getChunkSource().move(playerShadow);
        playerShadow.abilities.flying = player.abilities.flying;
        return playerShadow;
    }

    private EntityPlayerMPFake(MinecraftServer server, ServerWorld worldIn, GameProfile profile, PlayerInteractionManager interactionManagerIn, boolean shadow)
    {
        super(server, worldIn, profile, interactionManagerIn);
        isAShadow = shadow;
    }

    @Override
    protected void playEquipSound(ItemStack stack)
    {
        if (!isUsingItem()) super.playEquipSound(stack);
    }

    @Override
    public void kill()
    {
        kill(new StringTextComponent("Killed"));
    }

    public void kill(ITextComponent reason)
    {
        shakeOff();
        this.server.tell(new TickDelayedTask(this.server.getTickCount(), () -> this.connection.onDisconnect(reason)));
    }

    @Override
    public void tick()
    {
        if (this.getServer().getTickCount() % 10 == 0)
        {
            this.connection.resetPosition();
            this.getLevel().getChunkSource().move(this);
            //if (netherPortalCooldown==10) onTeleportationDone(); <- causes hard crash but would need to be done to enable portals
        }
        super.tick();
        this.doTick();
    }

    private void shakeOff()
    {
        if (getVehicle() instanceof PlayerEntity) stopRiding();
        for (Entity passenger : getIndirectPassengers())
        {
            if (passenger instanceof PlayerEntity) passenger.stopRiding();
        }
    }

    @Override
    public void die(@Nonnull DamageSource cause)
    {
        shakeOff();
        super.die(cause);
        setHealth(20);
        this.foodData = new FoodStats();
        kill(this.getCombatTracker().getDeathMessage());
    }

    @Override
    public @Nonnull String getIpAddress() {
        return "127.0.0.1";
    }
}