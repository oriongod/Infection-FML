package net.minecraft.slimevoid.infection.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.slimevoid.infection.InfectionGamemode;
import net.minecraft.slimevoid.infection.fx.EntityInfectedWaterFX;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedSkeleton;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedSpider;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedZombie;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_Infection;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockInfectedWater extends BlockFluid {

	public BlockInfectedWater(int id) {
		super(id, Material.water);
		blockIndexInTexture = 10 * 16 + 9;
	}
	
	@Override
	public int getBlockTextureFromSide(int par1) {
        return blockIndexInTexture;
    }
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if(Block.blocksList[world.getBlockId(x, y + 1, z)] == null || !Block.blocksList[world.getBlockId(x, y + 1, z)].isOpaqueCube()) {
			if(world.rand.nextInt(2) == 0) world.spawnParticle("reddust", x + world.rand.nextDouble(), y + 1, z + world.rand.nextDouble(), -0.9D, 0.5D, 0.25D);
		}
		
		if (world.rand.nextInt(100) == 0)
        {
            double d = (float)x + world.rand.nextFloat();
            double d2 = (double)y + maxY;
            double d4 = (float)z + world.rand.nextFloat();
            //world.spawnParticle("lava", d, d2, d4, 10.0D, 50.0D, 100.0D);
            if (world.isRemote)
            	ModLoader.getMinecraftInstance().effectRenderer.addEffect(((EntityFX)(new EntityInfectedWaterFX(world, d, d2, d4))));
        }

        if (world.rand.nextInt(200) == 0)
        {
//        	world.playSoundEffect(x, y, z, "liquid.lava", 0.2F + world.rand.nextFloat() * 0.2F, 0.9F + world.rand.nextFloat() * 0.15F);
        }
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		InfectionGamemode.infection.activateInfection(i, j, k);
		world.setBlockMetadata(i, j, k, 0);
	}
	
	@Override
	public void breakBlock(World world, int i, int j, int k, int side, int metadata) {
		super.breakBlock(world, i, j, k, side, metadata);
		mod_Infection.instance.gamemode.desinfect(i, j, k);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		super.onNeighborBlockChange(world, i, j, k, l);
		InfectionGamemode.infection.activateInfection(i, j, k);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		super.onEntityCollidedWithBlock(world, i, j, k, entity);
		if (!(entity instanceof EntityMooshroom) &&!(entity instanceof EntityInfectedSpider) && !(entity instanceof EntityInfectedSkeleton) && !(entity instanceof EntityInfectedZombie)) {
			entity.attackEntityFrom(DamageSource.inFire, 1);
		}
		mod_Infection.instance.gamemode.infectEntity(world, i, j, k, entity);
	}
}
