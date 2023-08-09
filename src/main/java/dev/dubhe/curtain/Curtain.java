package dev.dubhe.curtain;

import dev.dubhe.curtain.api.PlanExecution;
import dev.dubhe.curtain.api.rules.RuleManager;
import dev.dubhe.curtain.events.MyEventHandlers;
import dev.dubhe.curtain.features.logging.LoggerManager;
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
public class Curtain implements ICurtain {
    public static final String MODID = "curtain";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final List<ICurtain> SUB_MODS = new ArrayList<>();
    public static PlanExecution planExecution = null;
    public static RuleManager rules = null;
    public static MinecraftServer minecraftServer = null;

    public Curtain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MyEventHandlers.register();

        LoggerManager.registryBuiltinLogger();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        this.addRules(CurtainRules.class);
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
}
