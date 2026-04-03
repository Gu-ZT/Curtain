package dev.dubhe.curtain;

import dev.dubhe.curtain.api.rules.RuleManager;
import dev.dubhe.curtain.events.MyEventHandlers;
import dev.dubhe.curtain.features.logging.LoggerManager;
import dev.dubhe.curtain.features.rules.fakes.MinecraftServerInterface;
import dev.dubhe.curtain.utils.PlanExecution;
import dev.dubhe.curtain.utils.ServerTickRateManager;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Mod(Curtain.MODID)
public class Curtain {
    public static final String MODID = "curtain";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final List<ICurtain> SUB_MODS = new ArrayList<>();
    public static PlanExecution planExecution = null;
    public static RuleManager rules = null;
    public static MinecraftServer minecraftServer = null;
    public static final List<ICurtain> extensions = new ArrayList<>();
    public static dev.dubhe.curtain.api.rules.RuleManager ruleManager;

    public Curtain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MyEventHandlers.register();

        LoggerManager.registryBuiltinLogger();
    }

    public static void manageExtension(ICurtain extension)
    {
        extensions.add(extension);
        // Stop the stupid practice of extensions mixing into Carpet just to register themselves
        if (StackWalker.getInstance().walk(stream -> stream.skip(1)
                .anyMatch(el -> el.getClassName() == Curtain.class.getName())))
        {
            CurtainRules.LOG.warn("""
                    Extension '%s' is registering itself using a mixin into Carpet instead of a regular ModInitializer!
                    This is stupid and will crash the game in future versions!""".formatted(extension.getClass().getSimpleName()));
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        RuleManager.addRules(CurtainRules.class);
        this.setTrans();
    }

    /**
     * 添加窗帘附属
     *
     * @param curtain 附属入口
     */
    public static void addSubMod(ICurtain curtain) {
        Curtain.SUB_MODS.add(curtain);
    }

    private void parseTrans(String name, InputStream stream) {
        TranslationHelper.addTransMap(name, TranslationHelper.getTranslationFromResourcePath(stream));
    }

    private void setTrans() {
        InputStream stream;
        stream = TranslationHelper.class.getClassLoader().getResourceAsStream("assets/curtain/lang/zh_cn.json");
        this.parseTrans("zh_cn", stream);
        stream = TranslationHelper.class.getClassLoader().getResourceAsStream("assets/curtain/lang/en_us.json");
        this.parseTrans("en_us", stream);
    }

    public static ResourceLocation of(String str) {
        return new ResourceLocation(MODID, str);
    }
    public static void onServerClosed(MinecraftServer server)
    {
        // this for whatever reason gets called multiple times even when joining on SP
        // so we allow to pass multiple times gating it only on existing server ref
        if (minecraftServer != null)
        {
            extensions.forEach(e -> e.onServerClosed(server));
            minecraftServer = null;
        }
    }

    public static void tick(MinecraftServer server)
    {
        ServerTickRateManager trm = ((MinecraftServerInterface) (MinecraftServer) server).getTickRateManager();
        trm.tick();
        //in case something happens
        CurtainRules.impendingFillSkipUpdates.set(false);

        extensions.forEach(e -> e.onTick(server));
    }
}
