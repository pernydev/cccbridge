package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.blockEntityRenderer.RedRouterBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.display.TargetBlockDisplayTarget;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicModel;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicRenderer;
import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static cc.tweaked_programs.cccbridge.CCCBridge.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCCBridge {
    public static final String MOD_ID = "cccbridge";
    //public static final Logger LOGGER = LogUtils.getLogger();

    public CCCBridge() {
        CCCRegister.register();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public static void complete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), CCCRegister.SOURCE_BLOCK_ENTITY.get());
            AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_target"), new TargetBlockDisplayTarget()), CCCRegister.TARGET_BLOCK_ENTITY.get());

            ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CCCRegister.REDROUTER_BLOCK_ENTITY.get(), RedRouterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CCCRegister.ANIMATRONIC_BLOCK_ENTITY.get(), AnimatronicBlockEntityRenderer::new);
        event.registerEntityRenderer(CCCRegister.ANIMATRONIC_ENTITY.get(), AnimatronicRenderer::new);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(CCCRegister.SCROLLER_BLOCK.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AnimatronicModel.LAYER_LOCATION, AnimatronicModel::createBodyLayer);
    }
}
