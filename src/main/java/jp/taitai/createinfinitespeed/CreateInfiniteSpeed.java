package jp.taitai.createinfinitespeed;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(CreateInfiniteSpeed.MODID)
public class CreateInfiniteSpeed {
    public static final String MODID = "createinfinitespeed";

    public static final DeferredRegister<net.minecraft.world.level.block.Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<net.minecraft.world.level.block.Block> VARIABLE_SPEED_CONTROLLER =
            BLOCKS.register("variable_speed_controller", VariableSpeedControllerBlock::new);

    public static final RegistryObject<net.minecraft.world.level.block.Block> INFINITE_SPEED_CONTROLLER =
            BLOCKS.register("infinite_speed_controller", InfiniteSpeedControllerBlock::new);

    public static final RegistryObject<Item> VARIABLE_SPEED_ITEM =
            ITEMS.register("variable_speed_controller",
                    () -> new BlockItem(VARIABLE_SPEED_CONTROLLER.get(), new Item.Properties()));

    public static final RegistryObject<Item> INFINITE_SPEED_ITEM =
            ITEMS.register("infinite_speed_controller",
                    () -> new BlockItem(INFINITE_SPEED_CONTROLLER.get(), new Item.Properties()));

    public CreateInfiniteSpeed() {
        IEventBus bus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
