package doggytalents.base.d;

import doggytalents.base.ObjectLib;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GuiFoodBowlWrapper extends GuiFoodBowl {

	public GuiFoodBowlWrapper(InventoryPlayer playerInventory, TileEntityFoodBowl foodBowlIn) {
		super(playerInventory, foodBowlIn);
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
    }
}
