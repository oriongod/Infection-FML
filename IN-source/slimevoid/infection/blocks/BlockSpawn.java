package slimevoid.infection.blocks;

import slimevoid.infection.InfectionGamemode;
import slimevoid.infection.core.InfectionMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSpawn extends BlockContainer {

	public BlockSpawn(int id, int texture) {
		super(id, texture, Material.glass);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y - 1, z)];
		boolean onSolidBlock = block != null && block.isBlockSolid(world, x, y - 1, z, world.getBlockMetadata(x, y - 1, z)) && block.isOpaqueCube();
		boolean inGameArea = world.isRemote ? InfectionGamemode.infection.isInGameArea(x, y, z) : InfectionMod.isInGameArea(x, y, z);
		return super.canPlaceBlockAt(world, x, y, z) && onSolidBlock && inGameArea;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int l, float a, float b, float c) {
		// TODO: proxy.displayGuiScreen(new GuiShop());
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if (world.isRemote) InfectionMod.instance.gamemode.setSpawnBlock(x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySpawnBlock();
	}
}
