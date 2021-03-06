package stargatetech2.core;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.api.StackManager;
import stargatetech2.core.block.BlockNaquadah;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.core.item.ItemNaquadah.Metadata;
import stargatetech2.core.item.ItemTabletPC;
import stargatetech2.core.util.ChunkLoader;
import stargatetech2.core.util.CoreEventHandler;
import stargatetech2.core.util.CoreWorldGenerator;
import stargatetech2.core.util.Stacks;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModuleCore implements IContentModule{
	public static BlockNaquadah naquadahBlock;
	
	public static ItemTabletPC tabletPC;
	public static ItemNaquadah naquadahItem;
	
	@Override
	public void preInit(){
		naquadahBlock = new BlockNaquadah();
		naquadahItem = new ItemNaquadah();
		tabletPC = new ItemTabletPC();
	}

	@Override
	public void init(){
		naquadahBlock.registerBlock();
		
		StackManager manager = StackManager.instance;
		manager.addStack("naquadahOre", new ItemStack(naquadahBlock));
		manager.addStack("tabletPC", new ItemStack(tabletPC));
		for(Metadata meta : naquadahItem.DATA){
			manager.addStack(meta.name, new ItemStack(naquadahItem, 1, meta.ID));
		}
		
		Stacks.init();
	}

	@Override
	public void postInit(){
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
		StargateTech2.proxy.registerLanguages();
		StargateTech2.proxy.registerRenderers(Module.CORE);
		GameRegistry.registerWorldGenerator(new CoreWorldGenerator());
		ChunkLoader.register();
		
		FurnaceRecipes.smelting().addSmelting(naquadahBlock.blockID, BlockNaquadah.ORE, Stacks.naqIngot, 0);
		FurnaceRecipes.smelting().addSmelting(naquadahItem.itemID, ItemNaquadah.DUST.ID, Stacks.naqIngot, 0);
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "CGC", "NRN", 'N', Stacks.naqPlate, 'C', Stacks.circuit, 'G', Stacks.glass, 'R', Stacks.redDust);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahItem, 2, ItemNaquadah.PLATE.ID), "SS", "SS", 'S', Stacks.naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahItem, 1, ItemNaquadah.COIL_NAQ.ID), "--R", "-N-", "R--", 'R', Stacks.redDust, 'N', Stacks.naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahItem, 1, ItemNaquadah.COIL_END.ID), "--R", "-E-", "R--", 'R', Stacks.redDust, 'E', Stacks.pearl);
		GameRegistry.addShapedRecipe(Stacks.naqBlock, "NNN", "NNN", "NNN", 'N', Stacks.naqIngot);
		GameRegistry.addShapelessRecipe(new ItemStack(naquadahItem, 9, ItemNaquadah.INGOT.ID), Stacks.naqBlock);
	}

	@Override public void onServerStart(){
		ChunkLoader.load();
	}
	
	@Override public void onServerStop(){
		ChunkLoader.unload();
	}

	@Override
	public String getModuleName() {
		return "Core";
	}
}